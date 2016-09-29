package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class ReportNewUserReward {
    private Long id;

    private Date addDate;

    private Long managerId;

    private String managerName;

    private Long orgId;

    private String orgName;

    private Date created;

    private Integer active;

    private Boolean isDeleted;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private Long addNewBuyer;

    private Long addNewSeller;

    private BigDecimal sellerRewardAmount;

    private BigDecimal buyerRewardAmount;

    private BigDecimal buyerRewardRole;

    private BigDecimal sellerRewardRole;

    private Date openOrderDate; //开单时间
    
	public ReportNewUserReward() {
    }

    public ReportNewUserReward(Date now, Long userId, String userName, Long orgId, String orgName) {
        this.addDate = now;
        this.managerId = userId;
        this.managerName = userName;
        this.orgId = orgId;
        this.orgName = orgName;
        this.created = now;
        this.isDeleted = false;
        this.active = 0;
        this.createdBy = userName;
        this.lastUpdated = now;
        this.lastUpdatedBy = userName;
        this.modificationNumber = 0;
    }

    public Date getOpenOrderDate() {
		return openOrderDate;
	}

	public void setOpenOrderDate(Date openOrderDate) {
		this.openOrderDate = openOrderDate;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
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

    public Long getAddNewBuyer() {
        return addNewBuyer;
    }

    public void setAddNewBuyer(Long addNewBuyer) {
        this.addNewBuyer = addNewBuyer;
    }

    public Long getAddNewSeller() {
        return addNewSeller;
    }

    public void setAddNewSeller(Long addNewSeller) {
        this.addNewSeller = addNewSeller;
    }

    public BigDecimal getSellerRewardAmount() {
        return sellerRewardAmount;
    }

    public void setSellerRewardAmount(BigDecimal sellerRewardAmount) {
        this.sellerRewardAmount = sellerRewardAmount;
    }

    public BigDecimal getBuyerRewardAmount() {
        return buyerRewardAmount;
    }

    public void setBuyerRewardAmount(BigDecimal buyerRewardAmount) {
        this.buyerRewardAmount = buyerRewardAmount;
    }

    public BigDecimal getBuyerRewardRole() {
        return buyerRewardRole;
    }

    public void setBuyerRewardRole(BigDecimal buyerRewardRole) {
        this.buyerRewardRole = buyerRewardRole;
    }

    public BigDecimal getSellerRewardRole() {
        return sellerRewardRole;
    }

    public void setSellerRewardRole(BigDecimal sellerRewardRole) {
        this.sellerRewardRole = sellerRewardRole;
    }
}