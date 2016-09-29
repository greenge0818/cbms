package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

import com.prcsteel.platform.account.model.enums.AccountType;

/**
 * Created by caochao on 2015/7/22.
 */
public class ConsignOrderSettleDto {
    private String companyName;
    private String account_BankName;
    private String account_BankCode;
    private String account_Tel;
    private String account_Address;  //注册地址
    private BigDecimal amount;//已收金额
    private BigDecimal settleAmount;//结算金额
    private BigDecimal receiveAmount; //应收款
    private BigDecimal payAmount;//应付款
    private BigDecimal balance;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    private String accountType;



    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAccount_BankName() {
        return account_BankName;
    }

    public void setAccount_BankName(String account_BankName) {
        this.account_BankName = account_BankName;
    }

    public String getAccount_BankCode() {
        return account_BankCode;
    }

    public void setAccount_BankCode(String account_BankCode) {
        this.account_BankCode = account_BankCode;
    }

    public String getAccount_Tel() {
        return account_Tel;
    }

    public void setAccount_Tel(String account_Tel) {
        this.account_Tel = account_Tel;
    }

    public String getAccount_Address() {
        return account_Address;
    }

    public void setAccount_Address(String account_Address) {
        this.account_Address = account_Address;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(BigDecimal settleAmount) {
        this.settleAmount = settleAmount;
        BigDecimal temp = this.amount.subtract(settleAmount);
        if (temp.doubleValue() > 0) {
            if(accountType.equals(AccountType.buyer.toString())) {
                receiveAmount = BigDecimal.ZERO;
                payAmount = temp;
            }else{
                receiveAmount = temp;
                payAmount = BigDecimal.ZERO;
            }
        } else {
            if(accountType.equals(AccountType.seller.toString())) {
                receiveAmount = BigDecimal.ZERO;
                payAmount = temp.abs();
            }else{
                receiveAmount = temp.abs();
                payAmount = BigDecimal.ZERO;
            }
        }
    }

    public BigDecimal getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(BigDecimal receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
