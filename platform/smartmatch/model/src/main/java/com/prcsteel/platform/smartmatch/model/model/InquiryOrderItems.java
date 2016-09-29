package com.prcsteel.platform.smartmatch.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class InquiryOrderItems {
    private Long id;

    private Long purchaseOrderItemsId;

    private Long inquiryOrderSellerId;
    
    private Long resourceId;

    private String categoryUuid;
    
    private String categoryName;

    private String materialUuid;
    
    private String materialName;

    private String spec;

    private Long factoryId;
    
    private String factoryName;

    private Long warehouseId;
    
    private String warehouseName;
    
    private String abnormalWarehouse;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal weight;
    
    private String weightConcept;

    private String remark;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

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

	public Long getPurchaseOrderItemsId() {
		return purchaseOrderItemsId;
	}

	public void setPurchaseOrderItemsId(Long purchaseOrderItemsId) {
		this.purchaseOrderItemsId = purchaseOrderItemsId;
	}

	public Long getInquiryOrderSellerId() {
		return inquiryOrderSellerId;
	}

	public void setInquiryOrderSellerId(Long inquiryOrderSellerId) {
		this.inquiryOrderSellerId = inquiryOrderSellerId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
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

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Long getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getAbnormalWarehouse() {
		return abnormalWarehouse;
	}

	public void setAbnormalWarehouse(String abnormalWarehouse) {
		this.abnormalWarehouse = abnormalWarehouse;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
	
	public String getWeightConcept() {
		return weightConcept;
	}

	public void setWeightConcept(String weightConcept) {
		this.weightConcept = weightConcept;
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
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

	public InquiryOrderItems(){}
	
	public InquiryOrderItems(Long purchaseOrderItemsId,Long inquiryOrderSellerId,String categoryUuid,
			String materialUuid,String spec,Long factoryId,Long warehouseId,String abnormalWarehouse,Integer quantity,
			BigDecimal price,BigDecimal weight,String createdBy,String lastUpdatedBy,String weightConcept,Long resourceId){
		
		this.purchaseOrderItemsId = purchaseOrderItemsId;
		this.inquiryOrderSellerId = inquiryOrderSellerId;
		this.categoryUuid = categoryUuid;
		this.materialUuid = materialUuid;
		this.spec = spec;
		this.factoryId = factoryId;
		this.warehouseId = warehouseId;
		this.abnormalWarehouse = abnormalWarehouse;
		this.quantity = quantity;
		this.price = price;
		this.weight = weight;
		this.createdBy = createdBy;
		this.lastUpdatedBy = lastUpdatedBy;
		this.weightConcept = weightConcept;
		this.resourceId = resourceId;
		
	}


}