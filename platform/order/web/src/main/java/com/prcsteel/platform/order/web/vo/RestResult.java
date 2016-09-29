package com.prcsteel.platform.order.web.vo;

import java.io.Serializable;

public class RestResult implements Serializable{

	/**
	 * 成功
	 */
	public RestResult(){
		this.success = true;
	}

	public RestResult(Object data, boolean success){
		this.data = data;
		this.success = success;
	}

	public RestResult(Object data){
		this.data = data;
		success = true;
	}

	private static final long serialVersionUID = 1L;
	private boolean success;
	private String msg;
	private Object data;

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
