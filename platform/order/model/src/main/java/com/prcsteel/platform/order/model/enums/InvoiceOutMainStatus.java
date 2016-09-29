package com.prcsteel.platform.order.model.enums;
/**
 * 
 * @author zhoukun
 */
public enum InvoiceOutMainStatus {
	NO_INPUT("0"),                  // 财务未开票
    INPUT("1"),                     // 财务已开票
    ;

    private String code;

    InvoiceOutMainStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
