package com.prcsteel.platform.account.model.model;

/**
 * @Title: AttachmentType.java
 * @Package com.prcsteel.cbms.persist.model
 * @Description: 客户业务类型
 * @author Green.Ge
 * @date 2015年7月15日 下午4:01:27
 * @version V1.0
 */
public enum AccountBusinessType {
	merchant("钢贸商"), 
	factory("钢厂"), 
	terminal("终端"),
	next_terminal("次终端"), 
	other("其他");
	// 成员变量
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	// 构造方法
	private AccountBusinessType( String name) {
		this.name = name;
	}
}
