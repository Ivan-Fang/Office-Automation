package com.ivanfang.model.system;

import com.ivanfang.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "部門")
@TableName("sys_dept")
public class SysDept extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "部門名稱")
	@TableField("name")
	private String name;

	@ApiModelProperty(value = "上級部門id")
	@TableField("parent_id")
	private Long parentId;

	@ApiModelProperty(value = "樹結構")
	@TableField("tree_path")
	private String treePath;

	@ApiModelProperty(value = "排序")
	@TableField("sort_value")
	private Integer sortValue;

	@ApiModelProperty(value = "負責人")
	@TableField("leader")
	private String leader;

	@ApiModelProperty(value = "電話")
	@TableField("phone")
	private String phone;

	@ApiModelProperty(value = "狀態（1正常 0停用）")
	@TableField("status")
	private Integer status;

	@ApiModelProperty(value = "下級部門")
	@TableField(exist = false)
	private List<SysDept> children;

}