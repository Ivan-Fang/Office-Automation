package com.ivanfang.model.process;

import com.ivanfang.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "ProcessTemplate")
@TableName("process_template")
public class ProcessTemplate extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "範本名稱")
	@TableField("name")
	private String name;

	@ApiModelProperty(value = "圖示路徑")
	@TableField("icon_url")
	private String iconUrl;

	@ApiModelProperty(value = "processTypeId")
	@TableField("process_type_id")
	private Long processTypeId;

	@ApiModelProperty(value = "表單屬性")
	@TableField("form_props")
	private String formProps;

	@ApiModelProperty(value = "表單選項")
	@TableField("form_options")
	private String formOptions;

	@ApiModelProperty(value = "描述")
	@TableField("description")
	private String description;

	@ApiModelProperty(value = "流程定義key")
	@TableField("process_definition_key")
	private String processDefinitionKey;

	@ApiModelProperty(value = "流程定義上傳路process_model_id")
	@TableField("process_definition_path")
	private String processDefinitionPath;

	@ApiModelProperty(value = "流程定義模型id")
	@TableField("process_model_id")
	private String processModelId;

	@ApiModelProperty(value = "狀態")
	@TableField("status")
	private Integer status;

	@TableField(exist = false)
	private String processTypeName;
}
