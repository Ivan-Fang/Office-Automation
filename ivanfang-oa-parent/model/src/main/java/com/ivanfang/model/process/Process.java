package com.ivanfang.model.process;

import com.ivanfang.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Process")
@TableName("process")
public class Process extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "審批code")
	@TableField("process_code")
	private String processCode;

	@ApiModelProperty(value = "使用者id")
	@TableField("user_id")
	private Long userId;

	@ApiModelProperty(value = "審批模板id")
	@TableField("process_template_id")
	private Long processTemplateId;

	@ApiModelProperty(value = "審批類型id")
	@TableField("process_type_id")
	private Long processTypeId;

	@ApiModelProperty(value = "標題")
	@TableField("title")
	private String title;

	@ApiModelProperty(value = "描述")
	@TableField("description")
	private String description;

	@ApiModelProperty(value = "表單值")
	@TableField("form_values")
	private String formValues;

	@ApiModelProperty(value = "流程實例id")
	@TableField("process_instance_id")
	private String processInstanceId;

	@ApiModelProperty(value = "當前審批人")
	@TableField("current_auditor")
	private String currentAuditor;

	@ApiModelProperty(value = "狀態（0：預設 1：審批中 2：審批通過 -1：駁回）")
	@TableField("status")
	private Integer status;
}