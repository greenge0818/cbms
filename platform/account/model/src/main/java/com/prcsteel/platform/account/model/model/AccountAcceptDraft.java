package com.prcsteel.platform.account.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class AccountAcceptDraft {
    private Long id;

    private Long accountId;

    private Long acceptDraftId;

    private BigDecimal amount;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    public AccountAcceptDraft() {

    }

    public AccountAcceptDraft(Long accountId, Long acceptDraftId, BigDecimal amount) {
        this.accountId = accountId;
        this.acceptDraftId = acceptDraftId;
        this.amount = amount;
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

    public Long getAcceptDraftId() {
        return acceptDraftId;
    }

    public void setAcceptDraftId(Long acceptDraftId) {
        this.acceptDraftId = acceptDraftId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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