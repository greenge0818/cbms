package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class ReportRebateRecord {
    private Long id;

    private Date rebateTime;

    private Long accountId;

    private String accountName;

    private Long orgId;

    private String orgName;

    private Long managerId;

    private String managerName;

    private Long contactId;

    private String contactName;

    private String categoryGroupUuid;

    private String categoryGroupName;

    private BigDecimal weight;

    private BigDecimal amount;

    private BigDecimal rebateAmount;

    private BigDecimal rebateBalance;

    private String code;

    private Date created;

    private Boolean isDeleted;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    public ReportRebateRecord() {
    }

    public ReportRebateRecord(Date now, Long accountId, String accountName, Long orgId, String orgName, Long managerId, String managerName,
                              Long contactId, String contactName, String categoryGroupUuid, String categoryGroupName, BigDecimal weight,
                              BigDecimal amount, String code, String user) {
        this.rebateTime = now;
        this.accountId = accountId;
        this.accountName = accountName;
        this.orgId = orgId;
        this.orgName = orgName;
        this.managerId = managerId;
        this.managerName = managerName;
        this.contactId = contactId;
        this.contactName = contactName;
        this.categoryGroupUuid = categoryGroupUuid;
        this.categoryGroupName = categoryGroupName;
        this.weight = weight;
        this.amount = amount;
        this.code = code;
        this.created = now;
        this.isDeleted = false;
        this.createdBy = user;
        this.lastUpdated = now;
        this.lastUpdatedBy = user;
        this.modificationNumber = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getRebateTime() {
        return rebateTime;
    }

    public void setRebateTime(Date rebateTime) {
        this.rebateTime = rebateTime;
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
        this.accountName = accountName;
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

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCategoryGroupUuid() {
        return categoryGroupUuid;
    }

    public void setCategoryGroupUuid(String categoryGroupUuid) {
        this.categoryGroupUuid = categoryGroupUuid;
    }

    public String getCategoryGroupName() {
        return categoryGroupName;
    }

    public void setCategoryGroupName(String categoryGroupName) {
        this.categoryGroupName = categoryGroupName;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRebateAmount() {
        return rebateAmount;
    }

    public void setRebateAmount(BigDecimal rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public BigDecimal getRebateBalance() {
        return rebateBalance;
    }

    public void setRebateBalance(BigDecimal rebateBalance) {
        this.rebateBalance = rebateBalance;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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
}