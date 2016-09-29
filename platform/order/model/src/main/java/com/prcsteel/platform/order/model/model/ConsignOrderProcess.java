package com.prcsteel.platform.order.model.model;

import java.util.Date;

public class ConsignOrderProcess {
    private Integer id;

    private Long orgId;

    private String orgName;

    private Long userId;

    private String userName;

    private Integer type;
    
    private String orderStatusCode;

    private String orderStatusName;

    private Long operatorId;

    private String operatorName;

    private String operatorMobile;

    private String operatorRoleName;

    private Long operatorOrgId;
    
    private String operatorOrgName;
    
    private Date created;

	private String createdBy;

	private Date lastUpdated;

	private String lastUpdatedBy;

	private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    public Date getCreated() {
        return created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Integer getId() {
        return id;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public Integer getModificationNumber() {
        return modificationNumber;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public String getOperatorMobile() {
        return operatorMobile;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public Long getOperatorOrgId() {
		return operatorOrgId;
	}

    public String getOperatorOrgName() {
		return operatorOrgName;
	}

    public String getOperatorRoleName() {
        return operatorRoleName;
    }

    public String getOrderStatusCode() {
        return orderStatusCode;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public Long getOrgId() {
        return orgId;
    }

	public String getOrgName() {
        return orgName;
    }

	public String getParentRowId() {
        return parentRowId;
    }

    public String getRowId() {
        return rowId;
    }

    public Integer getType() {
		return type;
	}

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public void setModificationNumber(Integer modificationNumber) {
        this.modificationNumber = modificationNumber;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public void setOperatorMobile(String operatorMobile) {
        this.operatorMobile = operatorMobile;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public void setOperatorOrgId(Long operatorOrgId) {
		this.operatorOrgId = operatorOrgId;
	}

    public void setOperatorOrgName(String operatorOrgName) {
		this.operatorOrgName = operatorOrgName;
	}

    public void setOperatorRoleName(String operatorRoleName) {
        this.operatorRoleName = operatorRoleName;
    }

    public void setOrderStatusCode(String orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setParentRowId(String parentRowId) {
        this.parentRowId = parentRowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public void setType(Integer type) {
		this.type = type;
	}

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}