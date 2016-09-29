package com.prcsteel.platform.order.model.query;

import java.util.Date;

import com.prcsteel.platform.acl.model.model.User;

public class AllowanceUpdate {
	
	private Long id;
	
	private Date auditDate;
	
	private Integer modificationNumber;
	
	private Long allowanceId;
	
	private String status;
	
	private String remark;
	
	private String buyerAllowanceIds;
	
	private String state;
	
	private User user;

	private String allowanceType;
	
	private Date lastUpdated;
	
	private String lastUpdatedBy;
	
	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Long getAllowanceId() {
		return allowanceId;
	}

	public void setAllowanceId(Long allowanceId) {
		this.allowanceId = allowanceId;
	}

	public String getBuyerAllowanceIds() {
		return buyerAllowanceIds;
	}

	public void setBuyerAllowanceIds(String buyerAllowanceIds) {
		this.buyerAllowanceIds = buyerAllowanceIds;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAllowanceType() {
		return allowanceType;
	}

	public void setAllowanceType(String allowanceType) {
		this.allowanceType = allowanceType;
	}

	public Integer getModificationNumber() {
		return modificationNumber;
	}

	public void setModificationNumber(Integer modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

}
