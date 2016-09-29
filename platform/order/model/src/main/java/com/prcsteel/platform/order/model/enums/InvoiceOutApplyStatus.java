package com.prcsteel.platform.order.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 *
 * 开票申请状态
 *
 * Created by rolyer on 15-9-15.
 */
public enum InvoiceOutApplyStatus {
    PENDING_SUBMIT("PENDING_SUBMIT", "待提交"),
    PENDING_APPROVAL("PENDING_APPROVAL", "待审核"),
    APPROVED("APPROVED", "审核通过"),
    DISAPPROVE("DISAPPROVE", "审核不通过"),
    PARTIAL_INVOICED("PARTIAL_INVOICED", "部分开票"),
    REVOKED("REVOKED", "撤销"),
    INVOICED("INVOICED", "已开");


    private String value;
    private String name;

    InvoiceOutApplyStatus(String value, String name){
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
        Optional<InvoiceOutApplyStatus> res = Stream.of(InvoiceOutApplyStatus.values()).filter(a -> a.getValue().equals(value)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }
    
    /**
     * 获取申请时应当排除掉正牌这个状态集合中的订单详情
     * @return
     */
    public static List<String> getApplyExcludeStatus(){
    	return Arrays.asList(
    			PENDING_APPROVAL.getValue(),
    			APPROVED.getValue(),
    			PARTIAL_INVOICED.getValue(),
    			INVOICED.getValue());
    }
}
