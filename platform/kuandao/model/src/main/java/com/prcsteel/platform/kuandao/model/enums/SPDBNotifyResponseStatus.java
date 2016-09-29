package com.prcsteel.platform.kuandao.model.enums;

public enum SPDBNotifyResponseStatus {
	success("0","成功"), fail("1","失败");
	
	private String code;
	
	private String text;
	
	private SPDBNotifyResponseStatus(String code,String text){
		this.setCode(code);
		this.setText(text);
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
