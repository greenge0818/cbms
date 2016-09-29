package com.prcsteel.platform.order.model.enums;

/**
 * Created by kongbinheng on 2015/7/30.
 */

public enum BankTransactionType {
	NORMAL("normal", "正常"),
	UNPROCESSED("unprocessed", "未处理"),
	REFUND("refund", "已处理-退款"),
	CHARGE("charge", "已处理-充值"),
	PENDING("pending", "待处理"),
	CHARGEMAN("chargeman", "已处理人工处理"),
	CHARGEHAND("chargehand", "已处理手动充值");

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
	private BankTransactionType(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
