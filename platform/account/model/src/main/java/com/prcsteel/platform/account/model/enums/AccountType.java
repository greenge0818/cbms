package com.prcsteel.platform.account.model.enums;

/**
 * @Title: AccountType.java
 * @Package com.prcsteel.cbms.persist.model
 * @Description: 客户类型
 * @author Green.Ge
 * @date 2015年7月15日 下午4:01:27
 * @version V1.0
 */
public enum AccountType {
	buyer("买家"),
	seller("卖家"),
	both("两者都是");
	// 成员变量
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	// 构造方法
	private AccountType( String name) {
		this.name = name;
	}
}
