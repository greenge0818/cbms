package com.prcsteel.platform.order.model.dto;
/**
 * 
 * @author zhoukun
 */
public class IncreaseInvoiceInDto {

	Long orderItemId;//订单详情ID
	
	Double originalAmount;//更新前的金额
	
	Double originalWeight;//更新前的重量
	
	Double goalAmount;//需要更新成的金额
	
	Double goalWeight;//需要更新成的重量

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
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

	public Double getGoalAmount() {
		return goalAmount;
	}

	public void setGoalAmount(Double goalAmount) {
		this.goalAmount = goalAmount;
	}

	public Double getGoalWeight() {
		return goalWeight;
	}

	public void setGoalWeight(Double goalWeight) {
		this.goalWeight = goalWeight;
	}
	
	
}
