package com.prcsteel.platform.account.model.enums;

/**
 * 合同模板条码类型
 * Created by dengxiyan on 2016/4/6.
 */
public enum AccountContractTemplateBarCodeType {
    BUYER("10","买家客户销售合同"),
    SELLER("20","卖家客户采购合同"),
    CHANNEL("30","款道合同"),
    FRMAE("50","框架合同"),
    ORDER("60","对账单"),
    YEAR_PURCHASE("80","买家年度采购协议"),
    CONSIGN("90","卖家代运营协议");

    private String code;
    // 成员变量
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // 构造方法
     AccountContractTemplateBarCodeType(String code, String name) {
        this.name = name;
        this.code=code;
    }
}
