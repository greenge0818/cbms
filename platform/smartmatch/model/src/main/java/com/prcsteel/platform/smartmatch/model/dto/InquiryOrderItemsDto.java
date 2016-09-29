package com.prcsteel.platform.smartmatch.model.dto;

import java.math.BigDecimal;

/**
 * Created by myh on 2015/11/26.
 */
public class InquiryOrderItemsDto {
    Long id;
    Long purchaseOrderItemsId;
    String categoryUuid;
    String categoryName;
    String materialUuid;
    String materialName;
    BigDecimal requestWeightForSum;    //为了计算资源不足情况的时候引入的用于计算的字段
    BigDecimal requestWeight;
    String spec;
    String spec1;
    String spec2;
    String spec3;
    Long sellerId;
    Long contactId;//联系人ID
    String seller;
    String sellerName;
    String sellerContactName;
    String sellerTel;
    Long factoryId;
    String factory;
    Long warehouseId;
    String warehouse;
    String weightConcept;
    Integer quantity;
    BigDecimal stock;
    BigDecimal weight = BigDecimal.ZERO;
    BigDecimal costPrice;
    String purchaseFactoryIds;
    String purchaseFactoryNames;
    
    private Long cityId;
    
    private String cityName;

    private String remark;//询价单备注
    
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
		this.cityName = cityName;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWeightConcept() {
        return weightConcept;
    }

    public void setWeightConcept(String weightConcept) {
        this.weightConcept = weightConcept;
    }

    public BigDecimal getRequestWeightForSum() {
        return requestWeightForSum;
    }

    public void setRequestWeightForSum(BigDecimal requestWeightForSum) {
        this.requestWeightForSum = requestWeightForSum;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getWarehouseId() {
        return warehouseId;
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

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Long getPurchaseOrderItemsId() {
        return purchaseOrderItemsId;
    }

    public void setPurchaseOrderItemsId(Long purchaseOrderItemsId) {
        this.purchaseOrderItemsId = purchaseOrderItemsId;
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

    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public BigDecimal getRequestWeight() {
        return requestWeight;
    }

    public void setRequestWeight(BigDecimal requestWeight) {
        this.requestWeight = requestWeight;
    }

    public String getSpec1() {
        return spec1;
    }

    public void setSpec1(String spec1) {
        this.spec1 = spec1;
    }

    public String getSpec2() {
        return spec2;
    }

    public void setSpec2(String spec2) {
        this.spec2 = spec2;
    }

    public String getSpec3() {
        return spec3;
    }

    public void setSpec3(String spec3) {
        this.spec3 = spec3;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerContactName() {
        return sellerContactName;
    }

    public void setSellerContactName(String sellerContactName) {
        this.sellerContactName = sellerContactName;
    }

    public String getSellerTel() {
        return sellerTel;
    }

    public void setSellerTel(String sellerTel) {
        this.sellerTel = sellerTel;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public String getPurchaseFactoryNames() {
        return purchaseFactoryNames;
    }

    public void setPurchaseFactoryNames(String purchaseFactoryNames) {
        this.purchaseFactoryNames = purchaseFactoryNames;
    }

    public String getPurchaseFactoryIds() {
        return purchaseFactoryIds;
    }

    public void setPurchaseFactoryIds(String purchaseFactoryIds) {
        this.purchaseFactoryIds = purchaseFactoryIds;
    }

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}
    
}
