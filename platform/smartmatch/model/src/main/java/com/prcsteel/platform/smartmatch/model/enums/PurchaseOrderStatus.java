package com.prcsteel.platform.smartmatch.model.enums;

/**
 * 询价单状态
 * Created by Rabbit on 2015-12-11 14:28:00.
 */

public enum PurchaseOrderStatus {
	//modify by caosulin 增加待受理，待业务员处理，待网销主管分配，待服总分配 状态
    PENDING_QUOTE("PENDING_QUOTE", "待报价"),
    PENDING_ACCEPTE ("PENDING_ACCEPTE", "待受理"),
    PENDING_CLERK_ACCEPTE("PENDING_CLERK_ACCEPTE", "待业务员处理"),
    PENDING_DIRECTOR_ASSIGNED("PENDING_DIRECTOR_ASSIGNED", "待网销主管分配"),
    PENDING_SERVER_MANAGER_ASSIGNED("PENDING_SERVER_MANAGER_ASSIGNED", "待服总分配"),
    QUOTED("QUOTED", "已报价"),
    BILLED("BILLED", "已开单"),
    PENDING_OPEN_BILL("PENDING_OPEN_BILL", "待开单"),
    CLOSED("CLOSED", "已关闭"),
    QUOTED_CLOSED("QUOTED_CLOSED","已报价已关闭"); 

    private String code;
    private String name;

    PurchaseOrderStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}