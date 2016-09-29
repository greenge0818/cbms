package com.prcsteel.platform.order.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 订单变更状态
 * @author chengui
 */
public enum ConsignOrderAlterStatus {
    PENDING_CHANGED("PENDING_CHANGED", "待变更"),
    PENDING_APPROVAL("PENDING_APPROVAL", "已变更待审核"),
    DISAPPROVE("DISAPPROVE", "变更审核不通过"),
    CHANGED_SUCCESS1("CHANGED_SUCCESS1", "变更成功（审核通过）"),
    CHANGED_SUCCESS2("CHANGED_SUCCESS2", "变更成功（关联成功）"),
    CHANGED_SUCCESS3("CHANGED_SUCCESS3", "变更成功（已申请付款）"),
    PENDING_RELATE("PENDING_RELATE", "待关联变更"),
    PENDING_APPLY("PENDING_APPLY", "待申请付款变更"),
    CLOSED ("CLOSED", "已关闭本次变更"),
    PAYED_DISAPPROVE ("PAYED_DISAPPROVE", "付款申请审核不通过"),
    PENDING_APPR_PAY ("PENDING_APPR_PAY", "待审核付款"),
    PENDING_PRINT_PAY ("PENDING_PRINT_PAY", "待打印付款申请单"),
    PENDING_CONFIRM_PAY("PENDING_CONFIRM_PAY", "待确认付款"),
    PENDING_DEL_APPROVAL("PENDING_DEL_APPROVAL", "已删除待审核"),
    DEL("DEL", "已删除"),
    ORIGIN_ORDER("ORIGIN_ORDER","原订单");

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

    ConsignOrderAlterStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
        Optional<ConsignOrderAlterStatus> res = Stream.of(ConsignOrderAlterStatus.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }


}
