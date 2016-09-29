package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rolyer on 15-8-4.
 */
public class InvoiceOutApplyDetailVoDto {

	private Long orgId;
	private String orgName;
	private Long ownerId;
	private String ownerName;
	// 已申请开票金额
	private BigDecimal totalAmount = BigDecimal.ZERO;
	// 未开票金额
	private BigDecimal totalUnAmount = BigDecimal.ZERO;
	// hidden 未开票重量
	private BigDecimal totalUnWeight = BigDecimal.ZERO;

	private List<InvoiceOutApplyDetailListVoDto> detailList = new ArrayList<InvoiceOutApplyDetailListVoDto>();

	public void addDetail(InvoiceOutApplyDetailListVoDto detail) {
		this.detailList.add(detail);
	}

	public List<InvoiceOutApplyDetailListVoDto> getDetailList() {
		return detailList;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalUnAmount() {
		return totalUnAmount;
	}

	public void setTotalUnAmount(BigDecimal totalUnAmount) {
		this.totalUnAmount = totalUnAmount;
	}

	public void setDetailList(List<InvoiceOutApplyDetailListVoDto> detailList) {
		this.detailList = detailList;
	}

	public BigDecimal getTotalUnWeight() {
		return totalUnWeight;
	}

	public void setTotalUnWeight(BigDecimal totalUnWeight) {
		this.totalUnWeight = totalUnWeight;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public void addAllDetail(List<InvoiceOutApplyDetailListVoDto> detailListVoDtos) {
		this.detailList.addAll(detailListVoDtos);
	}

}
