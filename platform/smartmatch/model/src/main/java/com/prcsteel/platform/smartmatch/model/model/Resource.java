package com.prcsteel.platform.smartmatch.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class Resource {
	private Long id;

	private Long accountId;

	private String accountName;

	private String categoryUuid;

	private String categoryName;

	private String materialUuid;
	/**
	 * 材质名称
	 */
	private String materialName;

	private Long factoryId;

	private String factoryName;

	private BigDecimal weight;
	private Long cityId;
	private String cityName;

	private Long warehouseId;

	private String warehouseName;

	private String weightConcept;

	private BigDecimal price;
	/**
	 * 价格涨跌
	 */
	private BigDecimal priceChange;
	private Integer quantity;

	private String remark;

	private String status;
	// add by caosulin@prcsteel.com 2016.6.2增加异常
	/**** 异常状态，exception 异常，normal 正常 *****/
	private String isException;

	private String sourceType;

	private String userIds;

	private Date created;

	private String createdBy;

	/**
	 * 资源管理更新时间 ， 导入资源，资源管理模块更新资时，更新时间设置
	 */
	private Date mgtLastUpdated;

	/**
	 * 资源管理更新人
	 */
	private String mgtLastUpdatedBy;

	private Integer modificationNumber;

	private String rowId;

	private String parentRowId;

	private String ext1;

	private String ext2;

	private String ext3;

	private Integer ext4;

	private Integer ext5;

	private Integer ext6;

	private Date ext7;

	private Long ext8;

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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
		this.accountName = accountName;
	}

	public String getCategoryUuid() {
		return categoryUuid;
	}

	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getMaterialUuid() {
		return materialUuid;
	}

	public void setMaterialUuid(String materialUuid) {
		this.materialUuid = materialUuid;
	}

	public Long getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWeightConcept() {
		return weightConcept;
	}

	public void setWeightConcept(String weightConcept) {
		this.weightConcept = weightConcept;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Date getMgtLastUpdated() {
		return mgtLastUpdated;
	}

	public void setMgtLastUpdated(Date mgtLastUpdated) {
		this.mgtLastUpdated = mgtLastUpdated;
	}

	public String getMgtLastUpdatedBy() {
		return mgtLastUpdatedBy;
	}

	public void setMgtLastUpdatedBy(String mgtLastUpdatedBy) {
		this.mgtLastUpdatedBy = mgtLastUpdatedBy;
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

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public Integer getExt4() {
		return ext4;
	}

	public void setExt4(Integer ext4) {
		this.ext4 = ext4;
	}

	public Integer getExt5() {
		return ext5;
	}

	public void setExt5(Integer ext5) {
		this.ext5 = ext5;
	}

	public Integer getExt6() {
		return ext6;
	}

	public void setExt6(Integer ext6) {
		this.ext6 = ext6;
	}

	public Date getExt7() {
		return ext7;
	}

	public void setExt7(Date ext7) {
		this.ext7 = ext7;
	}

	public Long getExt8() {
		return ext8;
	}

	public void setExt8(Long ext8) {
		this.ext8 = ext8;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public BigDecimal getPriceChange() {
		return priceChange;
	}

	public void setPriceChange(BigDecimal priceChange) {
		this.priceChange = priceChange;
	}

	public String getIsException() {
		return isException;
	}

	public void setIsException(String isException) {
		this.isException = isException;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Resource() {
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Resource(Long accountId, String accountName, String categoryUuid, String categoryName, String materialUuid, Long factoryId, Long warehouseId,
			String weightConcept, BigDecimal price, String createdBy, String lastUpdatedBy, Integer quantity,
			BigDecimal weight, String remark, String sourceType, String status, String userIds, String factoryName,
			String warehouseName, String mgtLastUpdatedBy, Date mgtLastUpdated) {
		this.accountId = accountId;
		this.accountName = accountName;
		this.categoryUuid = categoryUuid;
		this.categoryName = categoryName;
		this.materialUuid = materialUuid;
		this.factoryId = factoryId;
		this.warehouseId = warehouseId;
		this.weightConcept = weightConcept;
		this.price = price;
		this.createdBy = createdBy;

		this.quantity = quantity;
		this.weight = weight;
		this.remark = remark;
		this.sourceType = sourceType;
		this.status = status;
		this.userIds = userIds;
		this.factoryName = factoryName;
		this.warehouseName = warehouseName;
		this.mgtLastUpdatedBy = mgtLastUpdatedBy;
		this.mgtLastUpdated = mgtLastUpdated;
	}

}