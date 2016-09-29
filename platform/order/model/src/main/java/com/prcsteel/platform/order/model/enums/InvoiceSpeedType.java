package com.prcsteel.platform.order.model.enums;

/**
 * Created by Rolyer on 2015/11/25.
 */
public enum InvoiceSpeedType {
    NORMAL("NORMAL"),
    FAST ("FAST"),
    SLOW ("SLOW");

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    InvoiceSpeedType(String value) {
        this.value = value;
    }
}
