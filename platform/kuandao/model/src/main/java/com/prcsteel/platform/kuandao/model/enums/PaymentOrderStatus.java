package com.prcsteel.platform.kuandao.model.enums;

public enum PaymentOrderStatus {

	match("00","已匹配"),
	confirm("01","全部确认"),
	unmatch("02","未匹配"),
	confirmPart("03","部分确认"),
	refundPart("04","部分退货"),
	refund("05","全部退货");
	
	private String code;
	
	private String text;
	
	private PaymentOrderStatus(String code,String text){
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
