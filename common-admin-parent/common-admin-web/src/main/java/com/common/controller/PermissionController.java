package com.common.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.common.core.dto.permission.PermissionCreateDTO;
import com.common.core.dto.permission.PermissionQueryDTO;
import com.common.core.dto.permission.PermissionUpdateDTO;
import com.common.core.vo.PageResult;
import com.common.core.vo.permission.PermissionTreeVO;
import com.common.core.vo.permission.PermissionVO;
import com.common.core.vo.Result;
import com.common.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * @Description 权限管理控制器，处理权限相关的HTTP请求
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Tag(name = "权限管理", description = "权限管理相关接口")
@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 分页查询权限列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询权限列表", description = "分页查询权限列表，支持多条件筛选")
    @SaCheckPermission("system:permission:list")
    public Result<PageResult<PermissionVO>> getPermissionPage(@Valid PermissionQueryDTO queryDTO) {
        PageResult<PermissionVO> pageResult = permissionService.getPermissionPage(queryDTO);
        return Result.success(pageResult);
    }

    /**
     * 获取权限树形结构
     */
    @GetMapping("/tree")
    @Operation(summary = "获取权限树形结构", description = "获取权限的树形结构列表")
    @SaCheckPermission("system:permission:list")
    public Result<List<PermissionTreeVO>> getPermissionTree(@Valid PermissionQueryDTO queryDTO) {
        List<PermissionTreeVO> permissionTree = permissionService.getPermissionTree(queryDTO);
        return Result.success(permissionTree);
    }

    /**
     * 获取权限详细信息
     */
    @GetMapping("/{permissionId}")
    @Operation(summary = "获取权限详细信息", description = "根据权限ID获取权限详细信息")
    @SaCheckPermission("system:permission:query")
    public Result<PermissionVO> getPermissionDetail(
            @Parameter(description = "权限ID") @PathVariable Long permissionId) {
        PermissionVO permissionDetail = permissionService.getPermissionDetail(permissionId);
        return Result.success(permissionDetail);
    }

    /**
     * 创建权限
     */
    @PostMapping
    @Operation(summary = "创建权限", description = "创建新的权限")
    @SaCheckPermission("system:permission:add")
    public Result<Long> createPermission(@Valid @RequestBody PermissionCreateDTO createDTO) {
        Long createBy = StpUtil.getLoginIdAsLong();
        Long permissionId = permissionService.createPermission(createDTO, createBy);
        return Result.success("权限创建成功", permissionId);
    }

    /**
     * 更新权限
     */
    @PutMapping
    @Operation(summary = "更新权限", description = "更新权限信息")
    @SaCheckPermission("system:permission:edit")
    public Result<Boolean> updatePermission(@Valid @RequestBody PermissionUpdateDTO updateDTO) {
        Long updateBy = StpUtil.getLoginIdAsLong();
        boolean success = permissionService.updatePermission(updateDTO, updateBy);
        return success ? Result.success("权限更新成功") : Result.error("权限更新失败");
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{permissionId}")
    @Operation(summary = "删除权限", description = "根据权限ID删除权限")
    @SaCheckPermission("system:permission:remove")
    public Result<Boolean> deletePermission(
            @Parameter(description = "权限ID") @PathVariable Long permissionId) {
        Long deleteBy = StpUtil.getLoginIdAsLong();
        boolean success = permissionService.deletePermission(permissionId, deleteBy);
        return success ? Result.success("权限删除成功") : Result.error("权限删除失败");
    }

    /**
     * 批量删除权限
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除权限", description = "根据权限ID列表批量删除权限")
    @SaCheckPermission("system:permission:remove")
    public Result<Boolean> deletePermissionsBatch(@RequestBody List<Long> permissionIds) {
        Long deleteBy = StpUtil.getLoginIdAsLong();
        boolean success = permissionService.deletePermissionsBatch(permissionIds, deleteBy);
        return success ? Result.success("权限批量删除成功") : Result.error("权限批量删除失败");
    }

    /**
     * 更新权限状态
     */
    @PutMapping("/{permissionId}/status/{status}")
    @Operation(summary = "更新权限状态", description = "更新权限的启用/禁用状态")
    @SaCheckPermission("system:permission:edit")
    public Result<Boolean> updatePermissionStatus(
            @Parameter(description = "权限ID") @PathVariable Long permissionId,
            @Parameter(description = "状态") @PathVariable Integer status) {
        Long updateBy = StpUtil.getLoginIdAsLong();
        boolean success = permissionService.updatePermissionStatus(permissionId, status, updateBy);
        return success ? Result.success("权限状态更新成功") : Result.error("权限状态更新失败");
    }

    /**
     * 批量更新权限状态
     */
    @PutMapping("/status/{status}")
    @Operation(summary = "批量更新权限状态", description = "批量更新权限的启用/禁用状态")
    @SaCheckPermission("system:permission:edit")
    public Result<Boolean> updatePermissionStatusBatch(
            @Parameter(description = "状态") @PathVariable Integer status,
            @RequestBody List<Long> permissionIds) {
        Long updateBy = StpUtil.getLoginIdAsLong();
        boolean success = permissionService.updatePermissionStatusBatch(permissionIds, status, updateBy);
        return success ? Result.success("权限状态批量更新成功") : Result.error("权限状态批量更新失败");
    }

    /**
     * 根据用户ID获取菜单权限树
     */
    @GetMapping("/menu/user/{userId}")
    @Operation(summary = "获取用户菜单权限", description = "根据用户ID获取菜单权限树")
    @SaCheckPermission("system:permission:query")
    public Result<List<PermissionTreeVO>> getMenuPermissionsByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<PermissionTreeVO> menuPermissions = permissionService.getMenuPermissionsByUserId(userId);
        return Result.success(menuPermissions);
    }

    /**
     * 根据用户ID获取按钮权限列表
     */
    @GetMapping("/button/user/{userId}")
    @Operation(summary = "获取用户按钮权限", description = "根据用户ID获取按钮权限代码列表")
    @SaCheckPermission("system:permission:query")
    public Result<List<String>> getButtonPermissionsByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<String> buttonPermissions = permissionService.getButtonPermissionsByUserId(userId);
        return Result.success(buttonPermissions);
    }

    /**
     * 根据角色ID获取权限列表
     */
    @GetMapping("/role/{roleId}")
    @Operation(summary = "获取角色权限列表", description = "根据角色ID获取权限列表")
    @SaCheckPermission("system:permission:query")
    public Result<List<PermissionVO>> getPermissionsByRoleId(
            @Parameter(description = "角色ID") @PathVariable Long roleId) {
        List<PermissionVO> permissions = permissionService.getPermissionsByRoleId(roleId);
        return Result.success(permissions);
    }

    /**
     * 根据权限类型获取权限列表
     */
    @GetMapping("/type/{permissionType}")
    @Operation(summary = "获取指定类型权限", description = "根据权限类型获取权限列表")
    @SaCheckPermission("system:permission:query")
    public Result<List<PermissionVO>> getPermissionsByType(
            @Parameter(description = "权限类型") @PathVariable Integer permissionType) {
        List<PermissionVO> permissions = permissionService.getPermissionsByType(permissionType);
        return Result.success(permissions);
    }

    /**
     * 检查权限编码是否存在
     */
    @GetMapping("/check/code")
    @Operation(summary = "检查权限编码", description = "检查权限编码是否已存在")
    @SaCheckPermission("system:permission:query")
    public Result<Boolean> checkPermissionCode(
            @Parameter(description = "权限编码") @RequestParam String permissionCode,
            @Parameter(description = "排除的权限ID") @RequestParam(required = false) Long excludeId) {
        boolean exists = permissionService.existsPermissionCode(permissionCode, excludeId);
        return Result.success(!exists);
    }

    /**
     * 验证用户权限
     */
    @GetMapping("/check/user/{userId}")
    @Operation(summary = "验证用户权限", description = "验证用户是否具有指定权限")
    @SaCheckPermission("system:permission:query")
    public Result<Boolean> checkUserPermission(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "权限编码") @RequestParam String permissionCode) {
        boolean hasPermission = permissionService.hasPermission(userId, permissionCode);
        return Result.success(hasPermission);
    }

    /**
     * 获取当前用户的菜单权限
     */
    @GetMapping("/menu/current")
    @Operation(summary = "获取当前用户菜单权限", description = "获取当前登录用户的菜单权限树")
    public Result<List<PermissionTreeVO>> getCurrentUserMenuPermissions() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<PermissionTreeVO> menuPermissions = permissionService.getMenuPermissionsByUserId(userId);
        return Result.success(menuPermissions);
    }

    /**
     * 获取当前用户的按钮权限
     */
    @GetMapping("/button/current")
    @Operation(summary = "获取当前用户按钮权限", description = "获取当前登录用户的按钮权限代码列表")
    public Result<List<String>> getCurrentUserButtonPermissions() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<String> buttonPermissions = permissionService.getButtonPermissionsByUserId(userId);
        return Result.success(buttonPermissions);
    }
} 