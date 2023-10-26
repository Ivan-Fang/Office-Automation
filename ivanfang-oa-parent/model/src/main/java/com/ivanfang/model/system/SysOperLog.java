package com.ivanfang.model.system;

import com.ivanfang.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel(description = "SysOperLog")
@TableName("sys_oper_log")
public class SysOperLog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "模組標題")
	@TableField("title")
	private String title;

	@ApiModelProperty(value = "業務類型（0其它 1新增 2修改 3刪除）")
	@TableField("business_type")
	private String businessType;

	@ApiModelProperty(value = "方法名稱")
	@TableField("method")
	private String method;

	@ApiModelProperty(value = "請求方式")
	@TableField("request_method")
	private String requestMethod;

	@ApiModelProperty(value = "操作類別（0其它 1後臺用戶 2手機端用戶）")
	@TableField("operator_type")
	private String operatorType;

	@ApiModelProperty(value = "操作人員")
	@TableField("oper_name")
	private String operName;

	@ApiModelProperty(value = "部門名稱")
	@TableField("dept_name")
	private String deptName;

	@ApiModelProperty(value = "請求URL")
	@TableField("oper_url")
	private String operUrl;

	@ApiModelProperty(value = "主機位址")
	@TableField("oper_ip")
	private String operIp;

	@ApiModelProperty(value = "請求參數")
	@TableField("oper_param")
	private String operParam;

	@ApiModelProperty(value = "返回參數")
	@TableField("json_result")
	private String jsonResult;

	@ApiModelProperty(value = "操作狀態（0正常 1異常）")
	@TableField("status")
	private Integer status;

	@ApiModelProperty(value = "錯誤消息")
	@TableField("error_msg")
	private String errorMsg;

	@ApiModelProperty(value = "操作時間")
	@TableField("oper_time")
	private Date operTime;

}
