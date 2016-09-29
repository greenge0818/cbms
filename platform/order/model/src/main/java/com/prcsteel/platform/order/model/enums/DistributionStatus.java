package com.prcsteel.platform.order.model.enums;

public enum DistributionStatus {

	Sure("0","可分配"),
	Lready("1","已分配"),
	Not("-1","不可分配"),
	
	Yes("1","是"),
	No("0","否");
	
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

	private String code;

	// 构造方法
	private DistributionStatus(String code, String name) {
		this.name = name;
		this.code = code;
		}
}
