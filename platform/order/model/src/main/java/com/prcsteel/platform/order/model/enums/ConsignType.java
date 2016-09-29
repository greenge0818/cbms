package com.prcsteel.platform.order.model.enums;

/**
 * @Title: AttachmentType.java
 * @Package com.prcsteel.cbms.persist.model
 * @Description: 卖家类型
 * @author Green.Ge
 * @date 2015年7月15日 下午4:01:27
 * @version V1.0
 */
public enum ConsignType {
	consign("代运营"),
	temp("临采");
	
	// 成员变量
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	// 构造方法
	private ConsignType(String name) {
		this.name = name;
	}
}
