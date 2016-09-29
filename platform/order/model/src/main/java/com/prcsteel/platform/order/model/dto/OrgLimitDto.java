package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

public class OrgLimitDto {
	
	private Long id;//服务中心ID
	
	private BigDecimal creditLimit;//服务中心可透支额度
	
	private BigDecimal creditLimitUsed;//服务中心已使用额度

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	public BigDecimal getCreditLimitUsed() {
		return creditLimitUsed;
	}

	public void setCreditLimitUsed(BigDecimal creditLimitUsed) {
		this.creditLimitUsed = creditLimitUsed;
	}
	
}
