package com.prcsteel.platform.order.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 合同变更操作类型
 * Created by lichaowei on 2016/9/1.
 */
public enum OrderChangeType {
    ADD("ADD", "添加"),
    DEL("DEL", "删除"),
    UPDATE("UPDATE","修改");

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

    OrderChangeType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
        Optional<OrderChangeType> res = Stream.of(OrderChangeType.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }


}
