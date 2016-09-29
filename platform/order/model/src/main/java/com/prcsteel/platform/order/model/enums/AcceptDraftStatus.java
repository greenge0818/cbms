package com.prcsteel.platform.order.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @Title: AcceptDraftStatus.java
 * @Package com.prcsteel.cbms.persist.model
 * @author Rabbit
 * @date 2015-11-11 11:25:34
 * @version V1.0
 */

public enum AcceptDraftStatus {
	NEW("NEW", "待提交审核"),
	SUBMITTED("SUBMITTED", "已申请充值，待审核"),
	CHARGED("CHARGED", "已完成充值"),
	ROLLBACKREQUEST("ROLLBACKREQUEST", "已申请取消充值，待审核"),
	RENEW("RENEW", "取消充值已完成");

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
	AcceptDraftStatus(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getNameByCode(String code) {
		Optional<AcceptDraftStatus> res = Stream.of(AcceptDraftStatus.values()).filter(a -> a.getCode().equals(code)).findFirst();
		return res.isPresent() ? res.get().getName() : "";
	}
	
}