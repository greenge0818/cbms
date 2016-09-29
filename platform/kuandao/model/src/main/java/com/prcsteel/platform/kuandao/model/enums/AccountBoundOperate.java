package com.prcsteel.platform.kuandao.model.enums;

public enum AccountBoundOperate {

	bound("1","绑定"), unbound("2","解绑");
	
	private String code;
	
	private String text;
	
	private AccountBoundOperate(String code, String text){
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
	
}
