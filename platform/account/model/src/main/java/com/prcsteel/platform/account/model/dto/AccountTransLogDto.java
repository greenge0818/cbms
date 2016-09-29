package com.prcsteel.platform.account.model.dto;

import java.math.BigDecimal;

import com.prcsteel.platform.account.model.model.AccountTransLog;

/**
 * Created by Administrator on 2015/8/7.
 */
public class AccountTransLogDto extends AccountTransLog {

    private String accountName;
    private BigDecimal balance;
    private BigDecimal balanceFreeze;
    private BigDecimal balanceSecondSettlement;
    private BigDecimal balanceSecondSettlementFreeze;
    /**
     * 记录总数 
     */
    private Integer total;
    
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;

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

    public BigDecimal getBalanceSecondSettlement() {
        return balanceSecondSettlement;
    }

    public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
        this.balanceSecondSettlement = balanceSecondSettlement;
    }

    public BigDecimal getBalanceSecondSettlementFreeze() {
        return balanceSecondSettlementFreeze;
    }

    public void setBalanceSecondSettlementFreeze(BigDecimal balanceSecondSettlementFreeze) {
        this.balanceSecondSettlementFreeze = balanceSecondSettlementFreeze;
    }

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
}
