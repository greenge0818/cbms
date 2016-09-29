package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

public class CustomerAccountDto {

	private Long accountId;

	private String cName;

	private BigDecimal balance;

	private BigDecimal balanceSecondSettlement;

	private BigDecimal totalAmount;

	private int actualPickWeightSalesman;

	private BigDecimal dealPrice;

	private String baseName;

	private BigDecimal amount;

	private BigDecimal bringAmount;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getBringAmount() {
		return bringAmount;
	}

	public void setBringAmount(BigDecimal bringAmount) {
		this.bringAmount = bringAmount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getBalanceSecondSettlement() {
		return balanceSecondSettlement;
	}

	public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
		this.balanceSecondSettlement = balanceSecondSettlement;
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

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}
}
