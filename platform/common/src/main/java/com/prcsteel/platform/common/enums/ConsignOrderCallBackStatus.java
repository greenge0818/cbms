package com.prcsteel.platform.common.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @Title 订单打回状态
 * @author kongbinheng
 * @date 2015-11-18 11:25:34
 * @version V1.2.5
 */

public enum ConsignOrderCallBackStatus {

	Normal("Normal", "正常"),
	Back("Back", "打回");

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
	ConsignOrderCallBackStatus(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getNameByCode(String code) {
		Optional<ConsignOrderCallBackStatus> res = Stream.of(ConsignOrderCallBackStatus.values()).filter(a -> a.getCode().equals(code)).findFirst();
		return res.isPresent() ? res.get().getName() : "";
	}
}