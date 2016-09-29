package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.List;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: BuyerRebateQuery
 * @Package com.prcsteel.cbms.persist.query
 * @Description: 买家返利报表查询mdoel
 * @date 2015/8/31
 */
public class BuyerRebateQuery extends PagedQuery {

    private Long orgId;
    private  String buyerTradeName;
    private String strStartTime;
    private String strEndTime;
    private Long buyerId;
    private Long contactId;
    private List<Long> userIdList;
    private List<Long> contactIdList;
    private List<Long> buyerIdList;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getBuyerTradeName() {
        return buyerTradeName;
    }

    public void setBuyerTradeName(String buyerTradeName) {
        this.buyerTradeName = buyerTradeName;
    }

    public String getStrStartTime() {
        return strStartTime;
    }

    public void setStrStartTime(String strStartTime) {
        this.strStartTime = strStartTime;
    }

    public String getStrEndTime() {
        return strEndTime;
    }

    public void setStrEndTime(String strEndTime) {
        this.strEndTime = strEndTime;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }

    public List<Long> getContactIdList() {
        return contactIdList;
    }

    public void setContactIdList(List<Long> contactIdList) {
        this.contactIdList = contactIdList;
    }

    public List<Long> getBuyerIdList() {
        return buyerIdList;
    }

    public void setBuyerIdList(List<Long> buyerIdList) {
        this.buyerIdList = buyerIdList;
    }
}
