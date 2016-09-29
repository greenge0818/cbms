package com.prcsteel.platform.order.model.model;

import java.util.Date;

public class InvoiceInProcess {
    private Integer id;

    private Long orgId;

    private String orgName;

    private Long userId;

    private String userName;

    private String invoiceInStatus;

    private String invoiceInStatusName;

    private Long operatorId;

    private String operatorName;

    private String operatorMobile;

    private String operatorRoleName;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInvoiceInStatus() {
        return invoiceInStatus;
    }

    public void setInvoiceInStatus(String invoiceInStatus) {
        this.invoiceInStatus = invoiceInStatus;
    }

    public String getInvoiceInStatusName() {
        return invoiceInStatusName;
    }

    public void setInvoiceInStatusName(String invoiceInStatusName) {
        this.invoiceInStatusName = invoiceInStatusName;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorMobile() {
        return operatorMobile;
    }

    public void setOperatorMobile(String operatorMobile) {
        this.operatorMobile = operatorMobile;
    }

    public String getOperatorRoleName() {
        return operatorRoleName;
    }

    public void setOperatorRoleName(String operatorRoleName) {
        this.operatorRoleName = operatorRoleName;
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