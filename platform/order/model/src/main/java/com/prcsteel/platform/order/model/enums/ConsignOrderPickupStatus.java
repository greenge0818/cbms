package com.prcsteel.platform.order.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 
 * @author zhoukun
 */
public enum ConsignOrderPickupStatus {
	NO_ENTRY(1, "未录入"),
    PART_ENTRY(2, "部分录入"),
    ALL_ENTRY(3, "全录入");

    private int code;
    private String name;

    ConsignOrderPickupStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getNameByCode(int code) {
        Optional<ConsignOrderPickupStatus> res = Stream.of(ConsignOrderPickupStatus.values()).filter(a -> a.getCode()==code).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }
}
