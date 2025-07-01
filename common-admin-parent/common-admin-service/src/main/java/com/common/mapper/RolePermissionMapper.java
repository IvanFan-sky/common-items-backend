package com.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.common.core.entity.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 角色权限关联Mapper接口
 * @Date 2025/1/8 9:30
 * @Author SparkFan
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<SysRolePermission> {

    /**
     * 根据角色ID删除角色权限关联
     *
     * @param roleId 角色ID
     * @return 删除记录数
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限ID删除角色权限关联
     *
     * @param permissionId 权限ID
     * @return 删除记录数
     */
    int deleteByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 批量插入角色权限关联
     *
     * @param rolePermissions 角色权限关联列表
     * @return 插入记录数
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
     * 根据角色ID列表查询权限ID列表
     *
     * @param roleIds 角色ID列表
     * @return 权限ID列表
     */
    List<Long> selectPermissionIdsByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 检查角色是否拥有指定权限
     *
     * @param roleId       角色ID
     * @param permissionId 权限ID
     * @return 是否存在关联
     */
    boolean existsRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
} 