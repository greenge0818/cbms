package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dq on 15-9-14.
 */
public class InvoiceOutApplyDetailItemsVoDto {

	// 交易单号
	private Long orderId;
	private String orderCode;
	// 开单时间
	private String created;
	// 实提总金额
	private BigDecimal totalActualAmount = BigDecimal.ZERO;
	// 实提总重量
	private BigDecimal totalActualWeight = BigDecimal.ZERO;
	// 合同总金额
	private BigDecimal totalContractAmount = BigDecimal.ZERO;
	// hidden 已申请开票金额
	private BigDecimal amount = BigDecimal.ZERO;
	// hidden 未开票金额
	private BigDecimal unAmount = BigDecimal.ZERO;
	// hidden 未开票重量
	private BigDecimal unWeight = BigDecimal.ZERO;

	private List<InvoiceOutApplyDetailItemsListVoDto> detailList = new ArrayList<InvoiceOutApplyDetailItemsListVoDto>();

	public void addDetail(InvoiceOutApplyDetailItemsListVoDto detail) {
		this.detailList.add(detail);
	}

	public List<InvoiceOutApplyDetailItemsListVoDto> getDetailList() {
		return detailList;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setDetailList(List<InvoiceOutApplyDetailItemsListVoDto> detailList) {
		this.detailList = detailList;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public BigDecimal getTotalActualWeight() {
		return totalActualWeight;
	}

	public void setTotalActualWeight(BigDecimal totalActualWeight) {
		this.totalActualWeight = totalActualWeight;
	}

	public BigDecimal getTotalContractAmount() {
		return totalContractAmount;
	}

	public void setTotalContractAmount(BigDecimal totalContractAmount) {
		this.totalContractAmount = totalContractAmount;
	}

	public BigDecimal getTotalActualAmount() {
		return totalActualAmount;
	}

	public void setTotalActualAmount(BigDecimal totalActualAmount) {
		this.totalActualAmount = totalActualAmount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getUnAmount() {
		return unAmount;
	}

	public void setUnAmount(BigDecimal unAmount) {
		this.unAmount = unAmount;
	}

	public BigDecimal getUnWeight() {
		return unWeight;
	}

	public void setUnWeight(BigDecimal unWeight) {
		this.unWeight = unWeight;
	}
}
