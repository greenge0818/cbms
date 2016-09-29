package com.prcsteel.platform.order.model.enums;
/**
 * 
 * @author zhoukun
 */
public enum PayRequestType {
	PAYMENT("1", "初次付款"),
    SECONDSETTLE("2", "二次结算"),
    WITHDRAW("3", "提现"),
	ADVANCE_PAYMENT("4", "预付款"),
	ORDER_CHANGE("5", "合同变更付款");

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

    PayRequestType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
