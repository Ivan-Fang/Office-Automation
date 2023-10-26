package com.ivanfang.model.process;

import com.ivanfang.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "ProcessRecord")
@TableName("process_record")
public class ProcessRecord extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "審批流程id")
	@TableField("process_id")
	private Long processId;

	@ApiModelProperty(value = "審批描述")
	@TableField("description")
	private String description;

	@ApiModelProperty(value = "狀態")
	@TableField("status")
	private Integer status;

	@ApiModelProperty(value = "操作使用者id")
	@TableField("operate_user_id")
	private Long operateUserId;

	@ApiModelProperty(value = "操作使用者")
	@TableField("operate_user")
	private String operateUser;

}
