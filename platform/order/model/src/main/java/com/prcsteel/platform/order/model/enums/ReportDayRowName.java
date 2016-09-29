package com.prcsteel.platform.order.model.enums;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportDayRowName
 * @Package com.prcsteel.platform.order.model.enums
 * @Description: 服务中心日报交易列的行数据
 * @date 2015/12/14
 */
public enum  ReportDayRowName {
    //顺序和前端界面保持一致
    SaleAmount("sale","销售金额（元）"),
    PurchaseAmount("purchase","采购金额（元）"),
    Weight("weight","交易数量（吨）"),
    Orders("orders","交易笔数（笔）"),
    ConsignOrders("consignOrders","代运营交易单笔数（笔）"),
    Accounts("accounts","代运营卖家数（家）");

    private String code;
    private String name;

    ReportDayRowName(String code, String name) {
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
