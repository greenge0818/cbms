package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.prcsteel.platform.common.query.PagedResult;

/**
 * 订单明细报表
 * @author zhoukun
 */
public class ConsignOrderDetailReportDto extends PagedResult {

	private List<ConsignOrderDetailsCombinationDto> listData;
	
	private BigDecimal totalAmount;
	
	private BigDecimal totalWeight;

	public List<ConsignOrderDetailsCombinationDto> getListData() {
		return listData;
	}

	public void setListData(List<ConsignOrderDetailsCombinationDto> listData) {
		this.listData = listData;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	
}
