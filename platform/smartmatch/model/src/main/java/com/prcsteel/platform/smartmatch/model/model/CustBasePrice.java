package com.prcsteel.platform.smartmatch.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class CustBasePrice {
    private Long id;

    private String basePriceName;
    
    // add by caosulin 20160822基价类别
    private String basePriceType;

    private Long cityId;

    private String city;

    private String materialUuid;

    private String material;

    private String categoryUuid;

    private String category;

    private String spec;

    private Long factoryId;

    private String factory;

    private BigDecimal price;

    private String remark;

    private Long userId;

    private String userName;

    private Date created;

    private Date lastUpdated;

    private Date baseLastUpdated;

    private String baseLastUpdateBy;

    private Boolean isDeleted;
    
    private Boolean release;
    
    public Boolean getRelease() {
		return release;
	}

	public void setRelease(Boolean release) {
		this.release = release;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBasePriceName() {
        return basePriceName;
    }

    public void setBasePriceName(String basePriceName) {
        this.basePriceName = basePriceName == null ? null : basePriceName.trim();
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getMaterialUuid() {
        return materialUuid;
    }

    public void setMaterialUuid(String materialUuid) {
        this.materialUuid = materialUuid == null ? null : materialUuid.trim();
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material == null ? null : material.trim();
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid == null ? null : categoryUuid.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec == null ? null : spec.trim();
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory == null ? null : factory.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getBaseLastUpdated() {
        return baseLastUpdated;
    }

    public void setBaseLastUpdated(Date baseLastUpdated) {
        this.baseLastUpdated = baseLastUpdated;
    }

    public String getBaseLastUpdateBy() {
        return baseLastUpdateBy;
    }

    public void setBaseLastUpdateBy(String baseLastUpdateBy) {
        this.baseLastUpdateBy = baseLastUpdateBy == null ? null : baseLastUpdateBy.trim();
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public void setBasePriceType(String basePriceType) {
		this.basePriceType = basePriceType;
	}
    
    public String getBasePriceType() {
		return basePriceType;
	}
    
}