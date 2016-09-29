package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * 应收应付发票报表。
 * @author tuxianming
 *
 */
public class ReportInvoiceInAndOutDto {
	private Long accountId;				//客户id
	private String accountName;				//客户全称
	private BigDecimal initAmount;			//期初未开票（元）
	private BigDecimal invoiceAmount;		//应开发票（元）
	private BigDecimal actInvoiceAmount;	//实开发票（元）
	private BigDecimal finalAmount;			//期末未开票（元）
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public BigDecimal getInitAmount() {
		return initAmount;
	}
	public void setInitAmount(BigDecimal initAmount) {
		this.initAmount = initAmount;
	}
	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(BigDecimal invoiceInAmount) {
		this.invoiceAmount = invoiceInAmount;
	}
	public BigDecimal getActInvoiceAmount() {
		return actInvoiceAmount;
	}
	public void setActInvoiceAmount(BigDecimal actInvoiceAmount) {
		this.actInvoiceAmount = actInvoiceAmount;
	}
	public BigDecimal getFinalAmount() {
		return finalAmount;
	}
	public void setFinalAmount(BigDecimal finalAmount) {
		this.finalAmount = finalAmount;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
}
