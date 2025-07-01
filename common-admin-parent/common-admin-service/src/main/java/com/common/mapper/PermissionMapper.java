package com.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.core.dto.PermissionQueryDTO;
import com.common.core.entity.SysPermission;
import com.common.core.vo.PermissionTreeVO;
import com.common.core.vo.PermissionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 权限数据访问层，处理权限相关的数据库操作
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Mapper
public interface PermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 分页查询权限列表
     *
     * @param page 分页对象
     * @param queryDTO 查询条件
     * @return 权限分页数据
     */
    IPage<PermissionVO> selectPermissionPage(Page<PermissionVO> page, @Param("query") PermissionQueryDTO queryDTO);

    /**
     * 查询权限树形结构
     *
     * @param queryDTO 查询条件
     * @return 权限树形结构列表
     */
    List<PermissionTreeVO> selectPermissionTree(@Param("query") PermissionQueryDTO queryDTO);

    /**
     * 根据权限编码查询权限
     *
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    SysPermission selectByPermissionCode(@Param("permissionCode") String permissionCode);

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<SysPermission> selectPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<SysPermission> selectPermissionsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询菜单权限列表
     *
     * @param userId 用户ID
     * @return 菜单权限列表
     */
    List<SysPermission> selectMenuPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询按钮权限代码列表
     *
     * @param userId 用户ID
     * @return 按钮权限代码列表
     */
    List<SysPermission> selectButtonPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 检查权限编码是否存在（排除指定ID）
     *
     * @param permissionCode 权限编码
     * @param excludeId 排除的权限ID
     * @return 是否存在
     */
    boolean existsPermissionCode(@Param("permissionCode") String permissionCode, @Param("excludeId") Long excludeId);

    /**
     * 查询子权限数量
     *
     * @param parentId 父权限ID
     * @return 子权限数量
     */
    Integer selectChildrenCount(@Param("parentId") Long parentId);

    /**
     * 根据父权限ID查询子权限列表
     *
     * @param parentId 父权限ID
     * @return 子权限列表
     */
    List<SysPermission> selectPermissionsByParentId(@Param("parentId") Long parentId);

    /**
     * 根据权限类型查询权限列表
     *
     * @param permissionType 权限类型
     * @param status 状态
     * @return 权限列表
     */
    List<SysPermission> selectPermissionsByType(@Param("permissionType") Integer permissionType, 
                                               @Param("status") Integer status);

    /**
     * 批量更新权限状态
     *
     * @param permissionIds 权限ID列表
     * @param status 状态
     * @param updateBy 更新者ID
     * @return 更新数量
     */
    int updateStatusBatch(@Param("permissionIds") List<Long> permissionIds, 
                         @Param("status") Integer status, 
                         @Param("updateBy") Long updateBy);

    /**
     * 查询所有权限（用于构建权限树）
     *
     * @return 权限列表
     */
    List<SysPermission> selectAllPermissions();
} 