package com.prcsteel.platform.kuandao.model.enums;

public enum AccountStatusEnum {
	
	bound("0","已绑定"),close("1","已销户"),open("2","已开户待绑定"),openviracct("3","待开立虚拟账号"),fail("4","失败"),init("5","待开户");
	
	private String code;
	
	private String text;
	
	private AccountStatusEnum(String code,String text){
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
