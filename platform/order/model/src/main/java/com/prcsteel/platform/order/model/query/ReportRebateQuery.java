package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * 买家返利报表公司详情
 * @author lixiang
 *
 */
public class ReportRebateQuery extends PagedQuery {
	
	private String startTimeStr;
	
	private String endTimeStr;
	
	private String sdate;//链接传来的起始时间
	
	private String edate;//链接传来的终止时间

	private Long accountId;

	private Long contactId;
	
	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public String getEdate() {
		return edate;
	}

	public void setEdate(String edate) {
		this.edate = edate;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}
}
