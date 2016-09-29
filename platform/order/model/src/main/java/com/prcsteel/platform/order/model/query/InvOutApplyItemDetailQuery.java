package com.prcsteel.platform.order.model.query;

import java.util.List;

/** 开票申请详情项查询Dto
 * Created by lcw on 2015/9/18.
 */
public class InvOutApplyItemDetailQuery {
    private String status;

    private List<Long> applyIds;

    private Long invoiceInId;

    private List<Long> ids;

    private List<Long> invoiceInIds;

    private List<Long> notInvoiceInIds;

    private  List<String> invInStatusList;

    private  List<String> relationStatusList;

    private List<Long> checkListIds;

    private  Integer isDefer;

    public List<Long> getInvoiceInIds() {
        return invoiceInIds;
    }

    public void setInvoiceInIds(List<Long> invoiceInIds) {
        this.invoiceInIds = invoiceInIds;
    }

    public List<String> getInvInStatusList() {
        return invInStatusList;
    }

    public void setInvInStatusList(List<String> invInStatusList) {
        this.invInStatusList = invInStatusList;
    }

    public List<String> getRelationStatusList() {
        return relationStatusList;
    }

    public void setRelationStatusList(List<String> relationStatusList) {
        this.relationStatusList = relationStatusList;
    }

    public Long getInvoiceInId() {
        return invoiceInId;
    }

    public void setInvoiceInId(Long invoiceInId) {
        this.invoiceInId = invoiceInId;
    }

    public List<Long> getNotInvoiceInIds() {
        return notInvoiceInIds;
    }

    public void setNotInvoiceInIds(List<Long> notInvoiceInIds) {
        this.notInvoiceInIds = notInvoiceInIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Long> getApplyIds() {
        return applyIds;
    }

    public void setApplyIds(List<Long> applyIds) {
        this.applyIds = applyIds;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getCheckListIds() {
        return checkListIds;
    }

    public void setCheckListIds(List<Long> checkListIds) {
        this.checkListIds = checkListIds;
    }

    public Integer getIsDefer() {
        return isDefer;
    }

    public void setIsDefer(Integer isDefer) {
        this.isDefer = isDefer;
    }
}
