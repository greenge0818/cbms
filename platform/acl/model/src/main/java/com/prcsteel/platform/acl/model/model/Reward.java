package com.prcsteel.platform.acl.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class Reward {
    private Integer id;
    
    private Integer orgId;//服务中心id
    
    private String orgName;//服务中心名称

    private String rewardType;

    private String categoryUuid;

    private BigDecimal sellerLimit;

    private BigDecimal rewardRole;

   private BigDecimal buyRadio;//买家采购量系数
   
   private BigDecimal singleTradeWeight;//单笔交易吨位上限

   private BigDecimal allTradeQuality;//总交易笔数上限

  private BigDecimal allTradeWeight;//总交易吨位上限

    private Integer seq; // 序号

    private String rewardStatus;

    private Date effectiveTime;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    public Reward() {
    }
    public Reward(String rewardType, String categoryUuid, BigDecimal sellerLimit, BigDecimal rewardRole, BigDecimal buyRadio, Integer seq) {
        this.rewardType = rewardType;
        this.categoryUuid = categoryUuid;
        this.sellerLimit = sellerLimit;
        this.rewardRole = rewardRole;
        this.buyRadio = buyRadio;
        this.seq = seq;
    }
    
    

    public Reward(Integer orgId, String orgName, String rewardType, String categoryUuid, BigDecimal sellerLimit,
			BigDecimal rewardRole, BigDecimal buyRadio, BigDecimal singleTradeWeight, BigDecimal allTradeQuality,
			BigDecimal allTradeWeight, Integer seq) {
		super();
		this.orgId = orgId;
		this.orgName = orgName;
		this.rewardType = rewardType;
		this.categoryUuid = categoryUuid;
		this.sellerLimit = sellerLimit;
		this.rewardRole = rewardRole;
		this.buyRadio = buyRadio;
		this.singleTradeWeight = singleTradeWeight;
		this.allTradeQuality = allTradeQuality;
		this.allTradeWeight = allTradeWeight;
		this.seq = seq;
	}
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public BigDecimal getSellerLimit() {
        return sellerLimit;
    }

    public void setSellerLimit(BigDecimal sellerLimit) {
        this.sellerLimit = sellerLimit;
    }

    public BigDecimal getRewardRole() {
        return rewardRole;
    }

    public void setRewardRole(BigDecimal rewardRole) {
        this.rewardRole = rewardRole;
    }

    public String getRewardStatus() {
        return rewardStatus;
    }

    public void setRewardStatus(String rewardStatus) {
        this.rewardStatus = rewardStatus;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
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

    public BigDecimal getBuyRadio() {
        return buyRadio;
    }

    public void setBuyRadio(BigDecimal buyRadio) {
        this.buyRadio = buyRadio;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public BigDecimal getSingleTradeWeight() {
		return singleTradeWeight;
	}
	public void setSingleTradeWeight(BigDecimal singleTradeWeight) {
		this.singleTradeWeight = singleTradeWeight;
	}
	public BigDecimal getAllTradeQuality() {
		return allTradeQuality;
	}
	public void setAllTradeQuality(BigDecimal allTradeQuality) {
		this.allTradeQuality = allTradeQuality;
	}
	public BigDecimal getAllTradeWeight() {
		return allTradeWeight;
	}
	public void setAllTradeWeight(BigDecimal allTradeWeight) {
		this.allTradeWeight = allTradeWeight;
	}
    
}