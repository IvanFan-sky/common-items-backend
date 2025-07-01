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
import com.common.core.vo.user.UserProfileVO;
import com.common.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    // ==================== 导入导出功能 ====================

    /**
     * 导出用户数据
     */
    @PostMapping("/export")
    @Operation(summary = "导出用户", description = "根据查询条件导出用户数据到Excel")
    @SaCheckPermission("user:export")
    public void exportUsers(@RequestBody(required = false) com.common.core.dto.user.UserQueryDTO queryDTO, 
                           HttpServletResponse response) {
        try {
            List<com.common.core.vo.user.UserExportVO> exportData = userService.exportUsers(queryDTO);
            com.common.core.util.ExcelUtils.exportExcel(response, exportData, 
                com.common.core.vo.user.UserExportVO.class, "用户数据");
        } catch (Exception e) {
            log.error("导出用户数据失败", e);
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    /**
     * 下载用户导入模板
     */
    @GetMapping("/import/template")
    @Operation(summary = "下载导入模板", description = "下载用户导入Excel模板")
    @SaCheckPermission("user:import")
    public void downloadImportTemplate(HttpServletResponse response) {
        try {
            List<com.common.core.dto.user.UserImportDTO> templateData = userService.getUserImportTemplate();
            com.common.core.util.ExcelUtils.exportExcel(response, templateData, 
                com.common.core.dto.user.UserImportDTO.class, "用户导入模板");
        } catch (Exception e) {
            log.error("下载用户导入模板失败", e);
            throw new RuntimeException("下载模板失败：" + e.getMessage());
        }
    }

    /**
     * 导入用户数据
     */
    @PostMapping("/import")
    @Operation(summary = "导入用户", description = "从Excel文件导入用户数据")
    @SaCheckPermission("user:import")
    public Result<com.common.core.vo.ImportResultVO> importUsers(
            @RequestParam("file") MultipartFile file) {
        
        // 验证文件
        if (file == null || file.isEmpty()) {
            return Result.error("请选择要导入的文件");
        }
        
        if (!com.common.core.util.ExcelUtils.isExcelFile(file)) {
            return Result.error("请上传Excel文件（.xlsx或.xls格式）");
        }
        
        // 文件大小限制（10MB）
        if (!com.common.core.util.ExcelUtils.isValidFileSize(file, 10 * 1024 * 1024)) {
            return Result.error("文件大小不能超过10MB");
        }
        
        try {
            // 读取Excel数据
            List<com.common.core.dto.user.UserImportDTO> importDataList = 
                com.common.core.util.ExcelUtils.importExcel(file, com.common.core.dto.user.UserImportDTO.class);
            
            if (importDataList.isEmpty()) {
                return Result.error("文件中没有有效数据");
            }
            
            // 获取当前登录用户ID
            Long currentUserId = getCurrentUserId();
            
            // 执行导入
            com.common.core.vo.ImportResultVO result = userService.importUsers(importDataList, currentUserId);
            
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("导入用户数据失败", e);
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        try {
            return StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            throw new RuntimeException("获取当前用户信息失败");
        }
    }

    // ==================== 个人信息管理功能 ====================

    /**
     * 获取个人资料
     */
    @GetMapping("/profile")
    @Operation(summary = "获取个人资料", description = "获取当前用户的个人资料信息")
    public Result<UserProfileVO> getUserProfile() {
        try {
            Long userId = getCurrentUserId();
            UserProfileVO profileVO = userService.getUserProfile(userId);
            return Result.success(profileVO);
        } catch (Exception e) {
            log.error("获取个人资料失败", e);
            return Result.error("获取个人资料失败：" + e.getMessage());
        }
    }

    /**
     * 更新个人资料
     */
    @PutMapping("/profile")
    @Operation(summary = "更新个人资料", description = "用户更新自己的个人资料")
    public Result<Void> updateUserProfile(@Valid @RequestBody UserProfileUpdateDTO profileUpdateDTO) {
        try {
            Long userId = getCurrentUserId();
            userService.updateUserProfile(userId, profileUpdateDTO);
            return Result.success("个人资料更新成功");
        } catch (Exception e) {
            log.error("更新个人资料失败", e);
            return Result.error("更新个人资料失败：" + e.getMessage());
        }
    }

    /**
     * 更新头像
     */
    @PutMapping("/profile/avatar")
    @Operation(summary = "更新头像", description = "用户更新自己的头像")
    public Result<Void> updateUserAvatar(@Valid @RequestBody UserAvatarUpdateDTO avatarUpdateDTO) {
        try {
            Long userId = getCurrentUserId();
            userService.updateUserAvatar(userId, avatarUpdateDTO);
            return Result.success("头像更新成功");
        } catch (Exception e) {
            log.error("更新头像失败", e);
            return Result.error("更新头像失败：" + e.getMessage());
        }
    }

    /**
     * 更新安全设置
     */
    @PutMapping("/profile/security")
    @Operation(summary = "更新安全设置", description = "用户更新自己的安全设置")
    public Result<Void> updateUserSecuritySettings(@Valid @RequestBody UserSecuritySettingsDTO securitySettingsDTO) {
        try {
            Long userId = getCurrentUserId();
            userService.updateUserSecuritySettings(userId, securitySettingsDTO);
            return Result.success("安全设置更新成功");
        } catch (Exception e) {
            log.error("更新安全设置失败", e);
            return Result.error("更新安全设置失败：" + e.getMessage());
        }
    }

    /**
     * 验证当前密码
     */
    @PostMapping("/profile/password/verify")
    @Operation(summary = "验证当前密码", description = "验证用户当前密码是否正确")
    public Result<Boolean> verifyCurrentPassword(@RequestParam String currentPassword) {
        try {
            Long userId = getCurrentUserId();
            Boolean isValid = userService.verifyCurrentPassword(userId, currentPassword);
            return Result.success(isValid);
        } catch (Exception e) {
            log.error("验证当前密码失败", e);
            return Result.error("验证失败：" + e.getMessage());
        }
    }

    /**
     * 生成双因子认证密钥
     */
    @PostMapping("/profile/2fa/generate")
    @Operation(summary = "生成双因子认证密钥", description = "生成用户的双因子认证密钥和二维码")
    public Result<Map<String, String>> generateTwoFactorSecret() {
        try {
            Long userId = getCurrentUserId();
            Map<String, String> result = userService.generateTwoFactorSecret(userId);
            return Result.success(result);
        } catch (Exception e) {
            log.error("生成双因子认证密钥失败", e);
            return Result.error("生成失败：" + e.getMessage());
        }
    }

    /**
     * 验证双因子认证码
     */
    @PostMapping("/profile/2fa/verify")
    @Operation(summary = "验证双因子认证码", description = "验证用户输入的双因子认证码")
    public Result<Boolean> verifyTwoFactorCode(@RequestParam String code) {
        try {
            Long userId = getCurrentUserId();
            Boolean isValid = userService.verifyTwoFactorCode(userId, code);
            return Result.success(isValid);
        } catch (Exception e) {
            log.error("验证双因子认证码失败", e);
            return Result.error("验证失败：" + e.getMessage());
        }
    }

    /**
     * 获取登录历史
     */
    @GetMapping("/profile/login-history")
    @Operation(summary = "获取登录历史", description = "获取用户的登录历史记录")
    public Result<List<Map<String, Object>>> getUserLoginHistory(
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            Long userId = getCurrentUserId();
            List<Map<String, Object>> loginHistory = userService.getUserLoginHistory(userId, limit);
            return Result.success(loginHistory);
        } catch (Exception e) {
            log.error("获取登录历史失败", e);
            return Result.error("获取登录历史失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备信息
     */
    @GetMapping("/profile/devices")
    @Operation(summary = "获取设备信息", description = "获取用户的登录设备信息")
    public Result<List<Map<String, Object>>> getUserDevices() {
        try {
            Long userId = getCurrentUserId();
            List<Map<String, Object>> devices = userService.getUserDevices(userId);
            return Result.success(devices);
        } catch (Exception e) {
            log.error("获取设备信息失败", e);
            return Result.error("获取设备信息失败：" + e.getMessage());
        }
    }

    /**
     * 移除设备
     */
    @DeleteMapping("/profile/devices/{deviceId}")
    @Operation(summary = "移除设备", description = "移除指定的登录设备")
    public Result<Void> removeUserDevice(@Parameter(description = "设备ID") @PathVariable String deviceId) {
        try {
            Long userId = getCurrentUserId();
            userService.removeUserDevice(userId, deviceId);
            return Result.success("设备移除成功");
        } catch (Exception e) {
            log.error("移除设备失败", e);
            return Result.error("移除设备失败：" + e.getMessage());
        }
    }
} 