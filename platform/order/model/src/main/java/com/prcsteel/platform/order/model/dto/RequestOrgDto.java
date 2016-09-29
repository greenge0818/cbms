package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

public class RequestOrgDto {
	
	private BigDecimal residueLimit;//剩余额度
	
	private BigDecimal limitAmount;//服务中心可用提现金额

	
	public BigDecimal getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(BigDecimal limitAmount) {
		this.limitAmount = limitAmount;
	}

	public BigDecimal getResidueLimit() {
		return residueLimit;
	}

	public void setResidueLimit(BigDecimal residueLimit) {
		this.residueLimit = residueLimit;
	}
	
}
