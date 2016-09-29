package com.prcsteel.platform.order.web.vo;

import java.math.BigDecimal;

/**
 * Created by caochao on 2015/8/24.
 */
public class BalanceBeginAndEndVo {
    private BigDecimal beginBalance;
    private BigDecimal endBalance;

    public BigDecimal getBeginBalance() {
        return beginBalance;
    }

    public void setBeginBalance(BigDecimal beginBalance) {
        this.beginBalance = beginBalance;
    }

    public BigDecimal getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(BigDecimal endBalance) {
        this.endBalance = endBalance;
    }
}
