package com.prcsteel.platform.order.model.model;

import com.prcsteel.platform.order.model.enums.AcceptDraftStatus;

import java.math.BigDecimal;
import java.util.Date;

public class AcceptDraft {
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

    private BigDecimal remainingAmount;

    private String status;

    private String reason;

    private String statusForShow;

    private Date acceptanceDate;

    private String acceptanceBankCode;

    private String drawerName;

    private String drawerAccountCode;

    private String drawerBankCode;

    private String drawerBankFullName;

    private String receiverName;

    private String receiverAccountCode;

    private String receiverBankFullName;

    private Short adjustDateCount;

    private Byte readTimes;

    private String acceptanceAgreementNumber;

    private Boolean isDifferentBank;

    private Boolean isPayed;

    private Boolean isDeleted;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    public AcceptDraft() {
    }

    public AcceptDraft(Long id, String reason, String status, String lastUpdatedBy, Integer modificationNumber) {
        this.id = id;
        this.status = status;
        this.reason = reason;
        this.lastUpdatedBy = lastUpdatedBy;
        this.modificationNumber = modificationNumber;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAcceptanceBankFullName() {
        return acceptanceBankFullName;
    }

    public void setAcceptanceBankFullName(String acceptanceBankFullName) {
        this.acceptanceBankFullName = acceptanceBankFullName;
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
        this.codeApprove = codeApprove;
    }

    public BigDecimal getAmountApprove() {
        return amountApprove;
    }

    public void setAmountApprove(BigDecimal amountApprove) {
        this.amountApprove = amountApprove;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusForShow = AcceptDraftStatus.getNameByCode(status);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
        this.acceptanceBankCode = acceptanceBankCode;
    }

    public String getDrawerName() {
        return drawerName;
    }

    public void setDrawerName(String drawerName) {
        this.drawerName = drawerName;
    }

    public String getDrawerAccountCode() {
        return drawerAccountCode;
    }

    public void setDrawerAccountCode(String drawerAccountCode) {
        this.drawerAccountCode = drawerAccountCode;
    }

    public String getDrawerBankCode() {
        return drawerBankCode;
    }

    public void setDrawerBankCode(String drawerBankCode) {
        this.drawerBankCode = drawerBankCode;
    }

    public String getDrawerBankFullName() {
        return drawerBankFullName;
    }

    public void setDrawerBankFullName(String drawerBankFullName) {
        this.drawerBankFullName = drawerBankFullName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAccountCode() {
        return receiverAccountCode;
    }

    public void setReceiverAccountCode(String receiverAccountCode) {
        this.receiverAccountCode = receiverAccountCode;
    }

    public String getReceiverBankFullName() {
        return receiverBankFullName;
    }

    public void setReceiverBankFullName(String receiverBankFullName) {
        this.receiverBankFullName = receiverBankFullName;
    }

    public Short getAdjustDateCount() {
        return adjustDateCount;
    }

    public void setAdjustDateCount(Short adjustDateCount) {
        this.adjustDateCount = adjustDateCount;
    }

    public Byte getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(Byte readTimes) {
        this.readTimes = readTimes;
    }

    public String getAcceptanceAgreementNumber() {
        return acceptanceAgreementNumber;
    }

    public void setAcceptanceAgreementNumber(String acceptanceAgreementNumber) {
        this.acceptanceAgreementNumber = acceptanceAgreementNumber;
    }

    public Boolean getIsDifferentBank() {
        return isDifferentBank;
    }

    public void setIsDifferentBank(Boolean isDifferentBank) {
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

    public String getStatusForShow(){
        return this.statusForShow;
    }
}