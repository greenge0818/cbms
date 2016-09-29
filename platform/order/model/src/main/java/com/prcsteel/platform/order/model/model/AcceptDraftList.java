package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class AcceptDraftList {
    private Long id;

    private Long accountId;

    private String accountName;

    private Long orgId;

    private String orgName;

    private String code;

    private String acceptanceBankFullName;

    private BigDecimal amount;

    private Date endDate;

    private BigDecimal discountRate;

    private BigDecimal discountRateBase;

    private Date endDateApprove;

    private String codeApprove;

    private BigDecimal amountApprove;

    private String status;

    private String reason;

    private Date acceptanceDate;

    private String acceptanceBankCode;

    private String drawerName;

    private String drawerAccountCode;

    private String drawerBankCode;

    private String drawerBankFullName;

    private String receiverName;

    private String receiverAccountCode;

    private String receiverBankFullName;

    private Integer adjustDateCount;

    private Integer readTimes;

    private String acceptanceAgreementNumber;

    private String isDifferentBank;

    private Boolean isPayed;

    private Boolean isDeleted;

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
        this.orgName = orgName == null ? null : orgName.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getAcceptanceBankFullName() {
        return acceptanceBankFullName;
    }

    public void setAcceptanceBankFullName(String acceptanceBankFullName) {
        this.acceptanceBankFullName = acceptanceBankFullName == null ? null : acceptanceBankFullName.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountRateBase() {
        return discountRateBase;
    }

    public void setDiscountRateBase(BigDecimal discountRateBase) {
        this.discountRateBase = discountRateBase;
    }

    public Date getEndDateApprove() {
        return endDateApprove;
    }

    public void setEndDateApprove(Date endDateApprove) {
        this.endDateApprove = endDateApprove;
    }

    public String getCodeApprove() {
        return codeApprove;
    }

    public void setCodeApprove(String codeApprove) {
        this.codeApprove = codeApprove == null ? null : codeApprove.trim();
    }

    public BigDecimal getAmountApprove() {
        return amountApprove;
    }

    public void setAmountApprove(BigDecimal amountApprove) {
        this.amountApprove = amountApprove;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Date getAcceptanceDate() {
        return acceptanceDate;
    }

    public void setAcceptanceDate(Date acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public String getAcceptanceBankCode() {
        return acceptanceBankCode;
    }

    public void setAcceptanceBankCode(String acceptanceBankCode) {
        this.acceptanceBankCode = acceptanceBankCode == null ? null : acceptanceBankCode.trim();
    }

    public String getDrawerName() {
        return drawerName;
    }

    public void setDrawerName(String drawerName) {
        this.drawerName = drawerName == null ? null : drawerName.trim();
    }

    public String getDrawerAccountCode() {
        return drawerAccountCode;
    }

    public void setDrawerAccountCode(String drawerAccountCode) {
        this.drawerAccountCode = drawerAccountCode == null ? null : drawerAccountCode.trim();
    }

    public String getDrawerBankCode() {
        return drawerBankCode;
    }

    public void setDrawerBankCode(String drawerBankCode) {
        this.drawerBankCode = drawerBankCode == null ? null : drawerBankCode.trim();
    }

    public String getDrawerBankFullName() {
        return drawerBankFullName;
    }

    public void setDrawerBankFullName(String drawerBankFullName) {
        this.drawerBankFullName = drawerBankFullName == null ? null : drawerBankFullName.trim();
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName == null ? null : receiverName.trim();
    }

    public String getReceiverAccountCode() {
        return receiverAccountCode;
    }

    public void setReceiverAccountCode(String receiverAccountCode) {
        this.receiverAccountCode = receiverAccountCode == null ? null : receiverAccountCode.trim();
    }

    public String getReceiverBankFullName() {
        return receiverBankFullName;
    }

    public void setReceiverBankFullName(String receiverBankFullName) {
        this.receiverBankFullName = receiverBankFullName == null ? null : receiverBankFullName.trim();
    }

    public Integer getAdjustDateCount() {
        return adjustDateCount;
    }

    public void setAdjustDateCount(Integer adjustDateCount) {
        this.adjustDateCount = adjustDateCount;
    }

    public Integer getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(Integer readTimes) {
        this.readTimes = readTimes;
    }

    public String getAcceptanceAgreementNumber() {
        return acceptanceAgreementNumber;
    }

    public void setAcceptanceAgreementNumber(String acceptanceAgreementNumber) {
        this.acceptanceAgreementNumber = acceptanceAgreementNumber == null ? null : acceptanceAgreementNumber.trim();
    }

    public String getIsDifferentBank() {
        return isDifferentBank;
    }

    public void setIsDifferentBank(String isDifferentBank) {
        this.isDifferentBank = isDifferentBank;
    }

    public Boolean getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(Boolean isPayed) {
        this.isPayed = isPayed;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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