package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PayRequestItemDto {

    private Long qid;
    
    private String code;
    
    private Long requesterId;
    
    private String requesterName;
    
    private Long orgId;
    
    private String orgName;
    
    private Long buyerId;
    
    private Date created;
    
    private String printTimes;
    
    private String buyerName;
    
    private Long itemId;
    
    private String receiverAccountCode;
    
    private String receiverName;
    
    private String receiverBankName;
    
    private String receiverBankCity;
    
    private String receiverBranchBankName;
    
    private BigDecimal payAmount;
    
    private String receiverBankCode;
    
    private String printName;
    
	private Date lastPrintDate;
	
	private String payCode;

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getPrintName() {
		return printName;
	}

	public void setPrintName(String printName) {
		this.printName = printName;
	}

	public Date getLastPrintDate() {
		return lastPrintDate;
	}

	public void setLastPrintDate(Date lastPrintDate) {
		this.lastPrintDate = lastPrintDate;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getReceiverBankCode() {
		return receiverBankCode;
	}

	public void setReceiverBankCode(String receiverBankCode) {
		this.receiverBankCode = receiverBankCode;
	}

	public String getPrintTimes() {
		return printTimes;
	}

	public void setPrintTimes(String printTimes) {
		this.printTimes = printTimes;
	}

	public Long getQid() {
		return qid;
	}

	public void setQid(Long qid) {
		this.qid = qid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getReceiverAccountCode() {
		return receiverAccountCode;
	}

	public void setReceiverAccountCode(String receiverAccountCode) {
		this.receiverAccountCode = receiverAccountCode;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverBankName() {
		return receiverBankName;
	}

	public void setReceiverBankName(String receiverBankName) {
		this.receiverBankName = receiverBankName;
	}

	public String getReceiverBankCity() {
		return receiverBankCity;
	}

	public void setReceiverBankCity(String receiverBankCity) {
		this.receiverBankCity = receiverBankCity;
	}

	public String getReceiverBranchBankName() {
		return receiverBranchBankName;
	}

	public void setReceiverBranchBankName(String receiverBranchBankName) {
		this.receiverBranchBankName = receiverBranchBankName;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
    
}
