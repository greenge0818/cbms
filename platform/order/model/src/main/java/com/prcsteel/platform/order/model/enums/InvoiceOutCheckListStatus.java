package com.prcsteel.platform.order.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 开票申请状态
 * Created by kongbinheng on 15-9-17.
 */
public enum InvoiceOutCheckListStatus {

    Invoiced("INVOICED", "已开"),
    PartialInvoiced("PARTIAL_INVOICED", "部分开票"),
    StayedInvoiced("STAYED_INVOICED", "待开"),
    RespiteInvoiced("RESPITE_INVOICED", "暂缓");

    private String value;
    private String name;

    InvoiceOutCheckListStatus(String value, String name){
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getName(String value) {
        Optional<InvoiceOutCheckListStatus> res = Stream.of(InvoiceOutCheckListStatus.values()).filter(a -> a.getValue().equals(value)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }
}
