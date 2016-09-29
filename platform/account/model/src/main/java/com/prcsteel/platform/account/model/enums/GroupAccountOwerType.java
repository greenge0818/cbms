package com.prcsteel.platform.account.model.enums;

/**
 * 客户额度操作流水类型enum
 * 2016-04-20
 * @author caochao
 *
 */
public enum GroupAccountOwerType {
	    INSERT("01","新增"),
	    EDIT("02","编辑");

	    private String code;
	    // 成员变量
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
	    GroupAccountOwerType(String code, String name) {
	        this.name = name;
	        this.code=code;
	    }
}
