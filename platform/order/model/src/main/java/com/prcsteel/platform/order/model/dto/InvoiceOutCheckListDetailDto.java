package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

import com.prcsteel.platform.order.model.model.InvoiceOutCheckListDetail;

public class InvoiceOutCheckListDetailDto extends InvoiceOutCheckListDetail{
	private Long buyerId;
	private BigDecimal balanceSecondSettlement;
	public BigDecimal getBalanceSecondSettlement() {
		return balanceSecondSettlement;
	}
	public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
		this.balanceSecondSettlement = balanceSecondSettlement;
	}
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
}
