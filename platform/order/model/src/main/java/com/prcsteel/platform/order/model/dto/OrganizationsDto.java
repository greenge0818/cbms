package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

public class OrganizationsDto {
	
	private Long id;
	
	private String name;
	
    private BigDecimal creditLimit;
    
    private BigDecimal creditLimitUsed;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
