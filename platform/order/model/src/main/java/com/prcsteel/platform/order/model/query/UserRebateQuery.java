package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.Date;

/**
 * 买家返利报表用户详情
 * Created by caochao on 2015/9/8.
 */
public class UserRebateQuery extends PagedQuery {

    private Date startDate;

    private Date endDate;

    private Long userId;

    private Long accountId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
