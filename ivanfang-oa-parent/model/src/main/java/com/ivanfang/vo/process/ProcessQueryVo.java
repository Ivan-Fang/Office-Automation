package com.ivanfang.vo.process;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Process")
public class ProcessQueryVo {

	@ApiModelProperty(value = "關鍵字（process code）")
	private String keyword;

	@ApiModelProperty(value = "使用者id")
	private Long userId;

	@ApiModelProperty(value = "審批模板id")
	@TableField("process_template_id")
	private Long processTemplateId;

	@ApiModelProperty(value = "審批類型id")
	@TableField("process_type_id")
	private Long processTypeId;

	private String createTimeBegin;
	private String createTimeEnd;

	@ApiModelProperty(value = "狀態（0：預設、1：審批中、2：審批通過、-1：駁回）")
	private Integer status;


}
