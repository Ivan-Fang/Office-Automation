package com.ivanfang.vo.process;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApprovalVo {

    private Long processId;

    private String taskId;

    @ApiModelProperty(value = "狀態")
    private Integer status;

    @ApiModelProperty(value = "審批描述")
    private String description;
}
