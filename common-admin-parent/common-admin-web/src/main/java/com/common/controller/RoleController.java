package com.common.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.common.core.dto.role.RoleCreateDTO;
import com.common.core.dto.role.RoleQueryDTO;
import com.common.core.dto.role.RoleUpdateDTO;
import com.common.core.dto.user.UserRoleAssignDTO;
import com.common.core.vo.PageResult;
import com.common.core.vo.Result;
import com.common.core.vo.role.RoleDetailVO;
import com.common.core.vo.role.RoleVO;
import com.common.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * @Description 角色管理控制器，处理角色相关的HTTP请求
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Tag(name = "角色管理", description = "角色管理相关接口")
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 分页查询角色列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询角色列表", description = "分页查询角色列表，支持多条件筛选")
    @SaCheckPermission("system:role:list")
    public Result<PageResult<RoleVO>> getRolePage(@Valid RoleQueryDTO queryDTO) {
        PageResult<RoleVO> pageResult = roleService.getRolePage(queryDTO);
        return Result.success(pageResult);
    }

    /**
     * 获取角色详细信息
     */
    @GetMapping("/{roleId}")
    @Operation(summary = "获取角色详细信息", description = "根据角色ID获取角色详细信息")
    @SaCheckPermission("system:role:query")
    public Result<RoleDetailVO> getRoleDetail(
            @Parameter(description = "角色ID") @PathVariable Long roleId) {
        RoleDetailVO roleDetail = roleService.getRoleDetail(roleId);
        return Result.success(roleDetail);
    }

    /**
     * 创建角色
     */
    @PostMapping
    @Operation(summary = "创建角色", description = "创建新的角色")
    @SaCheckPermission("system:role:add")
    public Result<Long> createRole(@Valid @RequestBody RoleCreateDTO createDTO) {
        Long createBy = StpUtil.getLoginIdAsLong();
        Long roleId = roleService.createRole(createDTO, createBy);
        return Result.success("角色创建成功", roleId);
    }

    /**
     * 更新角色
     */
    @PutMapping
    @Operation(summary = "更新角色", description = "更新角色信息")
    @SaCheckPermission("system:role:edit")
    public Result<Boolean> updateRole(@Valid @RequestBody RoleUpdateDTO updateDTO) {
        Long updateBy = StpUtil.getLoginIdAsLong();
        boolean success = roleService.updateRole(updateDTO, updateBy);
        return success ? Result.success("角色更新成功") : Result.error("角色更新失败");
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{roleId}")
    @Operation(summary = "删除角色", description = "根据角色ID删除角色")
    @SaCheckPermission("system:role:remove")
    public Result<Boolean> deleteRole(
            @Parameter(description = "角色ID") @PathVariable Long roleId) {
        Long deleteBy = StpUtil.getLoginIdAsLong();
        boolean success = roleService.deleteRole(roleId, deleteBy);
        return success ? Result.success("角色删除成功") : Result.error("角色删除失败");
    }

    /**
     * 批量删除角色
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除角色", description = "根据角色ID列表批量删除角色")
    @SaCheckPermission("system:role:remove")
    public Result<Boolean> deleteRolesBatch(@RequestBody List<Long> roleIds) {
        Long deleteBy = StpUtil.getLoginIdAsLong();
        boolean success = roleService.deleteRolesBatch(roleIds, deleteBy);
        return success ? Result.success("角色批量删除成功") : Result.error("角色批量删除失败");
    }

    /**
     * 更新角色状态
     */
    @PutMapping("/{roleId}/status/{status}")
    @Operation(summary = "更新角色状态", description = "更新角色的启用/禁用状态")
    @SaCheckPermission("system:role:edit")
    public Result<Boolean> updateRoleStatus(
            @Parameter(description = "角色ID") @PathVariable Long roleId,
            @Parameter(description = "状态") @PathVariable Integer status) {
        Long updateBy = StpUtil.getLoginIdAsLong();
        boolean success = roleService.updateRoleStatus(roleId, status, updateBy);
        return success ? Result.success("角色状态更新成功") : Result.error("角色状态更新失败");
    }

    /**
     * 批量更新角色状态
     */
    @PutMapping("/status/{status}")
    @Operation(summary = "批量更新角色状态", description = "批量更新角色的启用/禁用状态")
    @SaCheckPermission("system:role:edit")
    public Result<Boolean> updateRoleStatusBatch(
            @Parameter(description = "状态") @PathVariable Integer status,
            @RequestBody List<Long> roleIds) {
        Long updateBy = StpUtil.getLoginIdAsLong();
        boolean success = roleService.updateRoleStatusBatch(roleIds, status, updateBy);
        return success ? Result.success("角色状态批量更新成功") : Result.error("角色状态批量更新失败");
    }

    /**
     * 分配角色权限
     */
    @PutMapping("/{roleId}/permissions")
    @Operation(summary = "分配角色权限", description = "为角色分配权限")
    @SaCheckPermission("system:role:auth")
    public Result<Boolean> assignRolePermissions(
            @Parameter(description = "角色ID") @PathVariable Long roleId,
            @RequestBody List<Long> permissionIds) {
        Long updateBy = StpUtil.getLoginIdAsLong();
        boolean success = roleService.assignRolePermissions(roleId, permissionIds, updateBy);
        return success ? Result.success("角色权限分配成功") : Result.error("角色权限分配失败");
    }

    /**
     * 为用户分配角色
     */
    @PutMapping("/assign")
    @Operation(summary = "为用户分配角色", description = "为用户分配角色")
    @SaCheckPermission("system:user:role")
    public Result<Boolean> assignUserRoles(@Valid @RequestBody UserRoleAssignDTO assignDTO) {
        Long assignBy = StpUtil.getLoginIdAsLong();
        boolean success = roleService.assignUserRoles(assignDTO, assignBy);
        return success ? Result.success("用户角色分配成功") : Result.error("用户角色分配失败");
    }

    /**
     * 根据用户ID获取角色列表
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户角色列表", description = "根据用户ID获取角色列表")
    @SaCheckPermission("system:role:query")
    public Result<List<RoleVO>> getRolesByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<RoleVO> roles = roleService.getRolesByUserId(userId);
        return Result.success(roles);
    }

    /**
     * 获取所有启用的角色列表
     */
    @GetMapping("/enabled")
    @Operation(summary = "获取启用角色列表", description = "获取所有启用状态的角色列表")
    @SaCheckPermission("system:role:query")
    public Result<List<RoleVO>> getEnabledRoles() {
        List<RoleVO> roles = roleService.getEnabledRoles();
        return Result.success(roles);
    }

    /**
     * 检查角色编码是否存在
     */
    @GetMapping("/check/code")
    @Operation(summary = "检查角色编码", description = "检查角色编码是否已存在")
    @SaCheckPermission("system:role:query")
    public Result<Boolean> checkRoleCode(
            @Parameter(description = "角色编码") @RequestParam String roleCode,
            @Parameter(description = "排除的角色ID") @RequestParam(required = false) Long excludeId) {
        boolean exists = roleService.existsRoleCode(roleCode, excludeId);
        return Result.success(!exists);
    }

    /**
     * 检查角色名称是否存在
     */
    @GetMapping("/check/name")
    @Operation(summary = "检查角色名称", description = "检查角色名称是否已存在")
    @SaCheckPermission("system:role:query")
    public Result<Boolean> checkRoleName(
            @Parameter(description = "角色名称") @RequestParam String roleName,
            @Parameter(description = "排除的角色ID") @RequestParam(required = false) Long excludeId) {
        boolean exists = roleService.existsRoleName(roleName, excludeId);
        return Result.success(!exists);
    }
} 