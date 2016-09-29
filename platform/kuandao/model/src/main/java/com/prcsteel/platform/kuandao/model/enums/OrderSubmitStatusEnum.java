package com.prcsteel.platform.kuandao.model.enums;

public enum OrderSubmitStatusEnum {
	waitSubmit(0,"待提交"),
	finish(1,"支付完成"),
	submitFailed(2,"提交失败"),
	unmatch(3,"未匹配"),
	match(4,"已匹配"),
	refund(5,"已退款");
	
	private int code;
	private String text;
	
	private OrderSubmitStatusEnum(int code, String text) {
		this.code = code;
		this.text = text;
	}
	public int getCode() {
		return code;
	}
	public String getText() {
		return text;
	}
	
	
}
