package com.prcsteel.platform.kuandao.model.enums;

public enum ResponseStatus {

	success("00","成功"), fail("01","失败");
	
	private String code;
	
	private String text;
	
	private ResponseStatus(String code, String text){
		this.code = code;
		this.text = text;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public String getText(){
		return this.text;
	}
}
