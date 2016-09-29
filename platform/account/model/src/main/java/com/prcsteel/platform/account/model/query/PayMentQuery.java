package com.prcsteel.platform.account.model.query;

import java.math.BigDecimal;
import java.util.List;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * 
 * @author lixiang 2016/03/02
 *
 */
public class PayMentQuery extends PagedQuery{
	private String code;
	
	private String startTime;
	
	private String endTime;
	
	private List<String> status;
	
	private BigDecimal payAmount;
	
	private String accountName;
	
	private String type;
	
	private Boolean closedStatus;

	public Boolean getClosedStatus() {
		return closedStatus;
	}

	public void setClosedStatus(Boolean closedStatus) {
		this.closedStatus = closedStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
