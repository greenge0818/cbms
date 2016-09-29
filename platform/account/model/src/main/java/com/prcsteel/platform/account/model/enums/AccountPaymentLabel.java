package com.prcsteel.platform.account.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @Title 客户预付款标示1-银票预付2-现金预付3-无预付
 * @author kongbinheng
 * @date 2016-04-11 16:47:50
 * @version V2.0.10
 */
public enum AccountPaymentLabel {

	DraftsInAdvance ("1", "银票预付"),
	CashInAdvance("2", "现金预付"),
	NoAdvance("3", "无预付");

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
	AccountPaymentLabel(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getNameByCode(String code) {
		Optional<AccountPaymentLabel> res = Stream.of(AccountPaymentLabel.values()).filter(a -> a.getCode().equals(code)).findFirst();
		return res.isPresent() ? res.get().getName() : "";
	}
}
