package com.prcsteel.platform.order.model.enums;
/**
 * 
 * @author zhoukun
 */
public enum RebateType {
	REBATE,//返利
	ENCHASH;//提现

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
