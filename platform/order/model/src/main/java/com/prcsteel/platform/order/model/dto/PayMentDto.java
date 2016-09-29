package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PayMentDto {
	private Long requsetId;
	
    private String code;
    
    private Long requesterId;
    
    private String requesterName;
    
    private String orgName;
    
    private String accountName;
    
    private String status;
    
    private String type;
    
    private Date created;
    
    private BigDecimal payAmount;
    
    private String cashier;
    
    private Date paymentTime;
    
    private Long payItemId;
    
    private String oldCode;
    
    private int departmentCount;
    
    private String departmentName;
    
    private Long consignOrderId;

	public Long getConsignOrderId() {
		return consignOrderId;
	}

	public void setConsignOrderId(Long consignOrderId) {
		this.consignOrderId = consignOrderId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public int getDepartmentCount() {
		return departmentCount;
	}

	public void setDepartmentCount(int departmentCount) {
		this.departmentCount = departmentCount;
	}

	public String getOldCode() {
		return oldCode;
	}

	public void setOldCode(String oldCode) {
		this.oldCode = oldCode;
	}

	public Long getPayItemId() {
		return payItemId;
	}

	public void setPayItemId(Long payItemId) {
		this.payItemId = payItemId;
	}

	public String getCashier() {
		return cashier;
	}

	public void setCashier(String cashier) {
		this.cashier = cashier;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	public Long getRequsetId() {
		return requsetId;
	}

	public void setRequsetId(Long requsetId) {
		this.requsetId = requsetId;
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

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
    
}
