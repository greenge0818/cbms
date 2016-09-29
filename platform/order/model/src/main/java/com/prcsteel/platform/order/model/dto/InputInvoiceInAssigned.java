package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * 
 * @author zhoukun
 */
public class InputInvoiceInAssigned {

	private Long orderId;
	
	private Long orderitemId;
	
	private BigDecimal increaseWeight = BigDecimal.ZERO;
	
	private BigDecimal increaseAmount = BigDecimal.ZERO;
	
	private BigDecimal originalAmount = BigDecimal.ZERO;
	
	private BigDecimal originalWeight = BigDecimal.ZERO;

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

	public BigDecimal getIncreaseWeight() {
		return increaseWeight;
	}

	public void setIncreaseWeight(BigDecimal increaseWeight) {
		this.increaseWeight = increaseWeight;
	}

	public BigDecimal getIncreaseAmount() {
		return increaseAmount;
	}

	public void setIncreaseAmount(BigDecimal increaseAmount) {
		this.increaseAmount = increaseAmount;
	}

	public BigDecimal getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(BigDecimal originalAmount) {
		this.originalAmount = originalAmount;
	}

	public BigDecimal getOriginalWeight() {
		return originalWeight;
	}

	public void setOriginalWeight(BigDecimal originalWeight) {
		this.originalWeight = originalWeight;
	}
	
}
