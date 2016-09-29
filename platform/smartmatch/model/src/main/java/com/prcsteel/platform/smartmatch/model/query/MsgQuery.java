package com.prcsteel.platform.smartmatch.model.query;

import java.util.Date;

import com.prcsteel.platform.common.query.PagedQuery;

public class MsgQuery extends PagedQuery {
	private String mobile;
	
	private String startDate;
	
	private String endDate;
	
	private Date startTime;
	
	private Date endTime;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
