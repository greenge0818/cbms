package com.prcsteel.platform.account.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 买家开票发票状态
 * Created by DQ on 2015/11/6.
 */

public enum AccountInvoiceType {
	PRIVATE("PRIVATE","增值税专用发票"),
    NORMAL("NORMAL","增值税普通发票"); 

    private String code;
    private String value;

    AccountInvoiceType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public static String getValue(String code) {
    	Optional<AccountInvoiceType> res = Stream.of(AccountInvoiceType.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getValue() : "";
    }
}