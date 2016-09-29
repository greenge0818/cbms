package com.prcsteel.platform.kuandao.common.constant;

import org.springframework.beans.factory.annotation.Value;

public class KuandaoSequencePrefix {

	@Value("${Kuandao.Sequence.Prefix.account}")
	private String accountCode;
	
	@Value("${Kuandao.Sequence.Prefix.order}")
	private String payOrderCode;
	
	@Value("${Kuandao.Sequence.Prefix.refund}")
	private String refundCode;
	
	public String getAccountCode(){
		return this.accountCode;
	}
	
	public String getPayOrderCode(){
		return this.payOrderCode;
	}
	
	public String getRefundCode(){
		return this.refundCode;
	}
}
