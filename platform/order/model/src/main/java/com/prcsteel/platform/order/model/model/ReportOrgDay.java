package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class ReportOrgDay {
    private Long id;

    private Long orgId;

    private String orgName;

    private Date calculateDate;

    private BigDecimal reletedTotalSaleAmount;

    private BigDecimal reletedTotalPurchaseAmount;

    private BigDecimal reletedTotalWeight;

    private Long reletedTotalOrder;

    private Long reletedTotalConsignOrder;

    private BigDecimal secondTotalSaleAmount;

    private BigDecimal secondTotalPurchaseAmount;

    private BigDecimal secondTotalWeight;

    private Long secondTotalOrder;

    private Long secondTotalConsignOrder;

    private Long totalConsignSellerAccount;

    private String remark;

    private Byte isDeleted;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Long modificationNumber;

    private String rowId;

    private String parentRowId;

    public ReportOrgDay(){}

    public ReportOrgDay(Long orgId, String orgName, Date calculateDate, Date created, String createdBy, Date lastUpdated, String lastUpdatedBy, Long modificationNumber,String remark) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.calculateDate = calculateDate;
        this.created = created;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.modificationNumber = modificationNumber;
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Date getCalculateDate() {
        return calculateDate;
    }

    public void setCalculateDate(Date calculateDate) {
        this.calculateDate = calculateDate;
    }

    public BigDecimal getReletedTotalSaleAmount() {
        return reletedTotalSaleAmount;
    }

    public void setReletedTotalSaleAmount(BigDecimal reletedTotalSaleAmount) {
        this.reletedTotalSaleAmount = reletedTotalSaleAmount;
    }

    public BigDecimal getReletedTotalPurchaseAmount() {
        return reletedTotalPurchaseAmount;
    }

    public void setReletedTotalPurchaseAmount(BigDecimal reletedTotalPurchaseAmount) {
        this.reletedTotalPurchaseAmount = reletedTotalPurchaseAmount;
    }

    public BigDecimal getReletedTotalWeight() {
        return reletedTotalWeight;
    }

    public void setReletedTotalWeight(BigDecimal reletedTotalWeight) {
        this.reletedTotalWeight = reletedTotalWeight;
    }

    public Long getReletedTotalOrder() {
        return reletedTotalOrder;
    }

    public void setReletedTotalOrder(Long reletedTotalOrder) {
        this.reletedTotalOrder = reletedTotalOrder;
    }

    public Long getReletedTotalConsignOrder() {
        return reletedTotalConsignOrder;
    }

    public void setReletedTotalConsignOrder(Long reletedTotalConsignOrder) {
        this.reletedTotalConsignOrder = reletedTotalConsignOrder;
    }

    public BigDecimal getSecondTotalSaleAmount() {
        return secondTotalSaleAmount;
    }

    public void setSecondTotalSaleAmount(BigDecimal secondTotalSaleAmount) {
        this.secondTotalSaleAmount = secondTotalSaleAmount;
    }

    public BigDecimal getSecondTotalPurchaseAmount() {
        return secondTotalPurchaseAmount;
    }

    public void setSecondTotalPurchaseAmount(BigDecimal secondTotalPurchaseAmount) {
        this.secondTotalPurchaseAmount = secondTotalPurchaseAmount;
    }

    public BigDecimal getSecondTotalWeight() {
        return secondTotalWeight;
    }

    public void setSecondTotalWeight(BigDecimal secondTotalWeight) {
        this.secondTotalWeight = secondTotalWeight;
    }

    public Long getSecondTotalOrder() {
        return secondTotalOrder;
    }

    public void setSecondTotalOrder(Long secondTotalOrder) {
        this.secondTotalOrder = secondTotalOrder;
    }

    public Long getSecondTotalConsignOrder() {
        return secondTotalConsignOrder;
    }

    public void setSecondTotalConsignOrder(Long secondTotalConsignOrder) {
        this.secondTotalConsignOrder = secondTotalConsignOrder;
    }

    public Long getTotalConsignSellerAccount() {
        return totalConsignSellerAccount;
    }

    public void setTotalConsignSellerAccount(Long totalConsignSellerAccount) {
        this.totalConsignSellerAccount = totalConsignSellerAccount;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}