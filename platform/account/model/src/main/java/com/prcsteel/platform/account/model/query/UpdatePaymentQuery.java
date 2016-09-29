package com.prcsteel.platform.account.model.query;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by myh on 2016/3/15.
 */
public class UpdatePaymentQuery {
    private Long id;
    private BigDecimal balance;
    private BigDecimal balanceFreeze;
    private BigDecimal secondSettlement;
    private BigDecimal secondSettlementFreeze;
    private BigDecimal creditUsed;
    private BigDecimal creditLimit;
    private Date lastUpdated;
    private String lastUpdatedBy;
    //并发处理
    private BigDecimal originBalance;
    private BigDecimal originBalanceFreeze;
    private BigDecimal originSecondSettlement;
    private BigDecimal originSecondSettlementFreeze;
    private BigDecimal originCreditUsed;
    private BigDecimal originCreditLimit;

    public UpdatePaymentQuery() {
    }

    public UpdatePaymentQuery(Long id, BigDecimal balance, BigDecimal balanceFreeze, BigDecimal secondSettlement,
                              BigDecimal secondSettlementFreeze, BigDecimal creditUsed, BigDecimal creditLimit,
                              Date lastUpdated, String lastUpdatedBy, BigDecimal originBalance,
                              BigDecimal originBalanceFreeze, BigDecimal originSecondSettlement,
                              BigDecimal originSecondSettlementFreeze, BigDecimal originCreditUsed,
                              BigDecimal originCreditLimit) {
        this.id = id;
        this.balance = balance;
        this.balanceFreeze = balanceFreeze;
        this.secondSettlement = secondSettlement;
        this.secondSettlementFreeze = secondSettlementFreeze;
        this.creditUsed = creditUsed;
        this.creditLimit = creditLimit;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.originBalance = originBalance;
        this.originBalanceFreeze = originBalanceFreeze;
        this.originSecondSettlement = originSecondSettlement;
        this.originSecondSettlementFreeze = originSecondSettlementFreeze;
        this.originCreditUsed = originCreditUsed;
        this.originCreditLimit = originCreditLimit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalanceFreeze() {
        return balanceFreeze;
    }

    public void setBalanceFreeze(BigDecimal balanceFreeze) {
        this.balanceFreeze = balanceFreeze;
    }

    public BigDecimal getSecondSettlement() {
        return secondSettlement;
    }

    public void setSecondSettlement(BigDecimal secondSettlement) {
        this.secondSettlement = secondSettlement;
    }

    public BigDecimal getSecondSettlementFreeze() {
        return secondSettlementFreeze;
    }

    public void setSecondSettlementFreeze(BigDecimal secondSettlementFreeze) {
        this.secondSettlementFreeze = secondSettlementFreeze;
    }

    public BigDecimal getCreditUsed() {
        return creditUsed;
    }

    public void setCreditUsed(BigDecimal creditUsed) {
        this.creditUsed = creditUsed;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public BigDecimal getOriginBalance() {
        return originBalance;
    }

    public void setOriginBalance(BigDecimal originBalance) {
        this.originBalance = originBalance;
    }

    public BigDecimal getOriginBalanceFreeze() {
        return originBalanceFreeze;
    }

    public void setOriginBalanceFreeze(BigDecimal originBalanceFreeze) {
        this.originBalanceFreeze = originBalanceFreeze;
    }

    public BigDecimal getOriginSecondSettlement() {
        return originSecondSettlement;
    }

    public void setOriginSecondSettlement(BigDecimal originSecondSettlement) {
        this.originSecondSettlement = originSecondSettlement;
    }

    public BigDecimal getOriginSecondSettlementFreeze() {
        return originSecondSettlementFreeze;
    }

    public void setOriginSecondSettlementFreeze(BigDecimal originSecondSettlementFreeze) {
        this.originSecondSettlementFreeze = originSecondSettlementFreeze;
    }

    public BigDecimal getOriginCreditUsed() {
        return originCreditUsed;
    }

    public void setOriginCreditUsed(BigDecimal originCreditUsed) {
        this.originCreditUsed = originCreditUsed;
    }

    public BigDecimal getOriginCreditLimit() {
        return originCreditLimit;
    }

    public void setOriginCreditLimit(BigDecimal originCreditLimit) {
        this.originCreditLimit = originCreditLimit;
    }
}
