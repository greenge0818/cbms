package com.prcsteel.platform.account.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @Title 客户银行账号是否提醒
 * @author kongbinheng
 * @date 2015-11-23 14:30:50
 * @version V1.2.5
 */
public enum AccountBankDataReminded {

	Yes("Yes","提醒"),
	No("No","不提醒");

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
	AccountBankDataReminded(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getNameByCode(String code) {
		Optional<AccountBankDataReminded> res = Stream.of(AccountBankDataReminded.values()).filter(a -> a.getCode().equals(code)).findFirst();
		return res.isPresent() ? res.get().getName() : "";
	}
}
