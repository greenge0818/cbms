package com.prcsteel.platform.order.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @Title: AssociationType.java
 * @Package com.prcsteel.cbms.persist.model
 * @author lixiang
 * @date 2015年7月26日 下午17:25:27
 * @version V1.0
 */

public enum AssociationType {
	
	PAYMEN_ORDER("1", "付款单号"),
	ORDER_CODE("2", "交易单号"),
	BANK_OF_SERIAL_NUMBER("3", "银行流水号"), 
	REIMBUSEMENT_SERIAL_NUMBER("4", "还款流水号"),
	ACCEPT_DRAFT("5", "银票票号"),
	REBATE("6", "折让单号"),
	MONEYBACK("7","资金撤回"),
	MONEYALLOCATION("8","资金分配"),
	CREDIT_SERIAL("9","信用流水"),
	KUANDAO_PAYMENT("10","款道充值");

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
	private AssociationType(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public static String getNameByCode(String code) {
		Optional<AssociationType> res = Stream.of(AssociationType.values()).filter(a -> a.getCode().equals(code)).findFirst();
		return res.isPresent() ? res.get().getName() : "";
	}

	public static AssociationType getItemByCode(String code) {
		Optional<AssociationType> res = Stream.of(AssociationType.values()).filter(a -> a.getCode().equals(code)).findFirst();
		return res.isPresent() ? res.get() : null;
	}
}
