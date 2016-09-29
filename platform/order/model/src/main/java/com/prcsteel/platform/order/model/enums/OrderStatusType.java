package com.prcsteel.platform.order.model.enums;
/**
 * 
 * @author zhoukun
 */
public enum OrderStatusType {
	MAIN, // 订单状态
    PAY,//付款状态
    SECONDPAY,// 二次结算付款
    PICKUP,// 提货状态
    FILLUP // 放货状态
}
