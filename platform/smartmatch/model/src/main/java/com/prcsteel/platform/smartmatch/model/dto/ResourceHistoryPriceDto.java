package com.prcsteel.platform.smartmatch.model.dto;

import java.math.BigDecimal;

/**
 * 历史资源价格DTO
 * @author caosulin
 *
 */
public class ResourceHistoryPriceDto {
	private String versionDate;
	private BigDecimal price;
	public String getVersionDate() {
		return versionDate;
	}
	public void setVersionDate(String versionDate) {
		this.versionDate = versionDate;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
