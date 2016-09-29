package com.prcsteel.platform.order.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 
 * @author zhoukun
 */
public enum ConsignOrderPayStatus {
	APPLY("APPLY","待申请付款"), // 待申请（未申请／审核不通过）
    REQUESTED("REQUESTED","已申请付款待审核"),//已申请/待审核
    APPROVED("APPROVED","已通过审核待打印付款申请单"), //已通过审核待打印付款申请单
    APPLYPRINTED("APPLYPRINTED","已打印付款申请单"),//已打印付款申请单 Green add for issue3893
    PAYED("PAYED","已确认付款");  //已确认付款

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

    ConsignOrderPayStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
        Optional<ConsignOrderPayStatus> res = Stream.of(ConsignOrderPayStatus.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }
}
