package com.prcsteel.platform.smartmatch.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum ResourceException {

	EXCEPTION("exception", "异常"), 
	NORMAL("normal", "正常");

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
	ResourceException(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getNameByCode(String code) {
		Optional<QuotationOrderStatus> res = Stream.of(QuotationOrderStatus.values())
				.filter(a -> a.getCode().equals(code)).findFirst();
		return res.isPresent() ? res.get().getName() : "";
	}
}
