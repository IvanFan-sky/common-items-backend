package com.common.core.mapper;

import com.common.core.dto.role.RoleCreateDTO;
import com.common.core.dto.role.RoleQueryDTO;
import com.common.core.dto.role.RoleUpdateDTO;
import com.common.core.entity.SysRole;
import com.common.core.vo.role.RoleDetailVO;
import com.common.core.vo.role.RoleVO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Description 角色实体映射器，负责Entity、DTO、VO之间的转换
 * @Date 2025/1/8 9:30
 * @Author SparkFan
 */
@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleConvertMapper {

    RoleConvertMapper INSTANCE = Mappers.getMapper(RoleConvertMapper.class);

    // ==================== Entity -> VO ====================

    /**
     * 实体转基本VO
     */
    @Mapping(target = "statusText", expression = "java(getStatusName(sysRole.getStatus()))")
    @Mapping(target = "dataScopeText", expression = "java(getDataScopeName(sysRole.getDataScope()))")
    RoleVO toRoleVO(SysRole sysRole);

    /**
     * 实体列表转VO列表
     */
    List<RoleVO> toRoleVOList(List<SysRole> sysRoleList);

    /**
     * 实体转详细VO
     */
    @Mapping(target = "statusText", expression = "java(getStatusName(sysRole.getStatus()))")
    @Mapping(target = "dataScopeText", expression = "java(getDataScopeName(sysRole.getDataScope()))")
    @Mapping(target = "createBy", ignore = true)         // 复杂对象需要单独处理
    @Mapping(target = "updateBy", ignore = true)         // 复杂对象需要单独处理
    @Mapping(target = "permissionIds", ignore = true)     // 需要单独查询
    @Mapping(target = "userCount", ignore = true)        // 需要单独查询
    RoleDetailVO toRoleDetailVO(SysRole sysRole);

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
    SysRole createDTOToEntity(RoleCreateDTO createDTO);

    /**
     * 更新DTO转实体
     */
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    SysRole updateDTOToEntity(RoleUpdateDTO updateDTO);

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
    void updateEntityFromDTO(RoleUpdateDTO updateDTO, @MappingTarget SysRole sysRole);

    // ==================== Entity -> DTO ====================

    /**
     * 实体转查询DTO（用于回显）
     */
    RoleQueryDTO toRoleQueryDTO(SysRole sysRole);

    /**
     * 实体转更新DTO（用于回显）
     */
    RoleUpdateDTO toRoleUpdateDTO(SysRole sysRole);

    // ==================== 辅助方法 ====================

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
     * 获取数据范围名称
     */
    default String getDataScopeName(Integer dataScope) {
        if (dataScope == null) return "全部数据";
        return switch (dataScope) {
            case 1 -> "全部数据";
            case 2 -> "自定数据";
            case 3 -> "本部门数据";
            case 4 -> "本部门及以下数据";
            case 5 -> "仅本人数据";
            default -> "全部数据";
        };
    }

    /**
     * 批量转换实体为详细VO
     */
    List<RoleDetailVO> toRoleDetailVOList(List<SysRole> sysRoleList);

    /**
     * 复制实体基本信息
     */
    @Mapping(target = "deleted", ignore = true)
    SysRole copyEntityBasicInfo(SysRole source);
} 