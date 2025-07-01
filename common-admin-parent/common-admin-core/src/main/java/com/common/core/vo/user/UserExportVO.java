package com.common.core.vo.user;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description 用户导出视图对象，用于Excel导出功能
 * @Date 2025/1/7 18:30
 * @Author SparkFan
 */
@Data
@Schema(description = "用户导出VO")
public class UserExportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID", index = 0)
    @Schema(description = "用户ID", example = "1")
    private Long id;

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名", index = 1)
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称", index = 2)
    @Schema(description = "用户昵称", example = "管理员")
    private String nickname;

    /**
     * 邮箱地址
     */
    @ExcelProperty(value = "邮箱地址", index = 3)
    @Schema(description = "邮箱地址", example = "admin@example.com")
    private String email;

    /**
     * 手机号码
     */
    @ExcelProperty(value = "手机号码", index = 4)
    @Schema(description = "手机号码", example = "13800138000")
    private String phone;

    /**
     * 性别描述
     */
    @ExcelProperty(value = "性别", index = 5)
    @Schema(description = "性别描述", example = "男")
    private String genderText;

    /**
     * 生日
     */
    @ExcelProperty(value = "生日", index = 6)
    @DateTimeFormat("yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "生日", example = "1990-01-01")
    private LocalDate birthday;

    /**
     * 用户状态描述
     */
    @ExcelProperty(value = "状态", index = 7)
    @Schema(description = "用户状态描述", example = "启用")
    private String statusText;

    /**
     * 最近登录IP
     */
    @ExcelProperty(value = "最近登录IP", index = 8)
    @Schema(description = "最近登录IP", example = "192.168.1.1")
    private String lastLoginIp;

    /**
     * 最近登录时间
     */
    @ExcelProperty(value = "最近登录时间", index = 9)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最近登录时间", example = "2024-01-01 12:00:00")
    private LocalDateTime lastLoginTime;

    /**
     * 登录次数
     */
    @ExcelProperty(value = "登录次数", index = 10)
    @Schema(description = "登录次数", example = "10")
    private Integer loginCount;

    /**
     * 角色名称列表
     */
    @ExcelProperty(value = "角色", index = 11)
    @Schema(description = "角色名称列表", example = "管理员,用户")
    private String roleNames;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注", index = 12)
    @Schema(description = "备注", example = "这是一个备注")
    private String remark;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", index = 13)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2024-01-01 12:00:00")
    private LocalDateTime createTime;

    /**
     * 创建者名称
     */
    @ExcelProperty(value = "创建者", index = 14)
    @Schema(description = "创建者名称", example = "系统管理员")
    private String createByName;
} 