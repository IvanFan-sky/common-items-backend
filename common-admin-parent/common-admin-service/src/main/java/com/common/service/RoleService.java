package com.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.core.dto.role.RoleCreateDTO;
import com.common.core.dto.role.RoleQueryDTO;
import com.common.core.dto.role.RoleUpdateDTO;
import com.common.core.dto.user.UserRoleAssignDTO;
import com.common.core.entity.SysRole;
import com.common.core.vo.PageResult;
import com.common.core.vo.role.RoleDetailVO;
import com.common.core.vo.role.RoleVO;

import java.util.List;

/**
 * @Description 角色服务接口，处理角色相关的业务逻辑
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
public interface RoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     *
     * @param queryDTO 查询条件
     * @return 角色分页结果
     */
    PageResult<RoleVO> getRolePage(RoleQueryDTO queryDTO);

    /**
     * 获取角色详细信息
     *
     * @param roleId 角色ID
     * @return 角色详细信息
     */
    RoleDetailVO getRoleDetail(Long roleId);

    /**
     * 创建角色
     *
     * @param createDTO 创建角色信息
     * @param createBy 创建者ID
     * @return 角色ID
     */
    Long createRole(RoleCreateDTO createDTO, Long createBy);

    /**
     * 更新角色
     *
     * @param updateDTO 更新角色信息
     * @param updateBy 更新者ID
     * @return 是否成功
     */
    boolean updateRole(RoleUpdateDTO updateDTO, Long updateBy);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @param deleteBy 删除者ID
     * @return 是否成功
     */
    boolean deleteRole(Long roleId, Long deleteBy);

    /**
     * 批量删除角色
     *
     * @param roleIds 角色ID列表
     * @param deleteBy 删除者ID
     * @return 是否成功
     */
    boolean deleteRolesBatch(List<Long> roleIds, Long deleteBy);

    /**
     * 更新角色状态
     *
     * @param roleId 角色ID
     * @param status 状态
     * @param updateBy 更新者ID
     * @return 是否成功
     */
    boolean updateRoleStatus(Long roleId, Integer status, Long updateBy);

    /**
     * 批量更新角色状态
     *
     * @param roleIds 角色ID列表
     * @param status 状态
     * @param updateBy 更新者ID
     * @return 是否成功
     */
    boolean updateRoleStatusBatch(List<Long> roleIds, Integer status, Long updateBy);

    /**
     * 分配角色权限
     *
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @param updateBy 更新者ID
     * @return 是否成功
     */
    boolean assignRolePermissions(Long roleId, List<Long> permissionIds, Long updateBy);

    /**
     * 为用户分配角色
     *
     * @param assignDTO 用户角色分配信息
     * @param assignBy 分配者ID
     * @return 是否成功
     */
    boolean assignUserRoles(UserRoleAssignDTO assignDTO, Long assignBy);

    /**
     * 根据用户ID获取角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleVO> getRolesByUserId(Long userId);

    /**
     * 根据角色编码获取角色信息
     *
     * @param roleCode 角色编码
     * @return 角色信息
     */
    RoleVO getRoleByCode(String roleCode);

    /**
     * 检查角色编码是否存在
     *
     * @param roleCode 角色编码
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean existsRoleCode(String roleCode, Long excludeId);

    /**
     * 检查角色名称是否存在
     *
     * @param roleName 角色名称
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean existsRoleName(String roleName, Long excludeId);

    /**
     * 获取所有启用的角色列表
     *
     * @return 角色列表
     */
    List<RoleVO> getEnabledRoles();
} 