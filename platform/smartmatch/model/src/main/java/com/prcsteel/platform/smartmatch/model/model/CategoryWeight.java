package com.prcsteel.platform.smartmatch.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class CategoryWeight {
    private Long id;

    private Long factoryId;

    private String categoryUuid;

    private String materialUuid;

    private String normsUuid;

    private String normsName;

    private BigDecimal singleWeight;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getCategoryUuid() {
		return categoryUuid;
	}

	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}

	public String getMaterialUuid() {
        return materialUuid;
    }

    public void setMaterialUuid(String materialUuid) {
        this.materialUuid = materialUuid;
    }

    public String getNormsUuid() {
        return normsUuid;
    }

    public void setNormsUuid(String normsUuid) {
        this.normsUuid = normsUuid;
    }

    public String getNormsName() {
        return normsName;
    }

    public void setNormsName(String normsName) {
        this.normsName = normsName;
    }

    public BigDecimal getSingleWeight() {
        return singleWeight;
    }

    public void setSingleWeight(BigDecimal singleWeight) {
        this.singleWeight = singleWeight;
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
}