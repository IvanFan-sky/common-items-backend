package com.common.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.common.core.dto.user.*;
import com.common.core.entity.SysUser;
import com.common.core.mapper.UserConvertMapper;
import com.common.core.util.CaptchaUtils;
import com.common.core.util.LoginSecurityUtil;
import com.common.core.vo.*;
import com.common.core.vo.auth.LoginVO;
import com.common.core.vo.user.UserDetailVO;
import com.common.core.vo.user.UserVO;
import com.common.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 用户管理控制器，提供用户注册、登录、信息管理等功能
 * @Date 2025/1/7 16:00
 * @Author SparkFan
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户注册、登录、信息管理等相关接口")
public class UserController {

    private final UserService userService;
    private final UserConvertMapper convertMapper;
    private final CaptchaUtils captchaUtils;
    private final LoginSecurityUtil loginSecurityUtil;

    /**
     * 用户注册
     */
    @SaIgnore
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册接口，无需登录")
    public Result<Long> register(@Valid @RequestBody UserCreateDTO createDTO) {
        log.info("用户注册请求，用户名：{}", createDTO.getUsername());
        Long userId = userService.register(createDTO);
        return Result.success("注册成功", userId);
    }

    /**
     * 获取验证码
     */
    @SaIgnore
    @GetMapping("/captcha")
    @Operation(summary = "获取验证码", description = "生成登录验证码")
    public Result<Map<String, String>> getCaptcha() {
        Map<String, String> captcha = captchaUtils.generateCaptcha();
        return Result.success(captcha);
    }

    /**
     * 用户登录
     */
    @SaIgnore
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录验证接口，支持用户名/邮箱/手机号登录，包含验证码验证和安全检查")
    public Result<LoginVO> login(@Valid @RequestBody UserLoginDTO loginDTO, HttpServletRequest request) {
        String username = loginDTO.getUsername();
        log.info("用户登录请求，用户名：{}", username);
        
        try {
            // 1. 检查账户是否被锁定
            if (!loginSecurityUtil.canLogin(username)) {
                String errorMessage = loginSecurityUtil.getLoginFailureMessage(username);
                return Result.error(errorMessage);
            }
            
            // 2. 验证验证码（如果提供了验证码）
            if (StrUtil.isNotBlank(loginDTO.getCaptcha()) && StrUtil.isNotBlank(loginDTO.getCaptchaKey())) {
                if (!captchaUtils.verifyCaptcha(loginDTO.getCaptchaKey(), loginDTO.getCaptcha())) {
                    loginSecurityUtil.recordLoginFailure(username);
                    return Result.error("验证码错误");
                }
            }
            
            // 3. 获取客户端IP
            String loginIp = getClientIp(request);
            
            // 4. 验证登录
            SysUser user = userService.login(username, loginDTO.getPassword(), loginIp);
            
            // 5. 登录成功，清除失败记录
            loginSecurityUtil.clearLoginFailure(username);
            
            // 6. 生成token
            StpUtil.login(user.getId());
            String accessToken = StpUtil.getTokenValue();
            
            // 7. 构建登录响应
            LoginVO loginVO = buildLoginResponse(user, accessToken, loginIp);
            
            log.info("用户登录成功，用户ID：{}，用户名：{}，IP：{}", user.getId(), user.getUsername(), loginIp);
            return Result.success(loginVO);
            
        } catch (Exception e) {
            // 记录登录失败
            loginSecurityUtil.recordLoginFailure(username);
            String errorMessage = loginSecurityUtil.getLoginFailureMessage(username);
            log.warn("用户登录失败，用户名：{}，错误：{}", username, e.getMessage());
            return Result.error(errorMessage);
        }
    }

    /**
     * 构建登录响应对象
     */
    private LoginVO buildLoginResponse(SysUser user, String accessToken, String loginIp) {
        return LoginVO.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(StpUtil.getTokenTimeout())
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .email(user.hasEmail() ? user.getEmail() : null)
                .phone(user.hasPhone() ? user.getPhone() : null)
                .status(user.getStatus())
                .displayName(user.getNickname() != null ? user.getNickname() : user.getUsername())
                .loginTime(LocalDateTime.now())
                .loginIp(loginIp)
                .lastLoginTime(user.getLoginTime())
                .sessionId(StpUtil.getTokenValue())
                .message("登录成功")
                // TODO: 从角色权限表查询
                .roles(List.of())
                .permissions(List.of())
                .superAdmin(false)
                .firstLogin(user.getLoginTime() == null)
                .build();
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户退出登录")
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.success("退出成功");
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的基本信息")
    public Result<UserVO> getCurrentUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        UserVO userVO = userService.getUserInfo(userId);
        return Result.success(userVO);
    }

    /**
     * 获取用户详细信息
     */
    @GetMapping("/detail/{userId}")
    @Operation(summary = "获取用户详细信息", description = "根据用户ID获取用户详细信息")
    @SaCheckPermission("user:view")
    public Result<UserDetailVO> getUserDetail(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        UserDetailVO userDetailVO = userService.getUserDetail(userId);
        return Result.success(userDetailVO);
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询用户列表", description = "分页查询用户列表，支持多条件筛选")
    @SaCheckPermission("user:list")
    public Result<PageResult<UserVO>> getUserPage(@Valid UserQueryDTO queryDTO) {
        PageResult<UserVO> pageResult = userService.getUserPage(queryDTO);
        return Result.success(pageResult);
    }

    /**
     * 创建用户（管理员功能）
     */
    @PostMapping("/create")
    @Operation(summary = "创建用户", description = "管理员创建新用户")
    @SaCheckPermission("user:create")
    public Result<Long> createUser(@Valid @RequestBody UserCreateDTO createDTO) {
        Long createBy = StpUtil.getLoginIdAsLong();
        Long userId = userService.createUser(createDTO, createBy);
        return Result.success("用户创建成功", userId);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{userId}")
    @Operation(summary = "更新用户信息", description = "更新指定用户的信息")
    @SaCheckPermission("user:update")
    public Result<Void> updateUser(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Valid @RequestBody UserUpdateDTO updateDTO) {
        Long updateBy = StpUtil.getLoginIdAsLong();
        userService.updateUser(userId, updateDTO, updateBy);
        return Result.success("用户信息更新成功");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userId}")
    @Operation(summary = "删除用户", description = "逻辑删除指定用户")
    @SaCheckPermission("user:delete")
    public Result<Void> deleteUser(@Parameter(description = "用户ID") @PathVariable Long userId) {
        Long deleteBy = StpUtil.getLoginIdAsLong();
        userService.deleteUser(userId, deleteBy);
        return Result.success("用户删除成功");
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户", description = "批量逻辑删除用户")
    @SaCheckPermission("user:delete")
    public Result<Void> batchDeleteUsers(@RequestBody List<Long> userIds) {
        Long deleteBy = StpUtil.getLoginIdAsLong();
        userService.batchDeleteUsers(userIds, deleteBy);
        return Result.success("批量删除成功");
    }

    /**
     * 更新用户状态
     */
    @PutMapping("/{userId}/status")
    @Operation(summary = "更新用户状态", description = "启用或禁用用户")
    @SaCheckPermission("user:update")
    public Result<Void> updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "状态（0-禁用，1-启用）") @RequestParam Integer status) {
        Long updateBy = StpUtil.getLoginIdAsLong();
        userService.updateUserStatus(userId, status, updateBy);
        return Result.success("用户状态更新成功");
    }

    /**
     * 批量更新用户状态
     */
    @PutMapping("/batch/status")
    @Operation(summary = "批量更新用户状态", description = "批量启用或禁用用户")
    @SaCheckPermission("user:update")
    public Result<Void> batchUpdateUserStatus(
            @RequestBody List<Long> userIds,
            @Parameter(description = "状态（0-禁用，1-启用）") @RequestParam Integer status) {
        Long updateBy = StpUtil.getLoginIdAsLong();
        userService.batchUpdateUserStatus(userIds, status, updateBy);
        return Result.success("批量更新状态成功");
    }

    /**
     * 重置用户密码
     */
    @PutMapping("/{userId}/password/reset")
    @Operation(summary = "重置用户密码", description = "管理员重置用户密码")
    @SaCheckPermission("user:password:reset")
    public Result<Void> resetPassword(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        Long updateBy = StpUtil.getLoginIdAsLong();
        userService.resetPassword(userId, newPassword, updateBy);
        return Result.success("密码重置成功");
    }

    /**
     * 修改密码
     */
    @PutMapping("/password/change")
    @Operation(summary = "修改密码", description = "用户修改自己的密码")
    public Result<Void> changePassword(@Valid @RequestBody UserPasswordUpdateDTO passwordUpdateDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        userService.changePassword(userId, passwordUpdateDTO);
        return Result.success("密码修改成功");
    }

    /**
     * 检查用户名是否可用
     */
    @GetMapping("/check/username")
    @Operation(summary = "检查用户名是否可用", description = "验证用户名是否已被占用")
    @SaIgnore
    public Result<Boolean> checkUsernameAvailable(
            @Parameter(description = "用户名") @RequestParam String username,
            @Parameter(description = "排除的用户ID") @RequestParam(required = false) Long excludeId) {
        Boolean available = userService.checkUsernameAvailable(username, excludeId);
        return Result.success(available);
    }

    /**
     * 检查邮箱是否可用
     */
    @GetMapping("/check/email")
    @Operation(summary = "检查邮箱是否可用", description = "验证邮箱是否已被占用")
    @SaIgnore
    public Result<Boolean> checkEmailAvailable(
            @Parameter(description = "邮箱") @RequestParam String email,
            @Parameter(description = "排除的用户ID") @RequestParam(required = false) Long excludeId) {
        Boolean available = userService.checkEmailAvailable(email, excludeId);
        return Result.success(available);
    }

    /**
     * 检查手机号是否可用
     */
    @GetMapping("/check/phone")
    @Operation(summary = "检查手机号是否可用", description = "验证手机号是否已被占用")
    @SaIgnore
    public Result<Boolean> checkPhoneAvailable(
            @Parameter(description = "手机号") @RequestParam String phone,
            @Parameter(description = "排除的用户ID") @RequestParam(required = false) Long excludeId) {
        Boolean available = userService.checkPhoneAvailable(phone, excludeId);
        return Result.success(available);
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 处理多个IP的情况，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新Token", description = "刷新访问令牌")
    public Result<LoginVO> refreshToken() {
        try {
            // 检查当前token是否有效
            if (!StpUtil.isLogin()) {
                return Result.unauthorized("未登录或token已过期");
            }
            
            Long userId = StpUtil.getLoginIdAsLong();
            SysUser user = userService.getById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            // 续签token
            StpUtil.renewTimeout(7200); // 2小时
            String newToken = StpUtil.getTokenValue();
            
            // 构建响应
            LoginVO loginVO = buildLoginResponse(user, newToken, "");
            
            log.info("Token刷新成功，用户ID：{}", userId);
            return Result.success(loginVO);
            
        } catch (Exception e) {
            log.error("Token刷新失败", e);
            return Result.error("Token刷新失败");
        }
    }

    /**
     * 获取在线用户列表
     */
    @GetMapping("/online/list")
    @Operation(summary = "获取在线用户列表", description = "查询当前在线的用户列表")
    @SaCheckPermission("user:online:list")
    public Result<List<Map<String, Object>>> getOnlineUsers() {
        try {
            // 获取所有在线token
            List<String> tokenList = StpUtil.searchTokenValue("", 0, -1, false);
            List<Map<String, Object>> onlineUsers = new ArrayList<>();
            
            for (String token : tokenList) {
                try {
                    Object loginId = StpUtil.getLoginIdByToken(token);
                    if (loginId != null) {
                        Long userId = Long.valueOf(loginId.toString());
                        SysUser user = userService.getById(userId);
                        if (user != null) {
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("userId", user.getId());
                            userInfo.put("username", user.getUsername());
                            userInfo.put("nickname", user.getNickname());
                            userInfo.put("loginTime", System.currentTimeMillis());
                            userInfo.put("lastActivityTime", System.currentTimeMillis());
                            userInfo.put("tokenValue", token);
                            onlineUsers.add(userInfo);
                        }
                    }
                } catch (Exception e) {
                    log.warn("获取在线用户信息失败，token：{}", token, e);
                }
            }
            
            return Result.success(onlineUsers);
            
        } catch (Exception e) {
            log.error("获取在线用户列表失败", e);
            return Result.error("获取在线用户列表失败");
        }
    }

    /**
     * 强制用户下线
     */
    @PostMapping("/online/logout/{userId}")
    @Operation(summary = "强制用户下线", description = "管理员强制指定用户下线")
    @SaCheckPermission("user:online:logout")
    public Result<Void> forceLogout(@Parameter(description = "用户ID") @PathVariable Long userId) {
        try {
            StpUtil.logout(userId);
            log.info("强制用户下线成功，用户ID：{}", userId);
            return Result.success("用户已强制下线");
        } catch (Exception e) {
            log.error("强制用户下线失败，用户ID：{}", userId, e);
            return Result.error("强制下线失败");
        }
    }

    /**
     * 解锁账户
     */
    @PostMapping("/unlock/{identifier}")
    @Operation(summary = "解锁账户", description = "管理员解锁被锁定的账户")
    @SaCheckPermission("user:unlock")
    public Result<Void> unlockAccount(@Parameter(description = "登录标识") @PathVariable String identifier) {
        try {
            loginSecurityUtil.unlockAccount(identifier);
            log.info("账户解锁成功，标识：{}", identifier);
            return Result.success("账户解锁成功");
        } catch (Exception e) {
            log.error("账户解锁失败，标识：{}", identifier, e);
            return Result.error("账户解锁失败");
        }
    }

    /**
     * 获取账户锁定状态
     */
    @GetMapping("/lock/status/{identifier}")
    @Operation(summary = "获取账户锁定状态", description = "查询账户是否被锁定及剩余时间")
    @SaCheckPermission("user:lock:status")
    public Result<Map<String, Object>> getLockStatus(@Parameter(description = "登录标识") @PathVariable String identifier) {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("locked", loginSecurityUtil.isAccountLocked(identifier));
            status.put("failureCount", loginSecurityUtil.getLoginFailureCount(identifier));
            status.put("maxAttempts", loginSecurityUtil.getMaxLoginAttempts());
            status.put("remainingTime", loginSecurityUtil.getAccountLockRemainingTime(identifier));
            status.put("message", loginSecurityUtil.getLoginFailureMessage(identifier));
            
            return Result.success(status);
        } catch (Exception e) {
            log.error("获取账户锁定状态失败，标识：{}", identifier, e);
            return Result.error("获取锁定状态失败");
        }
    }
} 