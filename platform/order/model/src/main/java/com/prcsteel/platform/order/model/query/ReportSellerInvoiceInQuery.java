package com.prcsteel.platform.order.model.query;

import java.util.List;

import com.prcsteel.platform.order.model.model.ReportSellerInvoiceIn;

public class ReportSellerInvoiceInQuery {

	private Long sellerId;
	
	private List<String> statusList = null;
	
	private String payStatus;
	
	private ReportSellerInvoiceIn maxReportSellerInvoiceIn;
	
	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

	public ReportSellerInvoiceIn getMaxReportSellerInvoiceIn() {
		return maxReportSellerInvoiceIn;
	}

	public void setMaxReportSellerInvoiceIn(ReportSellerInvoiceIn maxReportSellerInvoiceIn) {
		this.maxReportSellerInvoiceIn = maxReportSellerInvoiceIn;
	}
	
}
