package com.sakura.model.dto.system.user;

import com.sakura.common.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户搜索对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "用户")
public class SysUserListDto extends PageDto {

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "开始时间")
    private String beginTime;

    @Schema(description = "结束时间")
    private String endTime;

}
