package com.prcsteel.platform.order.model.dto;
/**
 * 
 * @author zhoukun
 */
public class PoolInTotalModifier {

	Long sellerId;
	Long departmentId;
	Double changeAmount;
	
	Double changeWeight;
	
	Double originalAmount;
	
	Double originalWeight;

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public Double getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(Double changeAmount) {
		this.changeAmount = changeAmount;
	}

	public Double getChangeWeight() {
		return changeWeight;
	}

	public void setChangeWeight(Double changeWeight) {
		this.changeWeight = changeWeight;
	}

	public Double getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(Double originalAmount) {
		this.originalAmount = originalAmount;
	}

	public Double getOriginalWeight() {
		return originalWeight;
	}

	public void setOriginalWeight(Double originalWeight) {
		this.originalWeight = originalWeight;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	
}
