package com.ivanfang.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ivanfang.model.base.BaseEntity;
import lombok.Data;


@Data
@TableName("sys_role")			// map to the specified table in database
public class SysRole extends BaseEntity {

	private static final long serialVersionUID = 1L;

	// 角色名稱
	@TableField("role_name")	// map to the specified field (column) in database
	private String roleName;

	// 角色編碼
	@TableField("role_code")
	private String roleCode;

	// 描述
	@TableField("description")
	private String description;
}

