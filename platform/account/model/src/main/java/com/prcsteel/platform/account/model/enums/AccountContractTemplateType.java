package com.prcsteel.platform.account.model.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 账户合同模版类型
 * @author peanut
 * @title AccountContractTemplateType.java
 * @package com.prcsteel.platform.account.model.enums
 * @description 账户合同模版类型
 * @version V1.0
 * @date  2016年3月17日 下午4:05:25
 */
public enum AccountContractTemplateType {
	BUYER("buyer","买家合同模板"),
	SELLER("seller","卖家合同模板"),
	FRMAE("frame","框架合同模板"),
	CHANNEL("channel","款道合同模板"),
	CONSIGN("consign","卖家代运营协议模板"),
	YEAR_PURCHASE("yearPurcharse","买家年度采购协议模板");

	private String code;
	// 成员变量
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
	private AccountContractTemplateType(String code, String name) {
		this.name = name;
		this.code=code;
	}

	public static AccountContractTemplateType getNameByCode(String code) {
		Optional<AccountContractTemplateType> res = Stream.of(AccountContractTemplateType.values()).filter(a -> a.getCode().equals(code)).findFirst();
		return res.isPresent() ? res.get() : null;
	}

	public static List<Map<String,String>> getList(){
		List<Map<String,String>> list = new ArrayList();
		List<AccountContractTemplateType> sourceList = Arrays.asList(BUYER,SELLER,FRMAE,CHANNEL);
		Map<String,String> map ;
		for(AccountContractTemplateType status:sourceList){
			map = new HashMap<>();
			map.put("code",status.getCode());
			map.put("name",status.getName().substring(0,4));
			list.add(map);
		}
		return list;
	}
}
