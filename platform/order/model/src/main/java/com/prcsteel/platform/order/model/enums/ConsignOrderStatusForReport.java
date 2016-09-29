package com.prcsteel.platform.order.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author caochao
 * 2015-10-19
 */
public enum ConsignOrderStatusForReport {
    NEWAPPROVED("2", "待关联"),
    CLOSEREQUEST1("3", "交易关闭待审核"),
    CLOSEREQUEST2("5", "交易关闭待审核"),
    NEWDECLINED("-1", "交易关闭"),
    CLOSEAPPROVED("-2", "交易关闭"),
    SYSCLOSED("-3", "交易关闭"),
    CLOSEPAY("-4", "交易关闭"),
    CLOSEAFTERSETTLEAPPLY("-5","交易关闭待审核"),
    CLOSEAFTERSETTLEAPPROVED("-6","交易关闭待审核"),
    CLOSESECONDSETTLE("-7","交易关闭"),
    CLOSESECONDSETTLE2("-8","交易关闭"),
    RELATED("4", "待出库"),
    SECONDSETTLE("6", "待二次结算"),
    INVOICEREQUEST("7", "待开票申请"),
    INVOICE("8", "待开票"),
    FINISH("10", "交易完成");

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

    ConsignOrderStatusForReport(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
        Optional<ConsignOrderStatusForReport> res = Stream.of(ConsignOrderStatusForReport.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }
}
