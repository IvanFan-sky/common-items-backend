package com.common.core.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Description 用户头像更新DTO，用于头像上传和更新
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Data
@Schema(description = "用户头像更新DTO")
public class UserAvatarUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 头像地址
     */
    @NotBlank(message = "头像地址不能为空")
    @Size(max = 200, message = "头像地址长度不能超过200个字符")
    @Schema(description = "头像地址", example = "https://example.com/avatar.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String avatar;
} 