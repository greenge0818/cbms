package com.prcsteel.platform.order.model.query;

import java.util.List;

import com.prcsteel.platform.order.model.model.ConsignOrder;

/**
 * Created by lcw on 2015/10/16.
 */
public class ConsignOrderQuery extends ConsignOrder {
    private List<Long> orderIds;

    private String oldStatus;

    public List<Long> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }
}
