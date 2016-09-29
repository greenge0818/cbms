package com.prcsteel.platform.common.exception;

/**
 * @Title: BusinessException.java
 * @Package com.prcsteel.cbms.web.constants
 * @Description: 业务层返回给controller层的错误，需要在controller层捕获处理
 * @author Green.Ge
 * @date 2015年8月11日 下午2:18:19
 * @version V1.0
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	String code;
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	String msg;

	public BusinessException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
