package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by chenchen on 2015/8/6.
 */
public class AccountInvoiceNoPassDto {
		private String accountName;
	    private String buyerOrgName;
	    private String invoiceDataStatus;
	    private String status;
	    private BigDecimal balanceSecondSettlement;
	    private BigDecimal applyAmount;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getBuyerOrgName() {
		return buyerOrgName;
	}

	public void setBuyerOrgName(String buyerOrgName) {
		this.buyerOrgName = buyerOrgName;
	}

	public String getInvoiceDataStatus() {
		return invoiceDataStatus;
	}

	public void setInvoiceDataStatus(String invoiceDataStatus) {
		this.invoiceDataStatus = invoiceDataStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getBalanceSecondSettlement() {
		return balanceSecondSettlement;
	}

	public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
		this.balanceSecondSettlement = balanceSecondSettlement;
	}

	public BigDecimal getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
	}
}
