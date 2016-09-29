package com.prcsteel.platform.account.model.dto;

import com.prcsteel.platform.account.model.model.Account;

import java.math.BigDecimal;

/**
 * Created by Rabbit on 2016-4-19 11:31:08.
 */
public class AccountCreditDto extends Account {
    private BigDecimal groupCreditBalance;   //部门在信用额度组中的额度余额

    public BigDecimal getGroupCreditBalance() {
        return groupCreditBalance;
    }

    public void setGroupCreditBalance(BigDecimal groupCreditBalance) {
        this.groupCreditBalance = groupCreditBalance;
    }
}
