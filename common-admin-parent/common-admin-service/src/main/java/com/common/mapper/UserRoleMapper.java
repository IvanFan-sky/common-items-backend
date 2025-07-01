package com.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.common.core.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 用户角色关联Mapper接口
 * @Date 2025/1/8 9:30
 * @Author SparkFan
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 根据用户ID删除用户角色关联
     *
     * @param userId 用户ID
     * @return 删除记录数
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID删除用户角色关联
     *
     * @param roleId 角色ID
     * @return 删除记录数
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量插入用户角色关联
     *
     * @param userRoles 用户角色关联列表
     * @return 插入记录数
     */
    int insertBatch(@Param("userRoles") List<SysUserRole> userRoles);

    /**
     * 根据用户ID查询角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询用户ID列表
     *
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    List<Long> selectUserIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 检查用户是否拥有指定角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否存在关联
     */
    boolean existsUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
} 