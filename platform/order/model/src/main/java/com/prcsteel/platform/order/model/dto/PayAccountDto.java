package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * 
 * @author lixiang 2015.8.1
 *
 */
public class PayAccountDto {
	
	private BigDecimal balance;
	
	private BigDecimal balanceSecondSettlement;

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
	
	
}
