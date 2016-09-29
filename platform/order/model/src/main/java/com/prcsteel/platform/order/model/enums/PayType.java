package com.prcsteel.platform.order.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @Title: ApplyType.java
 * @Package com.prcsteel.cbms.persist.model
 * @author lixiang
 * @date 2015年7月27日 下午22:22:27
 * @version V1.0
 */
public enum PayType {
	BALANCE("1", "余额"), 
	SETTLEMENT("2", "结算"), 
	FREEZE("3", "冻结"),
	ADVANCE_PAYMENT("4", "预付款"),
	ALLOWANCE_SETTLEMENT("5", "二次结算折让"),
	KUANDAO_PAYMENT("6","款道充值");

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
	private PayType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static PayType getItemByCode(String code) {
		Optional<PayType> res = Stream.of(PayType.values()).filter(a -> a.getCode().equals(code)).findFirst();
		return res.isPresent() ? res.get() : null;
	}
}
