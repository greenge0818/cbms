package com.prcsteel.platform.order.model.query;

import java.util.List;

/**
 * 开票申请主表查询Dto
 * Created by lcw on 2015/9/18.
 */
public class InvoiceOutApplyQuery {
    private Long ownerId;

    private Long buyerId;

    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
