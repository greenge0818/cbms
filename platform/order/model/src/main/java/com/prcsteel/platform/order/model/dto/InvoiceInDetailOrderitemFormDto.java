package com.prcsteel.platform.order.model.dto;

public class InvoiceInDetailOrderitemFormDto {

    private Long orderId;

    private Long orderitemId;

    private Double originalAmount;
    
    private Double originalWeight;
    
    private Double increaseWeight;

    private Double increaseAmount;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderitemId() {
		return orderitemId;
	}

	public void setOrderitemId(Long orderitemId) {
		this.orderitemId = orderitemId;
	}

	public Double getIncreaseWeight() {
		return increaseWeight;
	}

	public void setIncreaseWeight(Double increaseWeight) {
		this.increaseWeight = increaseWeight;
	}

	public Double getIncreaseAmount() {
		return increaseAmount;
	}

	public void setIncreaseAmount(Double increaseAmount) {
		this.increaseAmount = increaseAmount;
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
    
}