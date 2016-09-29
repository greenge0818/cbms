package com.prcsteel.platform.order.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class EcOrder {
    private String remoteCode;//订单ID

    private String operator;//交易员

    private String mobile;//交易员手机号

    private String city;//交货地

    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    private List<EcOrderItems> items;

    public List<EcOrderItems> getItems() {
        return items;
    }

    public void setItems(List<EcOrderItems> items) {
        this.items = items;
    }

    public String getRemoteCode() {
        return remoteCode;
    }

    public void setRemoteCode(String remoteCode) {
        this.remoteCode = remoteCode;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}