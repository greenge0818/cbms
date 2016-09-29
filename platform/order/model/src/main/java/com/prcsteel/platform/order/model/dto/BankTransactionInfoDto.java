package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.BankTransactionInfo;

public class BankTransactionInfoDto extends BankTransactionInfo {

    private String bankTypeName;

    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
}