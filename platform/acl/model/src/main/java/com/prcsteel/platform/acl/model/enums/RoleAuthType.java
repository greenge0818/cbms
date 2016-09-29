package com.prcsteel.platform.acl.model.enums;
/**
 * 
 * @author zhoukun
 */
public enum RoleAuthType {
	ONLY_ME(0),        //仅自己
    ALL(1),            //所有
    SAME_LEVEL(2),     //同级+下线
    LOWER_LEVEL(3);     //下级

	RoleAuthType(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }
}
