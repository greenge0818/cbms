package com.prcsteel.platform.kuandao.model.enums;

public enum KuandaoChargStatus {

	init(0,"待充值"),finish(1,"已充值");
	
	private Integer code;
	
	private String text;
	
	private KuandaoChargStatus(Integer code, String text){
		this.setCode(code);
		this.setText(text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
