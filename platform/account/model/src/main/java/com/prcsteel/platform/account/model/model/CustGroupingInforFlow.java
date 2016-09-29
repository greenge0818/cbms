package com.prcsteel.platform.account.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class CustGroupingInforFlow {

    private Long id;
    private String owerType;

    private Long groupingInforId;

    private String groupingInforName;

    private String groupingInforNameBefore;

    private String groupingInforNameAfter;

    private Long accountId;

    private String accountName;

    private Long departmentId;

    private String departmentName;

    private Long userId;

    private String userName;
    private String serial;

    private BigDecimal beforeLimit;

    private BigDecimal afterLimit;

    private BigDecimal finalLimit;

    private String groupingInforStatus;

    private String remark;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwerType() {
        return owerType;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setOwerType(String owerType) {
        this.owerType = owerType == null ? null : owerType.trim();
    }

    public Long getGroupingInforId() {
        return groupingInforId;
    }

    public void setGroupingInforId(Long groupingInforId) {
        this.groupingInforId = groupingInforId;
    }

    public String getGroupingInforName() {
        return groupingInforName;
    }

    public void setGroupingInforName(String groupingInforName) {
        this.groupingInforName = groupingInforName == null ? null : groupingInforName.trim();
    }

    public String getGroupingInforNameBefore() {
        return groupingInforNameBefore;
    }

    public void setGroupingInforNameBefore(String groupingInforNameBefore) {
        this.groupingInforNameBefore = groupingInforNameBefore == null ? null : groupingInforNameBefore.trim();
    }

    public String getGroupingInforNameAfter() {
        return groupingInforNameAfter;
    }

    public void setGroupingInforNameAfter(String groupingInforNameAfter) {
        this.groupingInforNameAfter = groupingInforNameAfter == null ? null : groupingInforNameAfter.trim();
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName == null ? null : departmentName.trim();
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
        this.userName = userName == null ? null : userName.trim();
    }

    public BigDecimal getBeforeLimit() {
        return beforeLimit;
    }

    public void setBeforeLimit(BigDecimal beforeLimit) {
        this.beforeLimit = beforeLimit;
    }

    public BigDecimal getAfterLimit() {
        return afterLimit;
    }

    public void setAfterLimit(BigDecimal afterLimit) {
        this.afterLimit = afterLimit;
    }

    public BigDecimal getFinalLimit() {
        return finalLimit;
    }

    public void setFinalLimit(BigDecimal finalLimit) {
        this.finalLimit = finalLimit;
    }

    public String getGroupingInforStatus() {
        return groupingInforStatus;
    }

    public void setGroupingInforStatus(String groupingInforStatus) {
        this.groupingInforStatus = groupingInforStatus == null ? null : groupingInforStatus.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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