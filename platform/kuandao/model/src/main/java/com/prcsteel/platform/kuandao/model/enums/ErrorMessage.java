package com.prcsteel.platform.kuandao.model.enums;

public enum ErrorMessage {
	
	spdbTransFailed("与浦发系统交易失败，请稍后重试");
	
	private String errMsg;
	
	private ErrorMessage(String errMsg){
		this.errMsg = errMsg;
	}

	public String getErrMsg(){
		return this.errMsg;
	}
}
