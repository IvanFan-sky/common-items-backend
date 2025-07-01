package com.common.core.mapper;

import com.common.core.dto.PermissionCreateDTO;
import com.common.core.dto.PermissionQueryDTO;
import com.common.core.dto.PermissionUpdateDTO;
import com.common.core.entity.SysPermission;
import com.common.core.vo.PermissionTreeVO;
import com.common.core.vo.PermissionVO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Description 权限实体映射器，负责Entity、DTO、VO之间的转换
 * @Date 2025/1/8 9:30
 * @Author SparkFan
 */
@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionConvertMapper {

    PermissionConvertMapper INSTANCE = Mappers.getMapper(PermissionConvertMapper.class);

    // ==================== Entity -> VO ====================

    /**
     * 实体转基本VO
     */
    @Mapping(target = "permissionTypeText", expression = "java(getPermissionTypeName(sysPermission.getPermissionType()))")
    @Mapping(target = "statusText", expression = "java(getStatusName(sysPermission.getStatus()))")
    PermissionVO toPermissionVO(SysPermission sysPermission);

    /**
     * 实体列表转VO列表
     */
    List<PermissionVO> toPermissionVOList(List<SysPermission> sysPermissionList);

    /**
     * 实体转树形VO
     */
    @Mapping(target = "permissionTypeText", expression = "java(getPermissionTypeName(sysPermission.getPermissionType()))")
    @Mapping(target = "statusText", expression = "java(getStatusName(sysPermission.getStatus()))")
    @Mapping(target = "children", ignore = true)  // 需要递归构建
    PermissionTreeVO toPermissionTreeVO(SysPermission sysPermission);

    /**
     * 实体列表转树形VO列表
     */
    List<PermissionTreeVO> toPermissionTreeVOList(List<SysPermission> sysPermissionList);

    // ==================== DTO -> Entity ====================

    /**
     * 创建DTO转实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    SysPermission createDTOToEntity(PermissionCreateDTO createDTO);

    /**
     * 更新DTO转实体
     */
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    SysPermission updateDTOToEntity(PermissionUpdateDTO updateDTO);

    /**
     * 用更新DTO的值更新已有实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDTO(PermissionUpdateDTO updateDTO, @MappingTarget SysPermission sysPermission);

    // ==================== Entity -> DTO ====================

    /**
     * 实体转查询DTO（用于回显）
     */
    PermissionQueryDTO toPermissionQueryDTO(SysPermission sysPermission);

    /**
     * 实体转更新DTO（用于回显）
     */
    PermissionUpdateDTO toPermissionUpdateDTO(SysPermission sysPermission);

    // ==================== 辅助方法 ====================

    /**
     * 获取权限类型名称
     */
    default String getPermissionTypeName(Integer permissionType) {
        if (permissionType == null) return "未知";
        return switch (permissionType) {
            case 1 -> "目录";
            case 2 -> "菜单";
            case 3 -> "按钮";
            case 4 -> "接口";
            default -> "未知";
        };
    }

    /**
     * 获取状态名称
     */
    default String getStatusName(Integer status) {
        if (status == null) return "未知";
        return switch (status) {
            case 0 -> "禁用";
            case 1 -> "启用";
            default -> "未知";
        };
    }

    /**
     * 复制实体基本信息
     */
    @Mapping(target = "deleted", ignore = true)
    SysPermission copyEntityBasicInfo(SysPermission source);
} 