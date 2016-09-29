package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by Rabbit Mao on 2015/8/5.
 */
public class ConfirmedInvoiceOutDto {
    Long id;
    String buyerName;
    String code;
    BigDecimal amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
