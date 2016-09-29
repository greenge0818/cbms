package com.prcsteel.platform.order.model.enums;
/**
 * 
 * @author zhoukun
 */
public enum InvoiceOutStatus {
	INPUT("1"),                  // 已录入/待寄出
    SENT("2"),                   // 已寄出，未确认
    CONFIRMED_SUCCESS("3"),      // 确认通过
    CONFIRMED_FAIL("4")          // 确认失败
    ;

    private String code;

    InvoiceOutStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
