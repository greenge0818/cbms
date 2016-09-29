package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentOrderDto {
	private Long pid;

	private String code;
	
	private Long consignOrderId;
	
	private String contractCode;
	
	private String buyerName;
	
	private Long requesterId;
	
	private String requesterName;
	
	private Long orgId;
	
	private String orgName;
	
	private BigDecimal totalAmount;
	
	private int printTimes;
	
	private Date created;
	
	private Long itid;
	
	private Long receiverId;
	
	private String receiverName;
	
	private String receiverBankName;
	
	private String receiverBankCity;
	
	private String receiverBranchBankName;
	
	private String receiverAccountCode;
	
	private BigDecimal payAmount;
	
	private Long oid;
	
	private String status;
	
	private String receiverBankCode;

	private String lastPrintIp;
	
	private String printName;
	
	private Date lastPrintDate;
	
	private Long buyerId;
	
	private String payCode;
	
	private Long receiverDepartmentId;
	
	public Long getReceiverDepartmentId() {
		return receiverDepartmentId;
	}

	public void setReceiverDepartmentId(Long receiverDepartmentId) {
		this.receiverDepartmentId = receiverDepartmentId;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
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

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReceiverBankCode() {
		return receiverBankCode;
	}

	public void setReceiverBankCode(String receiverBankCode) {
		this.receiverBankCode = receiverBankCode;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getConsignOrderId() {
		return consignOrderId;
	}

	public void setConsignOrderId(Long consignOrderId) {
		this.consignOrderId = consignOrderId;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
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

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getPrintTimes() {
		return printTimes;
	}

	public void setPrintTimes(int printTimes) {
		this.printTimes = printTimes;
	}

	public Long getItid() {
		return itid;
	}

	public void setItid(Long itid) {
		this.itid = itid;
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

	public String getReceiverAccountCode() {
		return receiverAccountCode;
	}

	public void setReceiverAccountCode(String receiverAccountCode) {
		this.receiverAccountCode = receiverAccountCode;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	public String getLastPrintIp() {
		return lastPrintIp;
	}

	public void setLastPrintIp(String lastPrintIp) {
		this.lastPrintIp = lastPrintIp;
	}
}
