package com.ivanfang.model.system;

import com.ivanfang.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel(description = "SysLoginLog")
@TableName("sys_login_log")
public class SysLoginLog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用戶帳號")
	@TableField("username")
	private String username;

	@ApiModelProperty(value = "登錄IP地址")
	@TableField("ipaddr")
	private String ipaddr;

	@ApiModelProperty(value = "登錄狀態（0成功 1失敗）")
	@TableField("status")
	private Integer status;

	@ApiModelProperty(value = "提示資訊")
	@TableField("msg")
	private String msg;

	@ApiModelProperty(value = "存取時間")
	@TableField("access_time")
	private Date accessTime;

}
