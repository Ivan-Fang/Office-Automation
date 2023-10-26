package com.ivanfang.vo.process;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "Process")
public class ProcessVo {

	private Long id;

	private Date createTime;

	@ApiModelProperty(value = "審批code")
	private String processCode;

	@ApiModelProperty(value = "使用者id")
	private Long userId;
	private String name;

	@TableField("process_template_id")
	private Long processTemplateId;
	private String processTemplateName;

	@ApiModelProperty(value = "審批類型id")
	private Long processTypeId;
	private String processTypeName;

	@ApiModelProperty(value = "標題")
	private String title;

	@ApiModelProperty(value = "描述")
	private String description;

	@ApiModelProperty(value = "表單屬性")
	private String formProps;

	@ApiModelProperty(value = "表單選項")
	private String formOptions;

	@ApiModelProperty(value = "表單屬性值")
	private String formValues;

	@ApiModelProperty(value = "流程實例id")
	private String processInstanceId;

	@ApiModelProperty(value = "目前審批人")
	private String currentAuditor;

	@ApiModelProperty(value = "狀態（0：預設、1：審批中、2：審批通過、-1：駁回）")
	private Integer status;

	private String taskId;

}
