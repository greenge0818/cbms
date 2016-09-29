package com.prcsteel.platform.smartmatch.model.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by myh on 2015/11/26.
 */
public class InquiryOrderDto {
    Long purchaseOrderItemsId;
    String code;
    String remark;
    String createdTime;
    String categoryName;
    String materialName;
    String spec;
    String factory;
    String seller;
    String warehouse;
    BigDecimal requestWeight;
    Integer quantity;
    BigDecimal stock;
    BigDecimal costPrice;
    Integer sellerCount;
    List<InquiryOrderItemsDto> items;
    Date lastUpdated;
    Long inquiryOrderId;
    String userName;
    private Long cityId;
    
    private String cityName;
    
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

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public void setCode(String code) {
        this.code = code;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
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

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public BigDecimal getRequestWeight() {
        return requestWeight;
    }

    public void setRequestWeight(BigDecimal requestWeight) {
        this.requestWeight = requestWeight;
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

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public Integer getSellerCount() {
        return sellerCount;
    }

    public void setSellerCount(Integer sellerCount) {
        this.sellerCount = sellerCount;
    }

    public List<InquiryOrderItemsDto> getItems() {
        return items;
    }

    public void setItems(List<InquiryOrderItemsDto> items) {
        this.items = items;
    }

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Long getInquiryOrderId() {
		return inquiryOrderId;
	}

	public void setInquiryOrderId(Long inquiryOrderId) {
		this.inquiryOrderId = inquiryOrderId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
