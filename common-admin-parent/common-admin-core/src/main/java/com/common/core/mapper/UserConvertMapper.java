package com.common.core.mapper;

import com.common.core.dto.user.UserCreateDTO;
import com.common.core.dto.user.UserQueryDTO;
import com.common.core.dto.user.UserUpdateDTO;
import com.common.core.dto.user.UserProfileUpdateDTO;
import com.common.core.dto.user.UserAvatarUpdateDTO;
import com.common.core.dto.user.UserSecuritySettingsDTO;
import com.common.core.entity.SysUser;
import com.common.core.vo.user.UserDetailVO;
import com.common.core.vo.user.UserVO;
import com.common.core.vo.user.UserProfileVO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Description 用户实体映射器，负责Entity、DTO、VO之间的转换
 * @Date 2025/1/7 16:30
 * @Author SparkFan
 */
@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConvertMapper {

    UserConvertMapper INSTANCE = Mappers.getMapper(UserConvertMapper.class);

    // ==================== Entity -> VO ====================

    /**
     * 实体转基本VO
     */
    UserVO toUserVO(SysUser sysUser);

    /**
     * 实体列表转VO列表
     */
    List<UserVO> toUserVOList(List<SysUser> sysUserList);

    /**
     * 实体转详细VO
     */
    @Mapping(target = "genderText", expression = "java(getGenderName(sysUser.getGender()))")
    @Mapping(target = "statusText", expression = "java(getStatusName(sysUser.getStatus()))")
    @Mapping(target = "displayName", expression = "java(getDisplayName(sysUser))")
    @Mapping(target = "age", expression = "java(calculateAge(sysUser.getBirthday()))")
    @Mapping(target = "createBy", ignore = true)  // 复杂对象需要单独处理
    @Mapping(target = "updateBy", ignore = true)  // 复杂对象需要单独处理
    @Mapping(target = "roles", ignore = true)     // 关联数据需要单独查询
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "menus", ignore = true)
    @Mapping(target = "socialAccounts", ignore = true)
    @Mapping(target = "stats", ignore = true)
    @Mapping(target = "settings", ignore = true)
    @Mapping(target = "loginAddress", ignore = true)  // 需要根据IP解析
    @Mapping(target = "online", ignore = true)        // 需要查询在线状态
    @Mapping(target = "superAdmin", ignore = true)    // 需要根据角色判断
    UserDetailVO toUserDetailVO(SysUser sysUser);

    /**
     * 实体转个人资料VO
     */
    @Mapping(target = "genderText", expression = "java(getGenderName(sysUser.getGender()))")
    @Mapping(target = "age", expression = "java(calculateAge(sysUser.getBirthday()))")
    @Mapping(target = "tags", ignore = true)          // 需要单独解析JSON
    @Mapping(target = "roles", ignore = true)         // 需要单独查询
    @Mapping(target = "permissions", ignore = true)   // 需要单独查询
    @Mapping(target = "emailVerified", ignore = true) // 需要单独设置
    @Mapping(target = "phoneVerified", ignore = true) // 需要单独设置
    @Mapping(target = "twoFactorEnabled", ignore = true) // 需要单独设置
    @Mapping(target = "profileBackground", ignore = true) // 映射到合适的字段
    @Mapping(target = "position", ignore = true)      // 映射到合适的字段
    @Mapping(target = "department", ignore = true)    // 映射到合适的字段
    @Mapping(target = "address", ignore = true)       // 映射到合适的字段
    @Mapping(target = "realName", ignore = true)      // 映射到合适的字段
    UserProfileVO entityToProfileVO(SysUser sysUser);

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
    @Mapping(target = "loginIp", ignore = true)
    @Mapping(target = "loginTime", ignore = true)
    SysUser createDTOToEntity(UserCreateDTO createDTO);

    /**
     * 更新DTO转实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)  // 密码单独处理
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "loginIp", ignore = true)
    @Mapping(target = "loginTime", ignore = true)
    SysUser updateDTOToEntity(UserUpdateDTO updateDTO);

    /**
     * 用更新DTO的值更新已有实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "loginIp", ignore = true)
    @Mapping(target = "loginTime", ignore = true)
    void updateEntityFromDTO(UserUpdateDTO updateDTO, @MappingTarget SysUser sysUser);

    /**
     * 个人资料更新DTO转实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)     // 用户名不允许修改
    @Mapping(target = "password", ignore = true)     // 密码单独处理
    @Mapping(target = "status", ignore = true)       // 状态不允许用户自己修改
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "loginIp", ignore = true)
    @Mapping(target = "loginTime", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    @Mapping(target = "lastLoginTime", ignore = true)
    @Mapping(target = "loginCount", ignore = true)
    @Mapping(target = "passwordUpdateTime", ignore = true)
    SysUser profileUpdateDTOToEntity(UserProfileUpdateDTO profileUpdateDTO);

    /**
     * 用个人资料更新DTO的值更新已有实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "loginIp", ignore = true)
    @Mapping(target = "loginTime", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    @Mapping(target = "lastLoginTime", ignore = true)
    @Mapping(target = "loginCount", ignore = true)
    @Mapping(target = "passwordUpdateTime", ignore = true)
    void updateEntityFromProfileDTO(UserProfileUpdateDTO profileUpdateDTO, @MappingTarget SysUser sysUser);

    // ==================== Entity -> DTO ====================

    /**
     * 实体转查询DTO（用于回显）
     */
    UserQueryDTO toUserQueryDTO(SysUser sysUser);

    /**
     * 实体转更新DTO（用于回显）
     */
    UserUpdateDTO toUserUpdateDTO(SysUser sysUser);

    /**
     * 实体转个人资料更新DTO（用于回显）
     */
    @Mapping(target = "tags", ignore = true)          // 需要单独处理JSON
    @Mapping(target = "profileBackground", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "realName", ignore = true)
    UserProfileUpdateDTO toUserProfileUpdateDTO(SysUser sysUser);

    // ==================== 辅助方法 ====================

    /**
     * 获取性别名称
     */
    default String getGenderName(Integer gender) {
        if (gender == null) return "未知";
        return switch (gender) {
            case 1 -> "男";
            case 2 -> "女";
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
     * 复制实体基本信息（不包括敏感字段）
     */
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    SysUser copyEntityBasicInfo(SysUser source);

    /**
     * 批量转换实体为详细VO
     */
    List<UserDetailVO> toUserDetailVOList(List<SysUser> sysUserList);

    /**
     * 获取显示名称
     */
    default String getDisplayName(SysUser sysUser) {
        if (sysUser == null) return "";
        return sysUser.getNickname() != null && !sysUser.getNickname().trim().isEmpty() 
                ? sysUser.getNickname() : sysUser.getUsername();
    }

    /**
     * 计算年龄
     */
    default Integer calculateAge(java.time.LocalDate birthday) {
        if (birthday == null) return null;
        return java.time.Period.between(birthday, java.time.LocalDate.now()).getYears();
    }
}