package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Rabbit Mao on 2015/8/20.
 */
public class LocalTransactionDataJobDto {
    private Long accountId;
    private Long bankTransactionId;
    private String serialNumber;
    private BigDecimal amount;
    private Date transactionTime;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getBankTransactionId() {
        return bankTransactionId;
    }

    public void setBankTransactionId(Long bankTransactionId) {
        this.bankTransactionId = bankTransactionId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }
}
