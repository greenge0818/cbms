package com.prcsteel.platform.smartmatch.model.dto;

import com.prcsteel.platform.smartmatch.model.model.PurchaseOrder;

/**
 * Created by prcsteel on 2015/11/26.
 */
public class PurchaseOrderDtoForShow extends PurchaseOrder {
    private String deliveryCity;
    private String purchaseOtherCity;
    private String ownerName;
    private String purchaseStatus;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getPurchaseOtherCity() {
        return purchaseOtherCity;
    }

    public void setPurchaseOtherCity(String purchaseOtherCity) {
        this.purchaseOtherCity = purchaseOtherCity;
    }

	public String getPurchaseStatus() {
		return purchaseStatus;
	}

	public void setPurchaseStatus(String purchaseStatus) {
		this.purchaseStatus = purchaseStatus;
	}
}
