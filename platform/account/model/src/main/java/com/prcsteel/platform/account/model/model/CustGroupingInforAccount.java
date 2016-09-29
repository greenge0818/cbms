package com.prcsteel.platform.account.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class CustGroupingInforAccount {
    private Long id;

    private Long groupingInforId;

    private Long accountId;

    private String accountName;

    private Long departmentId;

    private String departmentName;

    private Integer isAutoSecondPayment;

    private Integer isAutoSecondPaymentAudit;

    private String serial;

    private BigDecimal creditLimit;

    private BigDecimal creditLimitAudit;

    private BigDecimal creditUsed;

    private BigDecimal creditBalance;

    private String status;

    private String owerType;

    private Long userId;

    private String userName;

    private Boolean isDelete;

    private String note;

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

    public Long getGroupingInforId() {
        return groupingInforId;
    }

    public void setGroupingInforId(Long groupingInforId) {
        this.groupingInforId = groupingInforId;
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

    public Integer getIsAutoSecondPayment() {
		return isAutoSecondPayment;
	}

	public void setIsAutoSecondPayment(Integer isAutoSecondPayment) {
		this.isAutoSecondPayment = isAutoSecondPayment;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial == null ? null : serial.trim();
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditLimitAudit() {
        return creditLimitAudit;
    }

    public void setCreditLimitAudit(BigDecimal creditLimitAudit) {
        this.creditLimitAudit = creditLimitAudit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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

  
	public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
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

    public BigDecimal getCreditUsed() {
        return creditUsed;
    }

    public void setCreditUsed(BigDecimal creditUsed) {
        this.creditUsed = creditUsed;
    }

    public BigDecimal getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(BigDecimal creditBalance) {
        this.creditBalance = creditBalance;
    }

    public String getOwerType() {
        return owerType;
    }

    public void setOwerType(String owerType) {
        this.owerType = owerType;
    }

    public Integer getIsAutoSecondPaymentAudit() {
        return isAutoSecondPaymentAudit;
    }

    public void setIsAutoSecondPaymentAudit(Integer isAutoSecondPaymentAudit) {
        this.isAutoSecondPaymentAudit = isAutoSecondPaymentAudit;
    }
}