package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDetailModifier {

	Long orderDetailId;
	
	BigDecimal changeWeight = BigDecimal.ZERO;
	
	BigDecimal changeAmount = BigDecimal.ZERO;
	
	BigDecimal changeBuyerAmount = BigDecimal.ZERO;
	
	String lastUpdatedBy;
	
	Date lastUpdated;

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public BigDecimal getChangeBuyerAmount() {
		return changeBuyerAmount;
	}

	public void setChangeBuyerAmount(BigDecimal changeBuyerAmount) {
		this.changeBuyerAmount = changeBuyerAmount;
	}

	public Long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public BigDecimal getChangeWeight() {
		return changeWeight;
	}

	public void setChangeWeight(BigDecimal changeWeight) {
		this.changeWeight = changeWeight;
	}

	public BigDecimal getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(BigDecimal changeAmount) {
		this.changeAmount = changeAmount;
	}
}
