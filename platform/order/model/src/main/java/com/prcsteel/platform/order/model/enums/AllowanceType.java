package com.prcsteel.platform.order.model.enums;

public enum AllowanceType {
	Buyer("buyer","买家折让单"),
	Seller("seller","卖家折让单");
	
	// 成员变量
	private String key;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	// 构造方法
	private AllowanceType(String key, String name) {
		this.key = key;
		this.name = name;
	}
}
