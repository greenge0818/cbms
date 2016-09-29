package com.prcsteel.platform.acl.model.enums;

/**
 * Created by Rabbit Mao on 2015/9/16.
 */
public enum RewardType {
    CATEGORY("category", "成交量提成设置"),
    ORDER("order", "提成比例设置"),
    RADIO("category_radio","买家交易量系数"),
    NORM("commission","佣金标准");


    private String code;
    private String name;

    RewardType(String code, String name) {
        this.code = code;
        this.name = name;
    }

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
}
