package com.prcsteel.platform.order.model.query;

import java.util.Arrays;
import java.util.List;

public class AllowanceOrderQuery {
	private Long id;
	private Long sellerId;
	private String contractCode;
	private Long buyerId;
	private String orderCode;
	private String startTime;
	private String endTime;
	private String allowanceType;
	private List<String> orderIds = null;
	private List<Long> orderDetailIds = null;
	private List<Long> orgs = null;
	private Long sellerDeptId;
	private Long buyerDeptId;
	
	public List<String> getOrderIds() {
		return orderIds;
	}
	public void setOrderIds(String orderIds) {
		if("".equals(orderIds)) {
			this.orderIds = null;
		}else {
			this.orderIds = Arrays.asList(orderIds.split(","));
		}
	}
	public List<Long> getOrgs() {
		return orgs;
	}
	public void setOrgs(List<Long> orgs) {
		this.orgs = orgs;
	}
	public List<Long> getOrderDetailIds() {
		return orderDetailIds;
	}
	public void setOrderDetailIds(List<Long> orderDetailIds) {
		this.orderDetailIds = orderDetailIds;
	}
	public String getAllowanceType() {
		return allowanceType;
	}
	public void setAllowanceType(String allowanceType) {
		this.allowanceType = allowanceType;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrderIds(List<String> orderIds) {
		this.orderIds = orderIds;
	}

	public Long getSellerDeptId() {
		return sellerDeptId;
	}

	public void setSellerDeptId(Long sellerDeptId) {
		this.sellerDeptId = sellerDeptId;
	}

	public Long getBuyerDeptId() {
		return buyerDeptId;
	}

	public void setBuyerDeptId(Long buyerDeptId) {
		this.buyerDeptId = buyerDeptId;
	}
}
