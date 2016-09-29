package com.prcsteel.platform.order.model.enums;

/**
 * @author dq
 */
public enum InvoiceOutIsDefer {
    YES("YES", 1),         // 延迟
    NO("NO", 0)			// 正常
    ;
    private String name;
    private int code;

    InvoiceOutIsDefer(String name, int code) {
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
