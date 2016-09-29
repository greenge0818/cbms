package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

public class OrgListDto {

	private Long id;

	private String name;

	private BigDecimal creditLimit = BigDecimal.ZERO; // 服务中心信用额

	private BigDecimal creditLimitUsed = BigDecimal.ZERO; // 服务中心已使用额

	private BigDecimal amountAdd = BigDecimal.ZERO; // 今日增加

	private BigDecimal amountReduce = BigDecimal.ZERO; // 今日减少

	private BigDecimal yesterdayAmount = BigDecimal.ZERO; // 昨日余额

	private BigDecimal todayAmount = BigDecimal.ZERO; // 今日余额

	public BigDecimal getYesterdayAmount() {
		return yesterdayAmount;
	}

	public void setYesterdayAmount(BigDecimal yesterdayAmount) {
		this.yesterdayAmount = yesterdayAmount;
	}

	public BigDecimal getTodayAmount() {
		return todayAmount;
	}

	public void setTodayAmount(BigDecimal todayAmount) {
		this.todayAmount = todayAmount;
	}

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

	public BigDecimal getAmountAdd() {
		return amountAdd;
	}

	public void setAmountAdd(BigDecimal amountAdd) {
		this.amountAdd = amountAdd;
	}

	public BigDecimal getAmountReduce() {
		return amountReduce;
	}

	public void setAmountReduce(BigDecimal amountReduce) {
		this.amountReduce = amountReduce;
	}

}
