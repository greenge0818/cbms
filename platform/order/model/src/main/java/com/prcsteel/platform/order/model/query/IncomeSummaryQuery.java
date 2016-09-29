package com.prcsteel.platform.order.model.query;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * 
* @Title: IncomeQuery.java 
* @Package com.prcsteel.platform.order.model.query
* @Description: 销项票清单参数
* @author lixiang   
* @date 2016年01月25日 上午10:42:28 
* @version V1.0
 */
public class IncomeSummaryQuery extends PagedQuery {
	private List<String> orgIds;
	
	private String accountName;
	
	private String nsortName;
	
	private Date startDate;
	
	private Date endDate;

	private Boolean showSubSummary;
	private Boolean showNoneIncome;
	
	//buyer, seller, category
	private String groupType;
	
	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public List<String> getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(List<String> orgIds) {
		this.orgIds = orgIds;
	}

	public void setOrgIds(String orgIds) {
		if(orgIds!=null){
			this.orgIds = Arrays.asList(orgIds.split(","));
		}
	}
	
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getNsortName() {
		return nsortName;
	}

	public void setNsortName(String nsortName) {
		this.nsortName = nsortName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getShowSubSummary() {
		return showSubSummary;
	}

	public void setShowSubSummary(Boolean showSubSummary) {
		this.showSubSummary = showSubSummary;
	}

	public Boolean getShowNoneIncome() {
		return showNoneIncome;
	}

	public void setShowNoneIncome(Boolean showNoneIncome) {
		this.showNoneIncome = showNoneIncome;
	}
}
