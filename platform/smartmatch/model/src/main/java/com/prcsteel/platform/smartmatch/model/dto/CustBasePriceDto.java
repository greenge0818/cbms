package com.prcsteel.platform.smartmatch.model.dto;

import java.math.BigDecimal;

import com.prcsteel.platform.smartmatch.model.model.CustBasePrice;

public class CustBasePriceDto extends CustBasePrice{
	private BigDecimal lowestPrice;

	public BigDecimal getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(BigDecimal lowestPrice) {
		this.lowestPrice = lowestPrice;
	}
}
