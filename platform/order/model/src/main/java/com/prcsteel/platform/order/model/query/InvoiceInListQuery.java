package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.List;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: InvoiceInListQuery
 * @Package com.prcsteel.platform.order.model.query
 * @Description: 进项发票清单查询model
 * @date 2015/9/12
 */
public class InvoiceInListQuery extends PagedQuery {

    private Long orgId;
    private  String sellerName;
    private String  code;
    private String strStartTime;
    private String strEndTime;
    private List<Long> userIdList;
    private List<Long> detailIdList;
    private List<String> statusList;
    private Boolean isPage = true;//是否分页查询

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }

    public List<Long> getDetailIdList() {
        return detailIdList;
    }

    public void setDetailIdList(List<Long> detailIdList) {
        this.detailIdList = detailIdList;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public Boolean isPage() {
        return isPage;
    }

    public void setIsPage(Boolean isPage) {
        this.isPage = isPage;
    }
}
