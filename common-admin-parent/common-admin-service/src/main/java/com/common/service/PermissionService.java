package com.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.core.dto.PermissionCreateDTO;
import com.common.core.dto.PermissionQueryDTO;
import com.common.core.dto.PermissionUpdateDTO;
import com.common.core.entity.SysPermission;
import com.common.core.vo.PageResult;
import com.common.core.vo.PermissionTreeVO;
import com.common.core.vo.PermissionVO;

import java.util.List;

/**
 * @Description 权限服务接口，处理权限相关的业务逻辑
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
public interface PermissionService extends IService<SysPermission> {

    /**
     * 分页查询权限列表
     *
     * @param queryDTO 查询条件
     * @return 权限分页结果
     */
    PageResult<PermissionVO> getPermissionPage(PermissionQueryDTO queryDTO);

    /**
     * 获取权限树形结构
     *
     * @param queryDTO 查询条件
     * @return 权限树形结构
     */
    List<PermissionTreeVO> getPermissionTree(PermissionQueryDTO queryDTO);

    /**
     * 获取权限详细信息
     *
     * @param permissionId 权限ID
     * @return 权限详细信息
     */
    PermissionVO getPermissionDetail(Long permissionId);

    /**
     * 创建权限
     *
     * @param createDTO 创建权限信息
     * @param createBy 创建者ID
     * @return 权限ID
     */
    Long createPermission(PermissionCreateDTO createDTO, Long createBy);

    /**
     * 更新权限
     *
     * @param updateDTO 更新权限信息
     * @param updateBy 更新者ID
     * @return 是否成功
     */
    boolean updatePermission(PermissionUpdateDTO updateDTO, Long updateBy);

    /**
     * 删除权限
     *
     * @param permissionId 权限ID
     * @param deleteBy 删除者ID
     * @return 是否成功
     */
    boolean deletePermission(Long permissionId, Long deleteBy);

    /**
     * 批量删除权限
     *
     * @param permissionIds 权限ID列表
     * @param deleteBy 删除者ID
     * @return 是否成功
     */
    boolean deletePermissionsBatch(List<Long> permissionIds, Long deleteBy);

    /**
     * 更新权限状态
     *
     * @param permissionId 权限ID
     * @param status 状态
     * @param updateBy 更新者ID
     * @return 是否成功
     */
    boolean updatePermissionStatus(Long permissionId, Integer status, Long updateBy);

    /**
     * 批量更新权限状态
     *
     * @param permissionIds 权限ID列表
     * @param status 状态
     * @param updateBy 更新者ID
     * @return 是否成功
     */
    boolean updatePermissionStatusBatch(List<Long> permissionIds, Integer status, Long updateBy);

    /**
     * 根据用户ID获取菜单权限树
     *
     * @param userId 用户ID
     * @return 菜单权限树
     */
    List<PermissionTreeVO> getMenuPermissionsByUserId(Long userId);

    /**
     * 根据用户ID获取按钮权限列表
     *
     * @param userId 用户ID
     * @return 按钮权限代码列表
     */
    List<String> getButtonPermissionsByUserId(Long userId);

    /**
     * 根据角色ID获取权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<PermissionVO> getPermissionsByRoleId(Long roleId);

    /**
     * 根据权限编码获取权限信息
     *
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    PermissionVO getPermissionByCode(String permissionCode);

    /**
     * 检查权限编码是否存在
     *
     * @param permissionCode 权限编码
     * @param excludeId 排除的权限ID
     * @return 是否存在
     */
    boolean existsPermissionCode(String permissionCode, Long excludeId);

    /**
     * 根据权限类型获取权限列表
     *
     * @param permissionType 权限类型
     * @return 权限列表
     */
    List<PermissionVO> getPermissionsByType(Integer permissionType);

    /**
     * 验证用户是否有指定权限
     *
     * @param userId 用户ID
     * @param permissionCode 权限编码
     * @return 是否有权限
     */
    boolean hasPermission(Long userId, String permissionCode);

    /**
     * 验证用户是否有指定权限列表中的任一权限
     *
     * @param userId 用户ID
     * @param permissionCodes 权限编码列表
     * @return 是否有权限
     */
    boolean hasAnyPermission(Long userId, List<String> permissionCodes);

    /**
     * 验证用户是否有指定权限列表中的全部权限
     *
     * @param userId 用户ID
     * @param permissionCodes 权限编码列表
     * @return 是否有权限
     */
    boolean hasAllPermissions(Long userId, List<String> permissionCodes);
} 