package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.List;

/**
 * Created by caochao on 2015/8/31.
 */
public class TradeStatisticsQuery extends PagedQuery {
    private String curMonth;

    private String prevMonth;

    private List<String> statusList;

    private Long orgId;

    private List<Long> traderIdList;

    public String getCurMonth() {
        return curMonth;
    }

    public void setCurMonth(String curMonth) {
        this.curMonth = curMonth;
    }

    public String getPrevMonth() {
        return prevMonth;
    }

    public void setPrevMonth(String prevMonth) {
        this.prevMonth = prevMonth;
    }

    public List<String> getStatus() {
        return statusList;
    }

    public void setStatus(List<String> status) {
        this.statusList = status;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public List<Long> getTraderIdList() {
        return traderIdList;
    }

    public void setTraderIdList(List<Long> traderIdList) {
        this.traderIdList = traderIdList;
    }
}
