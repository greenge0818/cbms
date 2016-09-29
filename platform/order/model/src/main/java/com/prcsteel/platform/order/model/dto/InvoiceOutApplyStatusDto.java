package com.prcsteel.platform.order.model.dto;

public class InvoiceOutApplyStatusDto {
	private String status;
	
	private Long accountId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
}
