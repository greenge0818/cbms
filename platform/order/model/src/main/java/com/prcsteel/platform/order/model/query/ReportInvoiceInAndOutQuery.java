package com.prcsteel.platform.order.model.query;

import java.util.Date;

/**
 * 应收应付发票报表query
 * @author tuxianming
 */
public class ReportInvoiceInAndOutQuery {
	private Long accountId;
	
	private Integer length;
	private Integer start;
	
	private Date startTime;
	private Date endTime;
	
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
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
}
