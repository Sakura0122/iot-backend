package com.sakura.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author: sakura
 * @date: 2024/10/10 17:00
 * @description:
 */
@Data
@Schema(description = "删除参数实体类")
public class DeleteDto {
    @Schema(description = "ID")
    @NotNull(message = "ID不能为空")
    private Long id;
}
