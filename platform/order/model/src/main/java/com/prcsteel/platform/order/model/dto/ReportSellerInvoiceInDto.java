package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

public class ReportSellerInvoiceInDto{

	private Long sellerId;

    private String sellerName;
    
	private BigDecimal primeBalance;
	
	private BigDecimal actualAmount;
	
	private BigDecimal invoiceInAmount;
	
	private BigDecimal terminalBalance;

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public BigDecimal getPrimeBalance() {
		return primeBalance;
	}

	public void setPrimeBalance(BigDecimal primeBalance) {
		this.primeBalance = primeBalance;
	}

	public BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}

	public BigDecimal getInvoiceInAmount() {
		return invoiceInAmount;
	}

	public void setInvoiceInAmount(BigDecimal invoiceInAmount) {
		this.invoiceInAmount = invoiceInAmount;
	}

	public BigDecimal getTerminalBalance() {
		return terminalBalance;
	}

	public void setTerminalBalance(BigDecimal terminalBalance) {
		this.terminalBalance = terminalBalance;
	}
	
}
