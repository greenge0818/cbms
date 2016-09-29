package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.Date;
import java.util.List;

/**
 * Created by caochao on 2015/8/25.
 */
public class BuyerTradeStatisticsQuery extends PagedQuery {
    private Long orgId;

    private Long buyerTrader;

    private Long buyerId;

    private Date beginTime;

    private Date endTime;

    private List<String> statusList;

    private String statusCode;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getBuyerTrader() {
        return buyerTrader;
    }

    public void setBuyerTrader(Long buyerTrader) {
        this.buyerTrader = buyerTrader;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

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

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }
}
