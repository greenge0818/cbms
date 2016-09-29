package com.prcsteel.platform.account.model.model;

import java.util.Date;

public class AccountAssignLogs {
    private Long id;

    private Long accountId;

    private Date regTime;

    private Long managerExId;

    private String managerExName;

    private Long managerNextId;

    private String managerNextName;
    
    private Long orgExId;

    private String orgExName;

    private Long orgNextId;

    private String orgNextName;

    private Long assignerId;

    private String assignerName;

    private String type;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;
    
    public Long getOrgExId() {
		return orgExId;
	}

	public void setOrgExId(Long orgExId) {
		this.orgExId = orgExId;
	}

	public String getOrgExName() {
		return orgExName;
	}

	public void setOrgExName(String orgExName) {
		this.orgExName = orgExName;
	}

	public Long getOrgNextId() {
		return orgNextId;
	}

	public void setOrgNextId(Long orgNextId) {
		this.orgNextId = orgNextId;
	}

	public String getOrgNextName() {
		return orgNextName;
	}

	public void setOrgNextName(String orgNextName) {
		this.orgNextName = orgNextName;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Long getManagerExId() {
        return managerExId;
    }

    public void setManagerExId(Long managerExId) {
        this.managerExId = managerExId;
    }

    public String getManagerExName() {
        return managerExName;
    }

    public void setManagerExName(String managerExName) {
        this.managerExName = managerExName == null ? null : managerExName.trim();
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
        this.managerNextName = managerNextName == null ? null : managerNextName.trim();
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
        this.assignerName = assignerName == null ? null : assignerName.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
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
        this.createdBy = createdBy == null ? null : createdBy.trim();
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
        this.lastUpdatedBy = lastUpdatedBy == null ? null : lastUpdatedBy.trim();
    }

    public Integer getModificationNumber() {
        return modificationNumber;
    }

    public void setModificationNumber(Integer modificationNumber) {
        this.modificationNumber = modificationNumber;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId == null ? null : rowId.trim();
    }

    public String getParentRowId() {
        return parentRowId;
    }

    public void setParentRowId(String parentRowId) {
        this.parentRowId = parentRowId == null ? null : parentRowId.trim();
    }
}