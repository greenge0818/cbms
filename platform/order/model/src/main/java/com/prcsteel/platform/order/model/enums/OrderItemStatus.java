package com.prcsteel.platform.order.model.enums;
/**
 * 
 * @author zhoukun
 */
public enum OrderItemStatus {
	TOBEINPUT, //都未录入
    SALESMANINPUT,  //交易员已录入
    SERVERINPUT,  //内勤已录入
    DEAL     //已匹配
}
