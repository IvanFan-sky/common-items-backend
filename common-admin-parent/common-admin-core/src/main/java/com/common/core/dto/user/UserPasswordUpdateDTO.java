package com.common.core.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Description 用户密码修改DTO，用于用户修改密码功能
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Data
@Schema(description = "用户密码修改DTO")
public class UserPasswordUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    /**
     * 原密码
     */
    @NotBlank(message = "原密码不能为空")
    @Schema(description = "原密码", example = "OldPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String oldPassword;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "新密码长度必须在6-20个字符之间")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{6,20}$", 
             message = "新密码必须包含大小写字母和数字，长度6-20位")
    @Schema(description = "新密码", example = "NewPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;

    /**
     * 确认新密码
     */
    @NotBlank(message = "确认新密码不能为空")
    @Schema(description = "确认新密码", example = "NewPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String confirmNewPassword;

    /**
     * 验证新密码一致性
     */
    @AssertTrue(message = "两次输入的新密码不一致")
    public boolean isNewPasswordMatch() {
        if (newPassword == null || confirmNewPassword == null) {
            return false;
        }
        return newPassword.equals(confirmNewPassword);
    }

    /**
     * 验证新旧密码不能相同
     */
    @AssertTrue(message = "新密码不能与原密码相同")
    public boolean isPasswordDifferent() {
        if (oldPassword == null || newPassword == null) {
            return true; // 让其他验证来处理null的情况
        }
        return !oldPassword.equals(newPassword);
    }
} 