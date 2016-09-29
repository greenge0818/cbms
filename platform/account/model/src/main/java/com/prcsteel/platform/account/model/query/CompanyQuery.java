package com.prcsteel.platform.account.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.List;

/**
 * Created by dengxiyan on 2016/3/5.
 */
public class CompanyQuery extends PagedQuery {
    private String companyName;
    private Long orgId;
    private Long accountTag;
    private List<Long> userIdList;
    private List<String> purchaseStatusList;
    private List<String> consignStatusList;
    private List<String> invoiceStatusList;
    private List<String> cardStatusList;
    private List<String> payStatusList; //打款资料状态集合
    private String orderBy; //根据什么排序
    private String order; //排序的顺序
    private String ids;//前台传过来的Id列表
    private List<String>idList;//客户Id集合
    private int queryStatus;//查询类型(证件资料,开票资料,打款资料等)
    private List<String> contractTemplateTypeList;//chengui 合同模板性质

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getAccountTag() {
        return accountTag;
    }

    public void setAccountTag(Long accountTag) {
        this.accountTag = accountTag;
    }

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }

    public List<String> getPurchaseStatusList() {
        return purchaseStatusList;
    }

    public void setPurchaseStatusList(List<String> purchaseStatusList) {
        this.purchaseStatusList = purchaseStatusList;
    }

    public List<String> getConsignStatusList() {
        return consignStatusList;
    }

    public void setConsignStatusList(List<String> consignStatusList) {
        this.consignStatusList = consignStatusList;
    }

    public List<String> getInvoiceStatusList() {
        return invoiceStatusList;
    }

    public void setInvoiceStatusList(List<String> invoiceStatusList) {
        this.invoiceStatusList = invoiceStatusList;
    }

    public List<String> getCardStatusList() {
        return cardStatusList;
    }

    public void setCardStatusList(List<String> cardStatusList) {
        this.cardStatusList = cardStatusList;
    }

    public List<String> getPayStatusList() {
        return payStatusList;
    }

    public void setPayStatusList(List<String> payStatusList) {
        this.payStatusList = payStatusList;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public int getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(int queryStatus) {
        this.queryStatus = queryStatus;
    }

    public List<String> getContractTemplateTypeList() {
        return contractTemplateTypeList;
    }

    public void setContractTemplateTypeList(List<String> contractTemplateTypeList) {
        this.contractTemplateTypeList = contractTemplateTypeList;
    }
}
