package com.prcsteel.platform.smartmatch.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum ResourceOperType {

	INSERT("insert", "新增操作"), UPDATE("update", "更新操作");

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
	ResourceOperType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getNameByCode(String code) {
		Optional<ResourceOperType> res = Stream.of(ResourceOperType.values()).filter(a -> a.getCode().equals(code))
				.findFirst();
		return res.isPresent() ? res.get().getName() : "";
	}
}
