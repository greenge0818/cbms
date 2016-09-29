package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.InvoiceInFlowLog;

public class InvoiceInFlowDto extends InvoiceInFlowLog{
	
	private String name;
	
	private String tel;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
