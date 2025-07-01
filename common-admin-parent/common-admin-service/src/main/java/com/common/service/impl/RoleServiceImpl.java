package com.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.core.dto.RoleCreateDTO;
import com.common.core.dto.RoleQueryDTO;
import com.common.core.dto.RoleUpdateDTO;
import com.common.core.dto.UserRoleAssignDTO;
import com.common.core.entity.SysRole;
import com.common.core.entity.SysRolePermission;
import com.common.core.entity.SysUserRole;
import com.common.core.mapper.RoleConvertMapper;
import com.common.core.vo.PageResult;
import com.common.core.vo.RoleDetailVO;
import com.common.core.vo.RoleVO;
import com.common.mapper.RoleMapper;
import com.common.mapper.RolePermissionMapper;
import com.common.mapper.UserRoleMapper;
import com.common.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 角色服务实现类，实现角色相关的业务逻辑
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, SysRole> implements RoleService {

    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final RoleConvertMapper convertMapper;

    @Override
    public PageResult<RoleVO> getRolePage(RoleQueryDTO queryDTO) {
        Page<RoleVO> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        IPage<RoleVO> pageResult = roleMapper.selectRolePage(page, queryDTO);
        
        return new PageResult<>(pageResult.getCurrent(), pageResult.getSize(), 
                               pageResult.getTotal(), pageResult.getRecords());
    }

    @Override
    public RoleDetailVO getRoleDetail(Long roleId) {
        SysRole role = getById(roleId);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        
        RoleDetailVO detailVO = convertMapper.toRoleDetailVO(role);
        
        // 填充权限信息
        List<Long> permissionIds = rolePermissionMapper.selectPermissionIdsByRoleId(roleId);
        detailVO.setPermissionIds(permissionIds);
        
        // 填充用户数量
        Integer userCount = roleMapper.selectUserCountByRoleId(roleId);
        detailVO.setUserCount(userCount);
        
        return detailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(RoleCreateDTO createDTO, Long createBy) {
        // 检查角色编码是否存在
        if (existsRoleCode(createDTO.getRoleCode(), null)) {
            throw new RuntimeException("角色编码已存在");
        }
        
        // 检查角色名称是否存在
        if (existsRoleName(createDTO.getRoleName(), null)) {
            throw new RuntimeException("角色名称已存在");
        }
        
        SysRole role = convertMapper.createDTOToEntity(createDTO);
        role.setCreateBy(createBy);
        role.setCreateTime(LocalDateTime.now());
        role.setStatus(1); // 默认启用
        
        save(role);
        
        // 分配权限
        if (!CollectionUtils.isEmpty(createDTO.getPermissionIds())) {
            assignRolePermissions(role.getId(), createDTO.getPermissionIds(), createBy);
        }
        
        log.info("创建角色成功，角色ID：{}，角色名称：{}", role.getId(), role.getRoleName());
        return role.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(RoleUpdateDTO updateDTO, Long updateBy) {
        SysRole existingRole = getById(updateDTO.getId());
        if (existingRole == null) {
            throw new RuntimeException("角色不存在");
        }
        
        // 检查角色编码是否存在
        if (existsRoleCode(updateDTO.getRoleCode(), updateDTO.getId())) {
            throw new RuntimeException("角色编码已存在");
        }
        
        // 检查角色名称是否存在
        if (existsRoleName(updateDTO.getRoleName(), updateDTO.getId())) {
            throw new RuntimeException("角色名称已存在");
        }
        
        // 使用MapStruct更新角色信息
        convertMapper.updateEntityFromDTO(updateDTO, existingRole);
        existingRole.setUpdateBy(updateBy);
        existingRole.setUpdateTime(LocalDateTime.now());
        
        updateById(existingRole);
        
        // 更新权限分配
        if (updateDTO.getPermissionIds() != null) {
            assignRolePermissions(existingRole.getId(), updateDTO.getPermissionIds(), updateBy);
        }
        
        log.info("更新角色成功，角色ID：{}，角色名称：{}", existingRole.getId(), existingRole.getRoleName());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long roleId, Long deleteBy) {
        SysRole role = getById(roleId);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        
        // 检查是否有用户关联此角色
        Integer userCount = roleMapper.selectUserCountByRoleId(roleId);
        if (userCount > 0) {
            throw new RuntimeException("该角色下还有用户，无法删除");
        }
        
        // 删除角色权限关联
        rolePermissionMapper.deleteByRoleId(roleId);
        
        // 删除角色
        removeById(roleId);
        
        log.info("删除角色成功，角色ID：{}，角色名称：{}", roleId, role.getRoleName());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRolesBatch(List<Long> roleIds, Long deleteBy) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
        
        for (Long roleId : roleIds) {
            deleteRole(roleId, deleteBy);
        }
        
        return true;
    }

    @Override
    public boolean updateRoleStatus(Long roleId, Integer status, Long updateBy) {
        SysRole role = getById(roleId);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        
        role.setStatus(status);
        role.setUpdateBy(updateBy);
        role.setUpdateTime(LocalDateTime.now());
        
        updateById(role);
        
        log.info("更新角色状态成功，角色ID：{}，状态：{}", roleId, status);
        return true;
    }

    @Override
    public boolean updateRoleStatusBatch(List<Long> roleIds, Integer status, Long updateBy) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
        
        return roleMapper.updateStatusBatch(roleIds, status, updateBy) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRolePermissions(Long roleId, List<Long> permissionIds, Long updateBy) {
        // 删除原有权限关联
        rolePermissionMapper.deleteByRoleId(roleId);
        
        // 添加新的权限关联
        if (!CollectionUtils.isEmpty(permissionIds)) {
            List<SysRolePermission> rolePermissions = permissionIds.stream()
                    .map(permissionId -> {
                        SysRolePermission rolePermission = new SysRolePermission();
                        rolePermission.setRoleId(roleId);
                        rolePermission.setPermissionId(permissionId);
                        rolePermission.setCreateBy(updateBy);
                        rolePermission.setCreateTime(LocalDateTime.now());
                        return rolePermission;
                    })
                    .collect(Collectors.toList());
            
            rolePermissionMapper.insertBatch(rolePermissions);
        }
        
        log.info("分配角色权限成功，角色ID：{}，权限数量：{}", roleId, 
                 CollectionUtils.isEmpty(permissionIds) ? 0 : permissionIds.size());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignUserRoles(UserRoleAssignDTO assignDTO, Long assignBy) {
        Long userId = assignDTO.getUserId();
        List<Long> roleIds = assignDTO.getRoleIds();
        
        // 删除原有角色关联
        userRoleMapper.deleteByUserId(userId);
        
        // 添加新的角色关联
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<SysUserRole> userRoles = roleIds.stream()
                    .map(roleId -> {
                        SysUserRole userRole = new SysUserRole();
                        userRole.setUserId(userId);
                        userRole.setRoleId(roleId);
                        userRole.setCreateBy(assignBy);
                        userRole.setCreateTime(LocalDateTime.now());
                        return userRole;
                    })
                    .collect(Collectors.toList());
            
            userRoleMapper.insertBatch(userRoles);
        }
        
        log.info("分配用户角色成功，用户ID：{}，角色数量：{}", userId, 
                 CollectionUtils.isEmpty(roleIds) ? 0 : roleIds.size());
        return true;
    }

    @Override
    public List<RoleVO> getRolesByUserId(Long userId) {
        List<SysRole> roles = roleMapper.selectRolesByUserId(userId);
        return convertMapper.toRoleVOList(roles);
    }

    @Override
    public RoleVO getRoleByCode(String roleCode) {
        SysRole role = roleMapper.selectByRoleCode(roleCode);
        return role != null ? convertMapper.toRoleVO(role) : null;
    }

    @Override
    public boolean existsRoleCode(String roleCode, Long excludeId) {
        return roleMapper.existsRoleCode(roleCode, excludeId);
    }

    @Override
    public boolean existsRoleName(String roleName, Long excludeId) {
        return roleMapper.existsRoleName(roleName, excludeId);
    }

    @Override
    public List<RoleVO> getEnabledRoles() {
        List<SysRole> roles = roleMapper.selectRolesByStatus(1);
        return convertMapper.toRoleVOList(roles);
    }


} 