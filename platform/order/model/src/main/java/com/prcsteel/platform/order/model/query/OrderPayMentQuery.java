package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * 初次付款信息报表传参
 * 
 * @author lixiang
 *
 */
public class OrderPayMentQuery extends PagedQuery{
	private String sellerName;
	
	private String code;
	
	private String accounting;
	
	private String orgName;
	
	private String startTime;
	
	private String endTime;

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccounting() {
		return accounting;
	}

	public void setAccounting(String accounting) {
		this.accounting = accounting;
	}

	
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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

}
