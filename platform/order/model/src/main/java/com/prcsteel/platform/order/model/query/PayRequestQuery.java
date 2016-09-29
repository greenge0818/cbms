package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

public class PayRequestQuery extends PagedQuery{
private String startTime;
	
	private String endTime;
	
	private String status;	

	private String requesterName;
	
	private String showPrinted;//显示已打印的

	public String getShowPrinted() {
		return showPrinted;
	}

	public void setShowPrinted(String showPrinted) {
		this.showPrinted = showPrinted;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}
}
