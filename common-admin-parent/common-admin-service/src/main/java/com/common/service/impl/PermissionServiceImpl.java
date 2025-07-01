package com.common.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.core.dto.PermissionCreateDTO;
import com.common.core.dto.PermissionQueryDTO;
import com.common.core.dto.PermissionUpdateDTO;
import com.common.core.entity.SysPermission;
import com.common.core.mapper.PermissionConvertMapper;
import com.common.core.vo.PageResult;
import com.common.core.vo.PermissionTreeVO;
import com.common.core.vo.PermissionVO;
import com.common.mapper.PermissionMapper;
import com.common.mapper.RolePermissionMapper;
import com.common.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description 权限服务实现类，实现权限相关的业务逻辑
 * @Date 2025/1/8 10:00
 * @Author SparkFan
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, SysPermission> implements PermissionService {

    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionConvertMapper convertMapper;

    @Override
    public PageResult<PermissionVO> getPermissionPage(PermissionQueryDTO queryDTO) {
        Page<PermissionVO> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        IPage<PermissionVO> pageResult = permissionMapper.selectPermissionPage(page, queryDTO);
        
        return new PageResult<>(pageResult.getCurrent(), pageResult.getSize(), 
                               pageResult.getTotal(), pageResult.getRecords());
    }

    @Override
    public List<PermissionTreeVO> getPermissionTree(PermissionQueryDTO queryDTO) {
        List<SysPermission> permissions = permissionMapper.selectAllPermissions();
        List<PermissionTreeVO> permissionTreeVOList = convertMapper.toPermissionTreeVOList(permissions);
        return buildPermissionTree(permissionTreeVOList, 0L);
    }

    @Override
    public PermissionVO getPermissionDetail(Long permissionId) {
        SysPermission permission = getById(permissionId);
        if (permission == null) {
            throw new RuntimeException("权限不存在");
        }
        
        return convertMapper.toPermissionVO(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPermission(PermissionCreateDTO createDTO, Long createBy) {
        // 检查权限编码是否存在
        if (existsPermissionCode(createDTO.getPermissionCode(), null)) {
            throw new RuntimeException("权限编码已存在");
        }
        
        // 验证父权限存在性
        if (createDTO.getParentId() != 0) {
            SysPermission parentPermission = getById(createDTO.getParentId());
            if (parentPermission == null) {
                throw new RuntimeException("父权限不存在");
            }
        }
        
        SysPermission permission = convertMapper.createDTOToEntity(createDTO);
        permission.setCreateBy(createBy);
        permission.setCreateTime(LocalDateTime.now());
        permission.setStatus(1); // 默认启用
        permission.setVisible(1); // 默认显示
        
        save(permission);
        
        log.info("创建权限成功，权限ID：{}，权限名称：{}", permission.getId(), permission.getPermissionName());
        return permission.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermission(PermissionUpdateDTO updateDTO, Long updateBy) {
        SysPermission existingPermission = getById(updateDTO.getId());
        if (existingPermission == null) {
            throw new RuntimeException("权限不存在");
        }
        
        // 检查权限编码是否存在
        if (existsPermissionCode(updateDTO.getPermissionCode(), updateDTO.getId())) {
            throw new RuntimeException("权限编码已存在");
        }
        
        // 验证父权限存在性（不能设置自己为父权限）
        if (updateDTO.getParentId() != 0) {
            if (updateDTO.getParentId().equals(updateDTO.getId())) {
                throw new RuntimeException("不能设置自己为父权限");
            }
            SysPermission parentPermission = getById(updateDTO.getParentId());
            if (parentPermission == null) {
                throw new RuntimeException("父权限不存在");
            }
        }
        
        // 使用MapStruct更新权限信息
        convertMapper.updateEntityFromDTO(updateDTO, existingPermission);
        existingPermission.setUpdateBy(updateBy);
        existingPermission.setUpdateTime(LocalDateTime.now());
        
        updateById(existingPermission);
        
        log.info("更新权限成功，权限ID：{}，权限名称：{}", existingPermission.getId(), existingPermission.getPermissionName());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermission(Long permissionId, Long deleteBy) {
        SysPermission permission = getById(permissionId);
        if (permission == null) {
            throw new RuntimeException("权限不存在");
        }
        
        // 检查是否有子权限
        List<SysPermission> children = permissionMapper.selectPermissionsByParentId(permissionId);
        if (!CollectionUtils.isEmpty(children)) {
            throw new RuntimeException("该权限下还有子权限，无法删除");
        }
        
        // 删除角色权限关联
        rolePermissionMapper.deleteByPermissionId(permissionId);
        
        // 删除权限
        removeById(permissionId);
        
        log.info("删除权限成功，权限ID：{}，权限名称：{}", permissionId, permission.getPermissionName());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermissionsBatch(List<Long> permissionIds, Long deleteBy) {
        if (CollectionUtils.isEmpty(permissionIds)) {
            return false;
        }
        
        for (Long permissionId : permissionIds) {
            deletePermission(permissionId, deleteBy);
        }
        
        return true;
    }

    @Override
    public boolean updatePermissionStatus(Long permissionId, Integer status, Long updateBy) {
        SysPermission permission = getById(permissionId);
        if (permission == null) {
            throw new RuntimeException("权限不存在");
        }
        
        permission.setStatus(status);
        permission.setUpdateBy(updateBy);
        permission.setUpdateTime(LocalDateTime.now());
        
        updateById(permission);
        
        log.info("更新权限状态成功，权限ID：{}，状态：{}", permissionId, status);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermissionStatusBatch(List<Long> permissionIds, Integer status, Long updateBy) {
        if (CollectionUtils.isEmpty(permissionIds)) {
            return false;
        }
        
        int updateCount = permissionMapper.updateStatusBatch(permissionIds, status, updateBy);
        
        log.info("批量更新权限状态成功，更新数量：{}，状态：{}", updateCount, status);
        return updateCount > 0;
    }

    @Override
    public List<PermissionTreeVO> getMenuPermissionsByUserId(Long userId) {
        List<SysPermission> menuPermissions = permissionMapper.selectMenuPermissionsByUserId(userId);
        List<PermissionTreeVO> menuTreeVOList = convertMapper.toPermissionTreeVOList(menuPermissions);
        return buildPermissionTree(menuTreeVOList, 0L);
    }

    @Override
    public List<String> getButtonPermissionsByUserId(Long userId) {
        List<SysPermission> buttonPermissions = permissionMapper.selectButtonPermissionsByUserId(userId);
        return buttonPermissions.stream()
                .map(SysPermission::getPermissionCode)
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionVO> getPermissionsByRoleId(Long roleId) {
        List<SysPermission> permissions = permissionMapper.selectPermissionsByRoleId(roleId);
        return convertMapper.toPermissionVOList(permissions);
    }

    @Override
    public PermissionVO getPermissionByCode(String permissionCode) {
        SysPermission permission = permissionMapper.selectByPermissionCode(permissionCode);
        if (permission == null) {
            return null;
        }
        return convertMapper.toPermissionVO(permission);
    }

    @Override
    public boolean existsPermissionCode(String permissionCode, Long excludeId) {
        return permissionMapper.existsPermissionCode(permissionCode, excludeId);
    }

    @Override
    public List<PermissionVO> getPermissionsByType(Integer permissionType) {
        List<SysPermission> permissions = permissionMapper.selectPermissionsByType(permissionType, 1);
        return convertMapper.toPermissionVOList(permissions);
    }

    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        List<SysPermission> userPermissions = permissionMapper.selectPermissionsByUserId(userId);
        return userPermissions.stream()
                .anyMatch(permission -> permission.getPermissionCode().equals(permissionCode));
    }

    @Override
    public boolean hasAnyPermission(Long userId, List<String> permissionCodes) {
        if (CollectionUtils.isEmpty(permissionCodes)) {
            return true;
        }
        
        List<SysPermission> userPermissions = permissionMapper.selectPermissionsByUserId(userId);
        List<String> userPermissionCodes = userPermissions.stream()
                .map(SysPermission::getPermissionCode)
                .collect(Collectors.toList());
        
        return permissionCodes.stream()
                .anyMatch(userPermissionCodes::contains);
    }

    @Override
    public boolean hasAllPermissions(Long userId, List<String> permissionCodes) {
        if (CollectionUtils.isEmpty(permissionCodes)) {
            return true;
        }
        
        List<SysPermission> userPermissions = permissionMapper.selectPermissionsByUserId(userId);
        List<String> userPermissionCodes = userPermissions.stream()
                .map(SysPermission::getPermissionCode)
                .collect(Collectors.toList());
        
        return userPermissionCodes.containsAll(permissionCodes);
    }

    /**
     * 构建权限树形结构
     *
     * @param permissionList 权限列表
     * @param parentId       父权限ID
     * @return 权限树形结构
     */
    private List<PermissionTreeVO> buildPermissionTree(List<PermissionTreeVO> permissionList, Long parentId) {
        Map<Long, List<PermissionTreeVO>> permissionMap = permissionList.stream()
                .collect(Collectors.groupingBy(PermissionTreeVO::getParentId));
        
        return buildChildren(parentId, permissionMap);
    }

    /**
     * 递归构建子节点
     *
     * @param parentId      父权限ID
     * @param permissionMap 权限分组Map
     * @return 子节点列表
     */
    private List<PermissionTreeVO> buildChildren(Long parentId, Map<Long, List<PermissionTreeVO>> permissionMap) {
        List<PermissionTreeVO> children = permissionMap.get(parentId);
        if (CollectionUtils.isEmpty(children)) {
            return new ArrayList<>();
        }
        
        children.forEach(child -> {
            List<PermissionTreeVO> subChildren = buildChildren(child.getId(), permissionMap);
            child.setChildren(subChildren);
            child.setHasChildren(!CollectionUtils.isEmpty(subChildren));
        });
        
        return children;
    }
} 