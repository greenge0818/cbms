package com.prcsteel.platform.smartmatch.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.prcsteel.platform.smartmatch.model.model.PurchaseOrder;

/**
 * Created by prcsteel on 2015/11/26.
 */
public class PurchaseOrderDto extends PurchaseOrder {
    private Date createdTime;
    private String buyerName;
    private String deliveryName;
    private String categoryName;
    private BigDecimal totalWeight;
    private String ownerName;
    private String orgName;
    private String materialName;
    private String otherPurchaseCityName;
    private Long buyerId;
    private Long accepterId;
    
    public Long getAccepterId() {
		return accepterId;
	}

	public void setAccepterId(Long accepterId) {
		this.accepterId = accepterId;
	}

	//add by caosulin 页面添加需求单号
    private String requestCode;//需求单号
    
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String getBuyerName() {
        return buyerName;
    }

    @Override
    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    @Override
    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getOtherPurchaseCityName() {
        return otherPurchaseCityName;
    }

    public void setOtherPurchaseCityName(String otherPurchaseCityName) {
        this.otherPurchaseCityName = otherPurchaseCityName;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

	public String getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}   
}