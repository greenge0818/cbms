package com.prcsteel.platform.acl.model.query;

import java.util.Date;

import com.prcsteel.platform.common.query.PagedQuery;

public class HolidayQuery extends PagedQuery {
	
	private String workDate;

	private Date startTime;// 起始时间

	private Date endTime;// 终止时间

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
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
