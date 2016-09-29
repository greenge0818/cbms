package com.prcsteel.platform.smartmatch.model.dto;

import java.math.BigDecimal;

/**
 * Created by rabbit on 2016-1-13 14:32:07.
 */
public class BillHistoryDataDto {
	private String time;

	private String nsortName;

	private BigDecimal weight;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNsortName() {
		return nsortName;
	}

	public void setNsortName(String nsortName) {
		this.nsortName = nsortName;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
}
