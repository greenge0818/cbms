package com.prcsteel.platform.order.model.query;

import java.util.List;

import com.prcsteel.platform.common.query.PagedQuery;
import com.prcsteel.platform.order.model.enums.InvoiceInStatus;

/**
 * @author lichaowie
 * @version V1.1
 * @Title: InvoiceInListQuery
 * @Package com.prcsteel.platform.order.model.query
 * @Description: 进项发票查询model
 * @date 2015/9/17
 */
public class InvoiceInQuery extends PagedQuery {

    private List<Long> applyIds;

    private  List<InvoiceInStatus> status;

    private  String relationStatus;

    private  String invoiceStartTime;

    private  String invoiceEndTime;

    private  Integer isDefer;

    public String getInvoiceEndTime() {
        return invoiceEndTime;
    }

    public void setInvoiceEndTime(String invoiceEndTime) {
        this.invoiceEndTime = invoiceEndTime;
    }

    public String getInvoiceStartTime() {
        return invoiceStartTime;
    }

    public void setInvoiceStartTime(String invoiceStartTime) {
        this.invoiceStartTime = invoiceStartTime;
    }


    public List<InvoiceInStatus> getStatus() {
        return status;
    }

    public void setStatus(List<InvoiceInStatus> status) {
        this.status = status;
    }

    public List<Long> getApplyIds() {
        return applyIds;
    }

    public void setApplyIds(List<Long> applyIds) {
        this.applyIds = applyIds;
    }

    public String getRelationStatus() {
        return relationStatus;
    }

    public void setRelationStatus(String relationStatus) {
        this.relationStatus = relationStatus;
    }

    public Integer getIsDefer() {
        return isDefer;
    }

    public void setIsDefer(Integer isDefer) {
        this.isDefer = isDefer;
    }
}
