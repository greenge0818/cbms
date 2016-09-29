package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class InvoiceInBordereauxQuery extends PagedQuery {
	
	private Long orgId;
	
	private Long sellerId;
	
	private String orderCode;

	private String sellerName;
	
	private Date startTime;

    private Date endTime; 
    
    private List<String> status;
    
	public List<String> getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = Arrays.asList(status.split(","));
	}
	
	public void setStatusForNull() {
		this.status = null;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	private List<Long> orderItemIds;

	public List<Long> getOrderItemIds() {
		return orderItemIds;
	}

	public void setOrderItemIds(List<Long> orderItemIds) {
		this.orderItemIds = orderItemIds;
	}
	
	
}
