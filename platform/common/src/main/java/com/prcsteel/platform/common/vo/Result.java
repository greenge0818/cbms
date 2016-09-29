package com.prcsteel.platform.common.vo;

import java.io.Serializable;

public class Result implements Serializable{

	/**
	 * 成功
	 */
	public Result(){
		this.success = true;
	}
	
	public Result(Object data,boolean success){
		this.data = data;
		this.success = success;
	}
	
	public Result(Object data){
		this.data = data;
		success = true;
	}
	
	private static final long serialVersionUID = 1L;
	private boolean success;
	private Object data;

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
