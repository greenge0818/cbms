package com.prcsteel.platform.kuandao.model.enums;

public enum KuandaoSequenceTypeEnum {
	
	payOrderCode("KuandaoPayOrderCode"),
	accountCode("KuandaoAcctCode"),
	refundCode("KuandaoRefundCode");
	
	private String type;
	
	private KuandaoSequenceTypeEnum(String type){
		this.type = type;
	}

	public String getType(){
		return this.type;
	}
}
