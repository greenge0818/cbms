package com.prcsteel.platform.kuandao.model.enums;

public enum ESBResultStatus {
	
	success("0","成功");
	
	private String code;
	
	private String text;
	
	private ESBResultStatus(String code,String text){
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

}
