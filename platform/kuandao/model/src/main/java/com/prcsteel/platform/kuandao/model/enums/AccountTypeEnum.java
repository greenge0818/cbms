package com.prcsteel.platform.kuandao.model.enums;

public enum AccountTypeEnum {
	
	company("0","企业"),person("1","个人");
	
	private String code;
	
	private String text;
	
	private AccountTypeEnum(String code,String text){
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
