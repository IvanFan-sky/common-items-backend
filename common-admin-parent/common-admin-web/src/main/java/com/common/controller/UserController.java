package com.common.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.common.core.dto.*;
import com.common.core.entity.SysUser;
import com.common.core.vo.PageResult;
import com.common.core.vo.Result;
import com.common.core.vo.UserDetailVO;
import com.common.core.vo.UserVO;
import com.common.service.UserService;
import com.common.core.mapper.UserConvertMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

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
     * 用户登录
     */
    @SaIgnore
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录验证接口，支持用户名/邮箱/手机号登录")
    public Result<String> login(@Valid @RequestBody UserLoginDTO loginDTO, HttpServletRequest request) {
        log.info("用户登录请求，用户名：{}", loginDTO.getUsername());
        
        // 获取客户端IP
        String loginIp = getClientIp(request);
        
        // 验证登录
        SysUser user = userService.login(loginDTO.getUsername(), loginDTO.getPassword(), loginIp);
        
        // 生成token
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        
        log.info("用户登录成功，用户ID：{}，用户名：{}", user.getId(), user.getUsername());
        return Result.success(token, "登录成功");
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
} 