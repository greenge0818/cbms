package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

import com.prcsteel.platform.order.model.model.PoolOut;

/**
 * Created by kongbinheng on 2015/8/4.
 */
public class PoolOutDto extends PoolOut {

    private BigDecimal surplusAmount;

    public BigDecimal getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(BigDecimal surplusAmount) {
        this.surplusAmount = surplusAmount;
    }
}
