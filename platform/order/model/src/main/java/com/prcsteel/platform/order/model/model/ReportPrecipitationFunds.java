package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class ReportPrecipitationFunds {
    private Long id;

    private Date calculateDate;

    private BigDecimal totalAccountBalance;

    private BigDecimal creditLimit;

    private BigDecimal precipitationFunds;

    private Byte isDeleted;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Long modificationNumber;

    private String rowId;

    private String parentRowId;

    public ReportPrecipitationFunds(){}

    public ReportPrecipitationFunds(Date calculateDate, BigDecimal totalAccountBalance, BigDecimal creditLimit, BigDecimal precipitationFunds, Date created, String createdBy, String lastUpdatedBy, Date lastUpdated, Long modificationNumber) {
        this.calculateDate = calculateDate;
        this.totalAccountBalance = totalAccountBalance;
        this.creditLimit = creditLimit;
        this.precipitationFunds = precipitationFunds;
        this.created = created;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdated = lastUpdated;
        this.modificationNumber = modificationNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCalculateDate() {
        return calculateDate;
    }

    public void setCalculateDate(Date calculateDate) {
        this.calculateDate = calculateDate;
    }

    public BigDecimal getTotalAccountBalance() {
        return totalAccountBalance;
    }

    public void setTotalAccountBalance(BigDecimal totalAccountBalance) {
        this.totalAccountBalance = totalAccountBalance;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getPrecipitationFunds() {
        return precipitationFunds;
    }

    public void setPrecipitationFunds(BigDecimal precipitationFunds) {
        this.precipitationFunds = precipitationFunds;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
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

    public Long getModificationNumber() {
        return modificationNumber;
    }

    public void setModificationNumber(Long modificationNumber) {
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