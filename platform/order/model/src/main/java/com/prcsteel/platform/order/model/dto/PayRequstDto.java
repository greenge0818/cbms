package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.PayRequest;

public class PayRequstDto extends PayRequest{
	private String payCode;

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	
}
