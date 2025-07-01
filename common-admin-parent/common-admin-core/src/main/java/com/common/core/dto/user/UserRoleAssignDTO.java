package com.common.core.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Description 用户角色分配DTO，用于用户角色关联操作
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Data
@Schema(description = "用户角色分配DTO")
public class UserRoleAssignDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "1", required = true)
    private Long userId;

    /**
     * 角色ID列表
     */
    @NotEmpty(message = "角色ID列表不能为空")
    @Schema(description = "角色ID列表", example = "[1, 2, 3]", required = true)
    private List<Long> roleIds;
} 