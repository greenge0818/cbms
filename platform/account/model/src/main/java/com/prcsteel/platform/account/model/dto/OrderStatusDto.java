package com.prcsteel.platform.account.model.dto;

import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * Created by caochao on 2016/4/29.
 */
public class OrderStatusDto {
    public OrderStatusDto() {

    }

    public OrderStatusDto(List<String> orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatusDto(List<String> orderStatus, String payStatus) {
        this.orderStatus = orderStatus;
        this.payStatus = payStatus;
    }

    private List<String> orderStatus;

    private String payStatus;

    public List<String> getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(List<String> orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
}
