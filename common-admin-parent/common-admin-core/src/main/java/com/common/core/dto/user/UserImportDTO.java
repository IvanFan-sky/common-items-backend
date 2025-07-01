package com.common.core.dto.user;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * @Description 用户导入数据传输对象，用于Excel导入功能
 * @Date 2025/1/7 18:30
 * @Author SparkFan
 */
@Data
@Schema(description = "用户导入DTO")
public class UserImportDTO {

    /**
     * 用户名（必填）
     */
    @ExcelProperty(value = "用户名", index = 0)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度必须在2-50个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "用户名只能包含字母、数字、下划线和中划线")
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称", index = 1)
    @Size(max = 50, message = "用户昵称长度不能超过50个字符")
    @Schema(description = "用户昵称", example = "管理员")
    private String nickname;

    /**
     * 邮箱地址
     */
    @ExcelProperty(value = "邮箱地址", index = 2)
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    @Schema(description = "邮箱地址", example = "admin@example.com")
    private String email;

    /**
     * 手机号码
     */
    @ExcelProperty(value = "手机号码", index = 3)
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号码", example = "13800138000")
    private String phone;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    @ExcelProperty(value = "性别", index = 4)
    @Schema(description = "性别", example = "男")
    private String genderText;

    /**
     * 生日
     */
    @ExcelProperty(value = "生日", index = 5)
    @DateTimeFormat("yyyy-MM-dd")
    @Schema(description = "生日", example = "1990-01-01")
    private LocalDate birthday;

    /**
     * 用户状态（0-禁用，1-启用）
     */
    @ExcelProperty(value = "状态", index = 6)
    @Schema(description = "用户状态", example = "启用")
    private String statusText;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注", index = 7)
    @Size(max = 500, message = "备注长度不能超过500个字符")
    @Schema(description = "备注", example = "这是一个备注")
    private String remark;

    // ==================== 转换后的字段 ====================

    /**
     * 性别数值
     */
    @Schema(description = "性别数值", example = "1")
    private Integer gender;

    /**
     * 状态数值
     */
    @Schema(description = "状态数值", example = "1")
    private Integer status;

    /**
     * 默认密码
     */
    @Schema(description = "默认密码", example = "123456")
    private String password = "123456";

    /**
     * 行号（用于错误提示）
     */
    @Schema(description = "Excel行号")
    private Integer rowIndex;

    /**
     * 验证错误信息
     */
    @Schema(description = "验证错误信息")
    private String errorMessage;
} 