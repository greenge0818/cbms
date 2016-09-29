package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author zhoukun
 */
public class ConsignOrderDetailQuery extends PagedQuery {
	private Long orgId;

	private Long buyerTrader;

	private Long buyerId;

	private String buyerFullName;
	
	private Date beginTime;
	
	private Date endTime;

	private List<String> statusList;

	private List<String> payStatusList;

	private List<String> fillUpStatusList;

	private Long sellId;

	private Date secBeginTime;

	public Long getSellId() {
		return sellId;
	}

	public void setSellId(Long sellId) {
		this.sellId = sellId;
	}

	public Date getSecBeginTime() {
		return secBeginTime;
	}

	public void setSecBeginTime(Date secBeginTime) {
		this.secBeginTime = secBeginTime;
	}

	public Date getSecEndTime() {
		return secEndTime;
	}

	public void setSecEndTime(Date secEndTime) {
		this.secEndTime = secEndTime;
	}

	private Date secEndTime;

	private Long orderId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getBuyerTrader() {
		return buyerTrader;
	}

	public void setBuyerTrader(Long buyerTrader) {
		this.buyerTrader = buyerTrader;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerFullName() {
		return buyerFullName;
	}

	public void setBuyerFullName(String buyerFullName) {
		this.buyerFullName = buyerFullName;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

	public List<String> getPayStatusList() {
		return payStatusList;
	}

	public void setPayStatusList(List<String> payStatusList) {
		this.payStatusList = payStatusList;
	}

	public List<String> getFillUpStatusList() {
		return fillUpStatusList;
	}

	public void setFillUpStatusList(List<String> fillUpStatusList) {
		this.fillUpStatusList = fillUpStatusList;
	}
}
