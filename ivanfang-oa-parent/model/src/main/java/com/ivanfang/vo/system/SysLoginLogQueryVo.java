package com.ivanfang.vo.system;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

@Data
public class SysLoginLogQueryVo {

	@ApiModelProperty(value = "用戶帳號")
	private String username;

	private String createTimeBegin;
	private String createTimeEnd;

}

