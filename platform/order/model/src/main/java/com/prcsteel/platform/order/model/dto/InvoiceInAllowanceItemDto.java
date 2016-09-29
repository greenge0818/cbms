package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.InvoiceInAllowanceItem;

/**
 * Created by lichaowei on 2015/12/8.
 */
public class InvoiceInAllowanceItemDto extends InvoiceInAllowanceItem {

    private Long orderItemId; // 订单详情Id

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

}
