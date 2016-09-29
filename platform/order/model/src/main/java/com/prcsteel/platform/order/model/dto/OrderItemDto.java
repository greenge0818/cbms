package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

public class OrderItemDto {
	
	private Long accountId;
	
	private BigDecimal totalAmount;

	private int actualPickWeightSalesman;

	private BigDecimal dealPrice;

	private BigDecimal amount;

	private BigDecimal bringAmount;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getActualPickWeightSalesman() {
		return actualPickWeightSalesman;
	}

	public void setActualPickWeightSalesman(int actualPickWeightSalesman) {
		this.actualPickWeightSalesman = actualPickWeightSalesman;
	}

	public BigDecimal getDealPrice() {
		return dealPrice;
	}

	public void setDealPrice(BigDecimal dealPrice) {
		this.dealPrice = dealPrice;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBringAmount() {
		return bringAmount;
	}

	public void setBringAmount(BigDecimal bringAmount) {
		this.bringAmount = bringAmount;
	}
	
}
