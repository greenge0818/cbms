package com.prcsteel.platform.order.model.enums;

/**
 * @Title: PaymentType.java
 * @Package com.prcsteel.cbms.persist.enums
 * @author wangxianjun
 * @date 2015年12月23日 上午9:22:27
 * @version V1.0
 */
public enum PaymentType {
    FIRST_PAYTYPE("0", "当日内现款全额支付"),
    DELAY_PAYTYPE("1", "提货后延期付款");
    // 成员变量
    private String code;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // 构造方法
    private PaymentType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
