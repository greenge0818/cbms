package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PoolOutDetailModifier {

	Long poolOutDetialId;
	
	BigDecimal changeWeight;
	
	BigDecimal changeAmount;
	
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

	public Long getPoolOutDetialId() {
		return poolOutDetialId;
	}

	public void setPoolOutDetialId(Long poolOutDetialId) {
		this.poolOutDetialId = poolOutDetialId;
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
