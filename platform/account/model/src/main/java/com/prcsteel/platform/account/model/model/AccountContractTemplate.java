package com.prcsteel.platform.account.model.model;

import java.io.Serializable;
import java.util.Date;

public class AccountContractTemplate implements Serializable {

    private static final long serialVersionUID = -7464264279004680444L;

    private Long id;

    private Long accountId;

    private String name;

    private String thumbnailUrl;

    private String type;

    private Integer enabled;

    private String status;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    private String content;

    private String preContent;

    private Integer sysTemplateStatus;

    private Integer renewAfterExpriration;//到期后续签

    private String statusDeclineReason;//合同模板审核未通过原因

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPreContent() {
        return preContent;
    }

    public void setPreContent(String preContent) {
        this.preContent = preContent;
    }

    public Integer getSysTemplateStatus() {
        return sysTemplateStatus;
    }

    public void setSysTemplateStatus(Integer sysTemplateStatus) {
        this.sysTemplateStatus = sysTemplateStatus;
    }

    public Integer getRenewAfterExpriration() {
        return renewAfterExpriration;
    }

    public void setRenewAfterExpriration(Integer renewAfterExpriration) {
        this.renewAfterExpriration = renewAfterExpriration;
    }

    public String getStatusDeclineReason() {
        return statusDeclineReason;
    }

    public void setStatusDeclineReason(String statusDeclineReason) {
        this.statusDeclineReason = statusDeclineReason;
    }
}