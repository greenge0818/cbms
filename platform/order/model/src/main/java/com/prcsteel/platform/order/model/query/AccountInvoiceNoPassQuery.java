package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author zhoukun
 */
public class AccountInvoiceNoPassQuery extends PagedQuery {

	private Long buyerId;

	private String invoiceStatus;

	private List<Long> orgList;

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public List<Long> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<Long> orgList) {
		this.orgList = orgList;
	}
}
