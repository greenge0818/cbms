package com.prcsteel.platform.order.model.enums;

/**
 * @Title: Status.java
 * @Package com.prcsteel.cbms.persist.model
 * @author lixiang
 * @date 2015年7月28日 下午11:22:27
 * @version V1.0
 */

public enum Status {

	REQUESTED("REQUESTED", "已申请 待审核"), APPROVED("APPROVED", "已通过审核"), DECLINED("DECLINED", "未通过"),CONFIRMEDPAY("CONFIRMEDPAY","已确认付款"),APPLYPRINTED("APPLYPRINTED","已打印付款申请单");

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
	private Status(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
