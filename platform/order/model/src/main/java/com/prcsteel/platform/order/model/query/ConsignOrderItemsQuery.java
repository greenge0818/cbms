package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.List;

/**
 * 根据买家业务员查找ConsignOrderItems数据
 * @author dq
 */
public class ConsignOrderItemsQuery extends PagedQuery {
	// 条件
	private Long outApplyId;
	
	private Long ownerId;
	
	private Long BuyerId;
	
	private List<String> consignOrderStatus = null;

	private String relationStatus;
	
	private List<String> invoiceInStatus = null;

	private List<String> invoiceOutApplyStatus = null;
	
	private List<Long> orderIds = null;

	private Long departmentId;          //部门ID

	public List<Long> getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(List<Long> orderIds) {
		this.orderIds = orderIds;
	}

	public Long getOutApplyId() {
		return outApplyId;
	}

	public void setOutApplyId(Long outApplyId) {
		this.outApplyId = outApplyId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getBuyerId() {
		return BuyerId;
	}

	public void setBuyerId(Long buyerId) {
		BuyerId = buyerId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public List<String> getConsignOrderStatus() {
		return consignOrderStatus;
	}

	public void setConsignOrderStatus(List<String> consignOrderStatus) {
		this.consignOrderStatus = consignOrderStatus;
	}

	public String getRelationStatus() {
		return relationStatus;
	}

	public void setRelationStatus(String relationStatus) {
		this.relationStatus = relationStatus;
	}

	public List<String> getInvoiceInStatus() {
		return invoiceInStatus;
	}

	public void setInvoiceInStatus(List<String> invoiceInStatus) {
		this.invoiceInStatus = invoiceInStatus;
	}

	public List<String> getInvoiceOutApplyStatus() {
		return invoiceOutApplyStatus;
	}

	public void setInvoiceOutApplyStatus(List<String> invoiceOutApplyStatus) {
		this.invoiceOutApplyStatus = invoiceOutApplyStatus;
	}
}
