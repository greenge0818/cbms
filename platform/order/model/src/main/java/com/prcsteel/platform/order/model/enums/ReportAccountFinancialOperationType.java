package com.prcsteel.platform.order.model.enums;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportAccountFinancialOperationTyep
 * @Package com.prcsteel.platform.order.model.enums
 * @Description: 往来报表类型
 * @date 2015/12/21
 */
public enum ReportAccountFinancialOperationType {
    Receive("receive","收款"),
    Payment("payment","付款"),
    Purchase("purchase","采购"),
    Sale("sale","销售"),
    AllowanceSale("allowanceSale","销售调价单号(折让单号%s)"),
    AllowancePurchase("allowancePurchase","采购调价(折让单号%s)"),
    AllowanceRollBackSale("allowanceRollBackSale","销售调价回滚(折让单号%s)"),
    AllowanceRollBackPurchase("allowanceRollBackPurchase","采购调价回滚(折让单号%s)"),
    OrderCloseSale("orderCloseSale","销售订单关闭"),
    OrderClosePurchase("orderClosePurchase","采购订单关闭"),
    SecondSettlement("secondSettlement","二结"),
    Initial("initial","期初");

    private String code;
    private String name;

    ReportAccountFinancialOperationType(String code, String name) {
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

    public String getName(String... code) {
        return  String.format(name,code);
    }

    public void setName(String name) {
        this.name = name;
    }
}
