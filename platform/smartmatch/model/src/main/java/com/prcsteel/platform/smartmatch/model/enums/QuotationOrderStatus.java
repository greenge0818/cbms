package com.prcsteel.platform.smartmatch.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @Title: QuotationOrderStatus.java
 * @Package com.prcsteel.cbms.persist.model
 * @author Rabbit
 * @date 2015-12-25 10:51:25
 * @version V1.0
 */

public enum QuotationOrderStatus {
	UNCONFIRMED("UNCONFIRMED", "未确认报价"),
	CONFIRMED("CONFIRMED", "已确认报价");

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
	QuotationOrderStatus(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getNameByCode(String code) {
		Optional<QuotationOrderStatus> res = Stream.of(QuotationOrderStatus.values()).filter(a -> a.getCode().equals(code)).findFirst();
		return res.isPresent() ? res.get().getName() : "";
	}
	
}