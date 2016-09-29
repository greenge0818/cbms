package com.prcsteel.platform.order.model.query;

import java.util.Date;
import java.util.List;

/**
 * Created by dengxiyan on 2015/9/18.
 * 服务中心/交易员提成
 */
public class ReportRewardQuery {
    private Long orgId;
    private String orgName;
    private String month;
    private Long managerId;
    private String managerName;
    private List<Long> userIdList;
    private String showManagerName;

    private Date openOrderDate;
    private Long accountId;

    private Date maxYearDate;

    public Date getMaxYearDate() {
        return maxYearDate;
    }

    public void setMaxYearDate(Date maxYearDate) {
        this.maxYearDate = maxYearDate;
    }

    public Date getMinYearDate() {
        return minYearDate;
    }

    public void setMinYearDate(Date minYearDate) {
        this.minYearDate = minYearDate;
    }

    public Date getMaxMonthDate() {
        return maxMonthDate;
    }

    public void setMaxMonthDate(Date maxMonthDate) {
        this.maxMonthDate = maxMonthDate;
    }

    public Date getMinMonthDate() {
        return minMonthDate;
    }

    public void setMinMonthDate(Date minMonthDate) {
        this.minMonthDate = minMonthDate;
    }

    private Date minYearDate;

    private Date maxMonthDate;

    private Date minMonthDate;
    
    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }

    public String getShowManagerName() {
        return showManagerName;
    }

    public void setShowManagerName(String showManagerName) {
        this.showManagerName = showManagerName;
    }

	public Date getOpenOrderDate() {
		return openOrderDate;
	}

	public void setOpenOrderDate(Date openOrderDate) {
		this.openOrderDate = openOrderDate;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	
}
