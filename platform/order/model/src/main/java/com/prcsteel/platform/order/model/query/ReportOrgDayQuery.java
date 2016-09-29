package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dengxiyan on 2015/12/09.
 * 服务中心日报
 */
public class ReportOrgDayQuery extends PagedQuery {
    private List<String> orderStatusList = new ArrayList<>();
    private Date startDate;
    private Date endDate;
    private String startTimeStr; //前端界面开始时间
    private String endTimeStr;//前端界面结束时间

    private String startPageTimeStr; //分页开始时间
    private String endPageTimeStr;//分页结束时间

    private String dateOrderBy ;//如果没有则按默认
    
    public ReportOrgDayQuery(){}

    public String getDateOrderBy() {
		return dateOrderBy;
	}

	public void setDateOrderBy(String dateOrderBy) {
		this.dateOrderBy = dateOrderBy;
	}



	public ReportOrgDayQuery(List<String> orderStatusList) {
        this.orderStatusList = orderStatusList;
    }

    public ReportOrgDayQuery(Date startDate, Date endDate, List<String> orderStatusList) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.orderStatusList = orderStatusList;
    }

    public List<String> getOrderStatusList() {
        return orderStatusList;
    }

    public void setOrderStatusList(List<String> orderStatusList) {
        this.orderStatusList = orderStatusList;
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

    public String getStartPageTimeStr() {
        return startPageTimeStr;
    }

    public void setStartPageTimeStr(String startPageTimeStr) {
        this.startPageTimeStr = startPageTimeStr;
    }

    public String getEndPageTimeStr() {
        return endPageTimeStr;
    }

    public void setEndPageTimeStr(String endPageTimeStr) {
        this.endPageTimeStr = endPageTimeStr;
    }
}
