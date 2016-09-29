package com.prcsteel.platform.account.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/8/22.
 */
public class AccountInAndOutQuery extends PagedQuery {
    private Date beginTime;
    private Date endTime;
    private String accountName;
    private String timeOrdering = "asc";

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTimeOrdering() {
        return timeOrdering;
    }

    public void setTimeOrdering(String timeOrdering) {
        if (timeOrdering != null && (timeOrdering.toLowerCase().equals("asc") || timeOrdering.toLowerCase().equals("desc"))) {
            this.timeOrdering = timeOrdering.toLowerCase();
        }
    }
}
