package com.prcsteel.platform.kuandao.model.enums;

public enum SPDBResponseEnum {
	
	success("00","交易成功"),
	contact("01","联系发卡方");
	
	private String code;
	private String text;
	
	private SPDBResponseEnum(String code,String text){
		this.code = code;
		this.text = text;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public String getText(){
		return this.text;
	}
	
	public static String getTextByCode(String code){
		SPDBResponseEnum[] responses = SPDBResponseEnum.values();
		for(SPDBResponseEnum response : responses){
			if(response.code.equals(code)){
				return response.text;
			}
		}
		return "";
	}
}
