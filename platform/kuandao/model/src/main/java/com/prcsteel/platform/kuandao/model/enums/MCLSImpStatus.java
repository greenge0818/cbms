package com.prcsteel.platform.kuandao.model.enums;

public enum MCLSImpStatus {

	unmatch("00","未匹配"),
	match("01","已匹配"),
	refund("02","已退款"),
	finish("03","已完成");
	
	private String code;
	
	private String text;
	
	private MCLSImpStatus(String code,String text){
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
		MCLSImpStatus[] values = MCLSImpStatus.values();
		for(MCLSImpStatus value : values){
			if(value.getCode().equals(code)){
				return value.getText();
			}
		}
		return null;
	}
}
