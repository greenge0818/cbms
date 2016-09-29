package com.prcsteel.platform.account.model.model;

import java.util.Date;

/**
 * Created by caochao on 2015/7/14.
 */
public class AssignLog {
    private Long id;

    private Long accountId;

    private Date regTime;

    private Long managerExId;

    private String managerExName;

    private Long managerNextId;

    private String managerNextName;

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

    private String accountName;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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
        this.managerExName = managerExName;
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

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getParentRowId() {
        return parentRowId;
    }

    public void setParentRowId(String parentRowId) {
        this.parentRowId = parentRowId;
    }
}
