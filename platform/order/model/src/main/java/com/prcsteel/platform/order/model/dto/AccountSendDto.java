package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

public class AccountSendDto {
	private Long accountId;//客户id
	
	private Long invoiceId;//进项票id
	
	private Long orderId;//订单id
	
	private String sendType;// 是否可以寄出

	private String accountName;// 买家全称

	private String invoiceInformation;// 开票资料状态

	private String applyInvocieType;// 开票申请状态

	private BigDecimal balanceSecondSettlement;// 买家二结欠款金额
	
	private String balanceSecondSettlementStr;

	public String getBalanceSecondSettlementStr() {
		return balanceSecondSettlementStr;
	}

	public void setBalanceSecondSettlementStr(String balanceSecondSettlementStr) {
		this.balanceSecondSettlementStr = balanceSecondSettlementStr;
	}

	private int statistics;//统计

	public int getStatistics() {
		return statistics;
	}

	public void setStatistics(int statistics) {
		this.statistics = statistics;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getInvoiceInformation() {
		return invoiceInformation;
	}

	public void setInvoiceInformation(String invoiceInformation) {
		this.invoiceInformation = invoiceInformation;
	}

	public String getApplyInvocieType() {
		return applyInvocieType;
	}

	public void setApplyInvocieType(String applyInvocieType) {
		this.applyInvocieType = applyInvocieType;
	}

	public BigDecimal getBalanceSecondSettlement() {
		return balanceSecondSettlement;
	}

	public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
		this.balanceSecondSettlement = balanceSecondSettlement;
	}

}
