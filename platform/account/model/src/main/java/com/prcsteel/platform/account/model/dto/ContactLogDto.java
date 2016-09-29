package com.prcsteel.platform.account.model.dto;

import java.util.Date;
import java.util.List;

public class ContactLogDto {
	
    private List<String> accountIds;

    private Long managerNextId;

    private String managerNextName;

    private Long assignerId;

    private String assignerName;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

	public List<String> getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(List<String> accountIds) {
		this.accountIds = accountIds;
	}

	public Long getManagerNextId() {
		return managerNextId;
	}

	public void setManagerNextId(Long managerNextId) {
		this.managerNextId = managerNextId;
	}

	public String getManagerNextName() {
		return managerNextName;
	}

	public void setManagerNextName(String managerNextName) {
		this.managerNextName = managerNextName;
	}

	public Long getAssignerId() {
		return assignerId;
	}

	public void setAssignerId(Long assignerId) {
		this.assignerId = assignerId;
	}

	public String getAssignerName() {
		return assignerName;
	}

	public void setAssignerName(String assignerName) {
		this.assignerName = assignerName;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

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

	public Integer getModificationNumber() {
		return modificationNumber;
	}

	public void setModificationNumber(Integer modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

}
