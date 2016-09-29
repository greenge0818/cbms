package com.prcsteel.platform.acl.model.enums;
/**
 * 
 * @author zhoukun
 */
public enum SysSettingDiscription {
    Invoice("Invoice", "销项发票单张上限额度"),
    Transaction("Transaction", "到账异常黑名单");

    // 成员变量
    private String code;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // 构造方法
    private SysSettingDiscription(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
