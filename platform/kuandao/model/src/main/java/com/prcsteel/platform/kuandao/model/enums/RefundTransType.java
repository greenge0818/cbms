package com.prcsteel.platform.kuandao.model.enums;

public enum RefundTransType {
	
	good(0,"退货"),fund(1,"退款");
	
	private int code;
	
	private String text;
	
	private RefundTransType(int code, String text){
		this.code = code;
		this.text = text;
	}
	
	public int getCode(){
		return this.code;
	}
	
	public String getText(){
		return this.text;
	}
}
