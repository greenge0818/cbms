package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class ReportRewardRecord {
    private Long id;

    private Long orderId;

    private Long orderItemsId;

    private Date rewardDate;

    private String categoryGroupUuid;

    private String categoryGroupName;

    private Long buyerManagerId;

    private String buyerManagerName;

    private Long categoryRewartRoleId;

    private BigDecimal categoryRewartRole;

    private Long buyerRewartRoleId;

    private Long sellerRewartRoleId;

    private String status;

    private Boolean isDeleted;

    private Long buyerManagerOrgId;

    private String buyerManagerOrgName;

    private Long sellerManagerId;

    private String sellerManagerName;

    private Long sellerManagerOrgId;

    private String sellerManagerOrgName;

    private String consignType;

    private BigDecimal weight;

    private BigDecimal buyerRewartRole;

    private BigDecimal sellerRewartRole;

    private BigDecimal buyerRewardAmount;

    private BigDecimal sellerRewardAmount;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    public ReportRewardRecord() {
    }

    public ReportRewardRecord(Long buyerManagerId, Date now, Long orderItemsId, Long orderId, String buyerManagerName,
                              String status, Long buyerManagerOrgId, String buyerManagerOrgName, Long sellerManagerId,
                              String sellerManagerName, String sellerManagerOrgName, Long sellerManagerOrgId,
                              String consignType, BigDecimal weight, String user) {
        this.buyerManagerId = buyerManagerId;
        this.rewardDate = now;
        this.orderItemsId = orderItemsId;
        this.orderId = orderId;
        this.buyerManagerName = buyerManagerName;
        this.status = status;
        this.isDeleted = false;
        this.buyerManagerOrgId = buyerManagerOrgId;
        this.buyerManagerOrgName = buyerManagerOrgName;
        this.sellerManagerId = sellerManagerId;
        this.sellerManagerName = sellerManagerName;
        this.sellerManagerOrgName = sellerManagerOrgName;
        this.sellerManagerOrgId = sellerManagerOrgId;
        this.consignType = consignType;
        this.weight = weight;
        this.created = now;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderItemsId() {
        return orderItemsId;
    }

    public void setOrderItemsId(Long orderItemsId) {
        this.orderItemsId = orderItemsId;
    }

    public Date getRewardDate() {
        return rewardDate;
    }

    public void setRewardDate(Date rewardDate) {
        this.rewardDate = rewardDate;
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

    public Long getBuyerManagerId() {
        return buyerManagerId;
    }

    public void setBuyerManagerId(Long buyerManagerId) {
        this.buyerManagerId = buyerManagerId;
    }

    public String getBuyerManagerName() {
        return buyerManagerName;
    }

    public void setBuyerManagerName(String buyerManagerName) {
        this.buyerManagerName = buyerManagerName;
    }

    public Long getCategoryRewartRoleId() {
        return categoryRewartRoleId;
    }

    public void setCategoryRewartRoleId(Long categoryRewartRoleId) {
        this.categoryRewartRoleId = categoryRewartRoleId;
    }

    public BigDecimal getCategoryRewartRole() {
        return categoryRewartRole;
    }

    public void setCategoryRewartRole(BigDecimal categoryRewartRole) {
        this.categoryRewartRole = categoryRewartRole;
    }

    public Long getBuyerRewartRoleId() {
        return buyerRewartRoleId;
    }

    public void setBuyerRewartRoleId(Long buyerRewartRoleId) {
        this.buyerRewartRoleId = buyerRewartRoleId;
    }

    public Long getSellerRewartRoleId() {
        return sellerRewartRoleId;
    }

    public void setSellerRewartRoleId(Long sellerRewartRoleId) {
        this.sellerRewartRoleId = sellerRewartRoleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getBuyerManagerOrgId() {
        return buyerManagerOrgId;
    }

    public void setBuyerManagerOrgId(Long buyerManagerOrgId) {
        this.buyerManagerOrgId = buyerManagerOrgId;
    }

    public String getBuyerManagerOrgName() {
        return buyerManagerOrgName;
    }

    public void setBuyerManagerOrgName(String buyerManagerOrgName) {
        this.buyerManagerOrgName = buyerManagerOrgName;
    }

    public Long getSellerManagerId() {
        return sellerManagerId;
    }

    public void setSellerManagerId(Long sellerManagerId) {
        this.sellerManagerId = sellerManagerId;
    }

    public String getSellerManagerName() {
        return sellerManagerName;
    }

    public void setSellerManagerName(String sellerManagerName) {
        this.sellerManagerName = sellerManagerName;
    }

    public Long getSellerManagerOrgId() {
        return sellerManagerOrgId;
    }

    public void setSellerManagerOrgId(Long sellerManagerOrgId) {
        this.sellerManagerOrgId = sellerManagerOrgId;
    }

    public String getSellerManagerOrgName() {
        return sellerManagerOrgName;
    }

    public void setSellerManagerOrgName(String sellerManagerOrgName) {
        this.sellerManagerOrgName = sellerManagerOrgName;
    }

    public String getConsignType() {
        return consignType;
    }

    public void setConsignType(String consignType) {
        this.consignType = consignType;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getBuyerRewartRole() {
        return buyerRewartRole;
    }

    public void setBuyerRewartRole(BigDecimal buyerRewartRole) {
        this.buyerRewartRole = buyerRewartRole;
    }

    public BigDecimal getSellerRewartRole() {
        return sellerRewartRole;
    }

    public void setSellerRewartRole(BigDecimal sellerRewartRole) {
        this.sellerRewartRole = sellerRewartRole;
    }

    public BigDecimal getBuyerRewardAmount() {
        return buyerRewardAmount;
    }

    public void setBuyerRewardAmount(BigDecimal buyerRewardAmount) {
        this.buyerRewardAmount = buyerRewardAmount;
    }

    public BigDecimal getSellerRewardAmount() {
        return sellerRewardAmount;
    }

    public void setSellerRewardAmount(BigDecimal sellerRewardAmount) {
        this.sellerRewardAmount = sellerRewardAmount;
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