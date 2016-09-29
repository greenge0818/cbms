package com.prcsteel.platform.order.model.enums;
/**
 * 
 * @author zhoukun
 */
public enum ConsignOrderFillUpStatus {
	NO_PRINT_ALL(0, "未全打印"),
    PRINT_ALL(1, "全部打印"),
    ALL_MATCHES(2, "全部匹配"),
    INITIAL(-1, "初始化");

    private int code;
    private String name;

    ConsignOrderFillUpStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
