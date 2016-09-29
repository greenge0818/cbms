package com.prcsteel.platform.kuandao.model.enums;

public enum AccountTranstypeEnum {
	
	open("1","会员开户"),modify("2","会员信息同步"),delete("3","会员注销");

	private String code;
	
	private String text;
	
	private AccountTranstypeEnum(String code,String text){
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
