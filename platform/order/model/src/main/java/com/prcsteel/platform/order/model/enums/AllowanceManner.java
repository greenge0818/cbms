package com.prcsteel.platform.order.model.enums;

/**
 * 
* @Title: AllowanceManner.java 
* @Package com.prcsteel.platform.order.model.enums
* @Description: 折让方式 
* @author lixiang   
* @date 2015年11月18日 上午9:10:46 
* @version V1.0
 */
public enum AllowanceManner {
	Weight("weight","重量"),
	Amount("amount","金额"),
	All("all","所有");
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
	private AllowanceManner(String key, String name) {
		this.key = key;
		this.name = name;
	}
}
