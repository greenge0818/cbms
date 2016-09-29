package com.prcsteel.platform.order.model.enums;

/**
 * @Title: AttachmentType.java
 * @Package com.prcsteel.cbms.persist.model
 * @Description: 提货方式
 * @author Green.Ge
 * @date 2015年7月22日 下午20点
 * @version V1.0
 */
public enum PickupType {
	CAR("凭车船号提货"), 
	IDCARD("身份证号提货"),
	TRANSFER("转货权提货方式"), 
	ORIGIN("原件提货方式");
	
	// 成员变量
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	// 构造方法
	private PickupType(String name) {
		this.name = name;
	}
}
