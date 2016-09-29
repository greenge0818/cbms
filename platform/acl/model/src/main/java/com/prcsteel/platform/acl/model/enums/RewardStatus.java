package com.prcsteel.platform.acl.model.enums;
/**
 * 
 * @author zhoukun
 */
public enum RewardStatus {
	INVALID(0, "INVALID"),
    EFFECT(1, "EFFECT"),
    EFFECT_NEXT_MONTH(2, "EFFECT_NEXT_MONTH");

    private int code;
    private String name;

    RewardStatus(int code, String name) {
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
}
