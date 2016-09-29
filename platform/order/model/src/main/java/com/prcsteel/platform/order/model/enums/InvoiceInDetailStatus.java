package com.prcsteel.platform.order.model.enums;

/**
 * @author kongbinheng
 * 进项票主表状态
 */
public enum InvoiceInDetailStatus {

    ToBeRelation("toberelation", "待关联"),
    HasRelation("hasrelation", "已关联"),
    Invoiced("invoiced", "已开"),
    Invoice("invoice", "未开");

    // 成员变量
    private String code;

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
    private InvoiceInDetailStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
