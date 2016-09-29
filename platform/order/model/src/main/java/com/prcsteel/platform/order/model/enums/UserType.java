package com.prcsteel.platform.order.model.enums;
/**
 * 
 * @author zhoukun
 */
public enum UserType {
	SALESMAN,     //交易员
    SERVER,    //内勤
    NOPERM,    //没有权限查看
    SUPERIOR;  //服务中心经理及以上

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
