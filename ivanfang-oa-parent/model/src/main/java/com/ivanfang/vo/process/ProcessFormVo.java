package com.ivanfang.vo.process;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "流程表單")
public class ProcessFormVo {

	@ApiModelProperty(value = "審批模板id")
	private Long processTemplateId;

	@ApiModelProperty(value = "審批類型id")
	private Long processTypeId;

	@ApiModelProperty(value = "表單值")
	private String formValues;

}
