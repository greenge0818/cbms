package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportAccountFundDto
 * @Package com.prcsteel.platform.order.model.dto
 * @Description: 往来单位账务报表dto
 * @date 2015/12/18
 */
public class ReportAccountFundDto {
    private Long accountId;
    private String accountName;
    private BigDecimal initialBalance; //期初余额
    private BigDecimal endingBalance;//期末余额
    private BigDecimal purchaseAmount;//实际采购交易金额
    private BigDecimal saleAmount;//实际销售交易金额
    private BigDecimal receivedAmount;//已收金额
    private BigDecimal payedAmount;//已付金额
    private BigDecimal initialBalanceFromAccountTran;
    private BigDecimal initialBalanceFromReportFinancial;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(BigDecimal endingBalance) {
        this.endingBalance = endingBalance;
    }

    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public BigDecimal getPayedAmount() {
        return payedAmount;
    }

    public void setPayedAmount(BigDecimal payedAmount) {
        this.payedAmount = payedAmount;
    }

    public BigDecimal getInitialBalanceFromAccountTran() {
        return initialBalanceFromAccountTran;
    }

    public void setInitialBalanceFromAccountTran(BigDecimal initialBalanceFromAccountTran) {
        this.initialBalanceFromAccountTran = initialBalanceFromAccountTran;
    }

    public BigDecimal getInitialBalanceFromReportFinancial() {
        return initialBalanceFromReportFinancial;
    }

    public void setInitialBalanceFromReportFinancial(BigDecimal initialBalanceFromReportFinancial) {
        this.initialBalanceFromReportFinancial = initialBalanceFromReportFinancial;
    }
}
