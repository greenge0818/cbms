package com.prcsteel.platform.smartmatch.model.model;

import java.util.Date;
/**
 * 基价设置数据对象
 * 
 * @author caosulin
 *
 */
public class CustBasePriceRelation {
    private Long id;

    private Long accountId;

    private String accountName;

    private Long basePriceId;
    
    private String basePriceName;

    private Long cityId;

    private String cityName;

    private String categoryUuid;
    
    private String categoryName;

    private Long factoryId;
    
    private String factoryName;
    
    private Integer isEnable;//是否启用，1启用，0禁用

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;
    
    private Integer baseRelationDetailCount;//关联条数

    public String getBasePriceName() {
		return basePriceName;
	}

	public void setBasePriceName(String basePriceName) {
		this.basePriceName = basePriceName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
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
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public Long getBasePriceId() {
        return basePriceId;
    }

    public void setBasePriceId(Long basePriceId) {
        this.basePriceId = basePriceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid == null ? null : categoryUuid.trim();
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
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

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public Integer getBaseRelationDetailCount() {
		return baseRelationDetailCount;
	}

	public void setBaseRelationDetailCount(Integer baseRelationDetailCount) {
		this.baseRelationDetailCount = baseRelationDetailCount;
	}
}