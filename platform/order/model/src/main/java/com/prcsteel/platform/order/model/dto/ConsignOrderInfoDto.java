package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口返回实体 订单详情
 * Created by chengui on 2016/5/30.
 */
public class ConsignOrderInfoDto {

    private ConsignOrder order;

    private List<ConsignOrderItems> items = new ArrayList<ConsignOrderItems>();

    public ConsignOrderInfoDto() {
    }

    public ConsignOrderInfoDto(ConsignOrder order, List<ConsignOrderItems> items) {
        this.order = order;
        this.items = items;
    }

    public ConsignOrder getOrder() {
        return order;
    }

    public void setOrder(ConsignOrder order) {
        this.order = order;
    }

    public List<ConsignOrderItems> getItems() {
        return items;
    }

    public void setItems(List<ConsignOrderItems> items) {
        this.items = items;
    }
}
