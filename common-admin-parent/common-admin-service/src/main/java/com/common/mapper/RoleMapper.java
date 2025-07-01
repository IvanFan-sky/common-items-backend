package com.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.core.dto.role.RoleQueryDTO;
import com.common.core.entity.SysRole;
import com.common.core.vo.role.RoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 角色Mapper接口
 * @Date 2025/1/8 9:30
 * @Author SparkFan
 */
@Mapper
public interface RoleMapper extends BaseMapper<SysRole> {

    /**
     * 分页查询角色列表
     *
     * @param page     分页对象
     * @param queryDTO 查询条件
     * @return 角色VO分页结果
     */
    IPage<RoleVO> selectRolePage(Page<RoleVO> page, @Param("query") RoleQueryDTO queryDTO);

    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色实体
     */
    SysRole selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据状态查询角色列表
     *
     * @param status 状态
     * @return 角色列表
     */
    List<SysRole> selectRolesByStatus(@Param("status") Integer status);

    /**
     * 检查角色编码是否存在
     *
     * @param roleCode  角色编码
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean existsRoleCode(@Param("roleCode") String roleCode, @Param("excludeId") Long excludeId);

    /**
     * 检查角色名称是否存在
     *
     * @param roleName  角色名称
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean existsRoleName(@Param("roleName") String roleName, @Param("excludeId") Long excludeId);

    /**
     * 根据角色ID查询关联的用户数量
     *
     * @param roleId 角色ID
     * @return 用户数量
     */
    Integer selectUserCountByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量更新角色状态
     *
     * @param roleIds  角色ID列表
     * @param status   状态
     * @param updateBy 更新人
     * @return 更新记录数
     */
    int updateStatusBatch(@Param("roleIds") List<Long> roleIds, 
                         @Param("status") Integer status, 
                         @Param("updateBy") Long updateBy);
} 