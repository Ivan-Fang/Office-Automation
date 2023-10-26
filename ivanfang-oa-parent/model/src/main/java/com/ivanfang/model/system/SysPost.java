package com.ivanfang.model.system;

import com.ivanfang.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "崗位")
@TableName("sys_post")
public class SysPost extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "崗位編碼")
	@TableField("post_code")
	private String postCode;

	@ApiModelProperty(value = "崗位名稱")
	@TableField("name")
	private String name;

	@ApiModelProperty(value = "顯示順序")
	@TableField("description")
	private String description;

	@ApiModelProperty(value = "狀態（1正常 0停用）")
	@TableField("status")
	private Integer status;

}
