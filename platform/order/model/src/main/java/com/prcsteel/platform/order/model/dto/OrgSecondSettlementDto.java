package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

public class OrgSecondSettlementDto {
	
	private Long baseId;
	
    private BigDecimal amount;

	private String applyType;
	
	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public Long getBaseId() {
		return baseId;
	}

	public void setBaseId(Long baseId) {
		this.baseId = baseId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}    
}
