package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.List;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportAccountFundQuery
 * @Package com.prcsteel.platform.order.model.query
 * @Description: 往来单位账务报表
 * @date 2015/12/17
 */
public class ReportAccountFundQuery extends PagedQuery {
    private String startTimeStr; //前端界面开始时间
    private String endTimeStr;//前端界面结束时间
    private String accountName;
    private Long accountId;
    private boolean isPage = true;
    private List<String> applyTypeList;

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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public boolean isPage() {
        return isPage;
    }

    public void setIsPage(boolean isPage) {
        this.isPage = isPage;
    }

    public List<String> getApplyTypeList() {
        return applyTypeList;
    }

    public void setApplyTypeList(List<String> applyTypeList) {
        this.applyTypeList = applyTypeList;
    }
}
