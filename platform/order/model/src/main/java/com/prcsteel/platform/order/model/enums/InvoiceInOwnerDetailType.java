package com.prcsteel.platform.order.model.enums;

/**
 * Created by Rabbit Mao on 2015/8/1.
 */
public enum InvoiceInOwnerDetailType {
    ORDER("ORDER", "订单"),
    INVOICE("INVOICE", "发票");

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    InvoiceInOwnerDetailType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
