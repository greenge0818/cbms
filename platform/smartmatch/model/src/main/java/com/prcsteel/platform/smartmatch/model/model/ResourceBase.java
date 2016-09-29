package com.prcsteel.platform.smartmatch.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class ResourceBase {
    private Long id;

    private Long custResourceId;

    private String operType;

    private Long accountId;

    private String accountName;

    private String categoryUuid;

    private String categoryName;

    private String materialUuid;

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

    private Integer quantity;

    private String remark;

    private String status;

    private String isException;

    private String sourceType;

    private String userIds;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Date mgtLastUpdated;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustResourceId() {
        return custResourceId;
    }

    public void setCustResourceId(Long custResourceId) {
        this.custResourceId = custResourceId;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType == null ? null : operType.trim();
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
        this.categoryUuid = categoryUuid == null ? null : categoryUuid.trim();
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
        this.materialUuid = materialUuid == null ? null : materialUuid.trim();
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName == null ? null : materialName.trim();
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName == null ? null : factoryName.trim();
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName == null ? null : warehouseName.trim();
    }

    public String getWeightConcept() {
        return weightConcept;
    }

    public void setWeightConcept(String weightConcept) {
        this.weightConcept = weightConcept == null ? null : weightConcept.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getIsException() {
        return isException;
    }

    public void setIsException(String isException) {
        this.isException = isException == null ? null : isException.trim();
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType == null ? null : sourceType.trim();
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds == null ? null : userIds.trim();
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
        this.mgtLastUpdatedBy = mgtLastUpdatedBy == null ? null : mgtLastUpdatedBy.trim();
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
        this.rowId = rowId == null ? null : rowId.trim();
    }

    public String getParentRowId() {
        return parentRowId;
    }

    public void setParentRowId(String parentRowId) {
        this.parentRowId = parentRowId == null ? null : parentRowId.trim();
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1 == null ? null : ext1.trim();
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2 == null ? null : ext2.trim();
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3 == null ? null : ext3.trim();
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
}