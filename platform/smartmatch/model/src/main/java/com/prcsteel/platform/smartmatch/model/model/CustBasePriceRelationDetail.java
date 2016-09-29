package com.prcsteel.platform.smartmatch.model.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 基价关联设置明细数据对象
 * @author caosulin
 *
 */
public class CustBasePriceRelationDetail {
    private Long id;

    private Long basePriceRelationId;

    private String materialUuid;
    
    private String materialName;

    private String spec1;

    private String spec2;

    private String spec3;

    private BigDecimal priceDeviation;

    private Boolean isDeficiencyInventory;

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

    public Long getBasePriceRelationId() {
        return basePriceRelationId;
    }

    public void setBasePriceRelationId(Long basePriceRelationId) {
        this.basePriceRelationId = basePriceRelationId;
    }

    public String getMaterialUuid() {
        return materialUuid;
    }

    public void setMaterialUuid(String materialUuid) {
        this.materialUuid = materialUuid == null ? null : materialUuid.trim();
    }
    
    public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getSpec1() {
        return spec1;
    }

    public void setSpec1(String spec1) {
        this.spec1 = spec1 == null ? null : spec1.trim();
    }

    public String getSpec2() {
        return spec2;
    }

    public void setSpec2(String spec2) {
        this.spec2 = spec2 == null ? null : spec2.trim();
    }

    public String getSpec3() {
        return spec3;
    }

    public void setSpec3(String spec3) {
        this.spec3 = spec3 == null ? null : spec3.trim();
    }

    public BigDecimal getPriceDeviation() {
		return priceDeviation;
	}

	public void setPriceDeviation(BigDecimal priceDeviation) {
		this.priceDeviation = priceDeviation;
	}

	public Boolean getIsDeficiencyInventory() {
        return isDeficiencyInventory;
    }

    public void setIsDeficiencyInventory(Boolean isDeficiencyInventory) {
        this.isDeficiencyInventory = isDeficiencyInventory;
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
}