package com.prcsteel.platform.order.model.enums;
/**
 * 
 * @author zhoukun
 */
public enum PayStatus {
	REQUESTED,    //已申请/待审核
    APPROVED,     //已通过审核
    APPLYPRINTED, //已打印申请单
    CONFIRMED,    //已确认
    DECLINED,     //审核不通过
    CONFIRMEDPAY, //已确认付款
    ABANDONED,     //已作废
    CLOSED //已关闭
}
