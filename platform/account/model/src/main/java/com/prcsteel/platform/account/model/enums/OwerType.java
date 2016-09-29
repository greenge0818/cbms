package com.prcsteel.platform.account.model.enums;

/**
 * 客户额度操作流水类型enum
 * 2016-04-19
 * @author afeng
 *
 */
public enum OwerType {
	    INSERT_GROUP("01","新建分组"),
	    EDIT_GROUP("02","编辑分组"),
	    DELETE_GROUP("03","删除分组"),
	    INSERT_CUST("04","新建公司"),
	    EDIT_CUST("05","编辑公司"),
	    DELETE_CUST("06","删除公司");

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
	    OwerType(String code, String name) {
	        this.name = name;
	        this.code=code;
	    }
}
