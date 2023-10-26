package com.ivanfang.vo.system;

import lombok.Data;

@Data
public class SysPostQueryVo {

	// @ApiModelProperty(value = "崗位編碼")
	private String postCode;

	// @ApiModelProperty(value = "崗位名稱")
	private String name;

	// @ApiModelProperty(value = "狀態（1正常 0停用）")
	private Boolean status;


}