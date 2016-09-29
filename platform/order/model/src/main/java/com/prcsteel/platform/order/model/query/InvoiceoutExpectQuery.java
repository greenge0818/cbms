package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.Date;
import java.util.List;

/**
 * Created by caochao on 2015/9/14.
 */
public class InvoiceoutExpectQuery extends PagedQuery {
    private Long orgId;

    private Long buyerId;

    private Date beginTime;

    private Date endTime;

    private List<String> itemStatusList;

    private List<String> applyStatusList;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public List<String> getItemStatusList() {
        return itemStatusList;
    }

    public void setItemStatusList(List<String> itemStatusList) {
        this.itemStatusList = itemStatusList;
    }

    public List<String> getApplyStatusList() {
        return applyStatusList;
    }

    public void setApplyStatusList(List<String> applyStatusList) {
        this.applyStatusList = applyStatusList;
    }
}
