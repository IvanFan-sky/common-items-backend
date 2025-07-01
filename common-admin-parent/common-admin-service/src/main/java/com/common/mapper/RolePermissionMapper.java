package com.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.common.core.entity.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 角色权限关联数据访问层，处理角色权限关联的数据库操作
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<SysRolePermission> {

    /**
     * 根据角色ID删除角色权限关联
     *
     * @param roleId 角色ID
     * @return 删除数量
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限ID删除角色权限关联
     *
     * @param permissionId 权限ID
     * @return 删除数量
     */
    int deleteByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 批量插入角色权限关联
     *
     * @param rolePermissions 角色权限关联列表
     * @return 插入数量
     */
    int insertBatch(@Param("rolePermissions") List<SysRolePermission> rolePermissions);

    /**
     * 根据角色ID查询权限ID列表
     *
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> selectPermissionIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限ID查询角色ID列表
     *
     * @param permissionId 权限ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 检查角色权限关联是否存在
     *
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 是否存在
     */
    boolean existsRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 批量删除角色权限关联
     *
     * @param roleIds 角色ID列表
     * @return 删除数量
     */
    int deleteByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 批量删除权限角色关联
     *
     * @param permissionIds 权限ID列表
     * @return 删除数量
     */
    int deleteByPermissionIds(@Param("permissionIds") List<Long> permissionIds);
} 