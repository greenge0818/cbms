package com.prcsteel.platform.order.model.enums;
/**
 * 
 * @author zhoukun
 */
public enum TransactionType {
	NORMAL,//正常
    UNPROCESSED,//未处理
    REFUND,//已处理，线下退款
    CHARGE;//已处理，新建账户并充值

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
