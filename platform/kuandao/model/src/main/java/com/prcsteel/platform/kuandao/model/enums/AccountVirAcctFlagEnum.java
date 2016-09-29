package com.prcsteel.platform.kuandao.model.enums;

public enum AccountVirAcctFlagEnum{

	YES("0","生成虚拟账号"),NO("1","不生成虚拟账号");
	
	private String code;
	
	private String text;
	
	private AccountVirAcctFlagEnum(String code,String text){
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
