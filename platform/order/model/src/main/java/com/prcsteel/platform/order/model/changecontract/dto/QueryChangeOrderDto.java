package com.prcsteel.platform.order.model.changecontract.dto;

import com.prcsteel.platform.order.model.enums.ConsignOrderAlterStatus;

import java.util.List;

/**
 * 变更订单查询Dto
 * Created by lichaowei on 2016/8/19.
 */
public class QueryChangeOrderDto {
    private Long orderId;

    private Long orderItemId;

    private Integer changeOrderId;

    private Integer itemChangeId;

    private List<ConsignOrderAlterStatus> alterStatuses;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getChangeOrderId() {
        return changeOrderId;
    }

    public void setChangeOrderId(Integer changeOrderId) {
        this.changeOrderId = changeOrderId;
    }

    public List<ConsignOrderAlterStatus> getAlterStatuses() {
        return alterStatuses;
    }

    public void setAlterStatuses(List<ConsignOrderAlterStatus> alterStatuses) {
        this.alterStatuses = alterStatuses;
    }

    public Integer getItemChangeId() {
        return itemChangeId;
    }

    public void setItemChangeId(Integer itemChangeId) {
        this.itemChangeId = itemChangeId;
    }
}
