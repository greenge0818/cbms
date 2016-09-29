package com.prcsteel.platform.kuandao.model.enums;

public enum DepositWarnType {

	nopayer(0,"买家不存在"),
	duplicate(1,"存在多个买家"),
	noaccount(2,"买家开户失败"),
	limitbank(3,"受限银行");
	
	private Integer code;
	
	private String text;
	
	private DepositWarnType(Integer code, String text){
		this.code = code;
		this.text = text;
	}
	
	public Integer getCode(){
		return this.code;
	}
	
	public String getText(){
		return this.text;
	}
}
