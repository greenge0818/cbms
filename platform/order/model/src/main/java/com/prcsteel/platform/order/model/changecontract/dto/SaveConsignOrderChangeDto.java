package com.prcsteel.platform.order.model.changecontract.dto;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.model.ConsignOrderChange;
import com.prcsteel.platform.order.model.model.ConsignOrderItemsChange;

import java.util.List;

/**
 * 合同变更保存用
 * Created by lichaowei on 2016/8/15.
 */
public class SaveConsignOrderChangeDto {

    private ConsignOrderChange orderChange; // 变更订单数据

    private List<ConsignOrderItemsChange> orderItemsChanges; // 变更订单数据集合

    private User user;

    public ConsignOrderChange getOrderChange() {
        return orderChange;
    }

    public void setOrderChange(ConsignOrderChange orderChange) {
        this.orderChange = orderChange;
    }

    public List<ConsignOrderItemsChange> getOrderItemsChanges() {
        return orderItemsChanges;
    }

    public void setOrderItemsChanges(List<ConsignOrderItemsChange> orderItemsChanges) {
        this.orderItemsChanges = orderItemsChanges;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
