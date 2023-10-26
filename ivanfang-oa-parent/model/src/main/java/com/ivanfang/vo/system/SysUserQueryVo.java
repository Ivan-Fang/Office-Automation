//
//
package com.ivanfang.vo.system;


import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 使用者查詢實體
 * </p>
 */
@Data
public class SysUserQueryVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String keyword;

	private String createTimeBegin;
	private String createTimeEnd;

	private Long roleId;
	private Long postId;
	private Long deptId;

}

