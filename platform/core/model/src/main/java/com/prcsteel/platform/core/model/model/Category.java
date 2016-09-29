package com.prcsteel.platform.core.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class Category {
	private Integer id;

	private String uuid;

	private String name;

	/**
	 * 区域名称
	 */

	private String aliasName;
	private Integer priority;

	private Integer varietyQuantity;

	private BigDecimal priceDeviation;

	private Boolean isDeleted;

	/**
	 * ec 设置前台是否显示
	 */

	private String isEcShow;

	private BigDecimal priceMin;

	private BigDecimal priceMax;

	private Date created;

	private String createdBy;

	private Date lastUpdated;

	private String lastUpdatedBy;

	private Integer modificationNumber;

	private String rowId;

	private String parentRowId;

	public Integer getVarietyQuantity() {
		return varietyQuantity;
	}

	public void setVarietyQuantity(Integer varietyQuantity) {
		this.varietyQuantity = varietyQuantity;
	}

	public BigDecimal getPriceDeviation() {
		return priceDeviation;
	}

	public void setPriceDeviation(BigDecimal priceDeviation) {
		this.priceDeviation = priceDeviation;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
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

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getIsEcShow() {
		return isEcShow;
	}

	public void setIsEcShow(String isEcShow) {
		this.isEcShow = isEcShow;
	}

	public BigDecimal getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(BigDecimal priceMin) {
		this.priceMin = priceMin;
	}

	public BigDecimal getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(BigDecimal priceMax) {
		this.priceMax = priceMax;
	}
}