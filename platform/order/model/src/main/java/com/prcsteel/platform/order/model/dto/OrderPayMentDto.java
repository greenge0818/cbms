package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class OrderPayMentDto {
	private Long orderId;
	
    private String buyerName;
    
    private String sellerName;
    
    private String code;
    
    private Date firstApplyTime;//首次申请付款时间
    
    private Date firstPayTime;//首次确认付款时间
    
    private String status;
    
    private String accounting;//核算会计
    
    private String buyerOrgName;
    
    private BigDecimal payAmount;//首次付款金额
    
    private String paymentBank;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getFirstApplyTime() {
		return firstApplyTime;
	}

	public void setFirstApplyTime(Date firstApplyTime) {
		this.firstApplyTime = firstApplyTime;
	}

	public Date getFirstPayTime() {
		return firstPayTime;
	}

	public void setFirstPayTime(Date firstPayTime) {
		this.firstPayTime = firstPayTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccounting() {
		return accounting;
	}

	public void setAccounting(String accounting) {
		this.accounting = accounting;
	}

	public String getBuyerOrgName() {
		return buyerOrgName;
	}

	public void setBuyerOrgName(String buyerOrgName) {
		this.buyerOrgName = buyerOrgName;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getPaymentBank() {
		return paymentBank;
	}

	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}
}
