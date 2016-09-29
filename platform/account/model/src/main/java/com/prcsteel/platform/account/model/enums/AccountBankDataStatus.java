package com.prcsteel.platform.account.model.enums;

import java.util.*;
import java.util.stream.Stream;

/**
 * @Title 客户银行账号审核状态（打款资料）
 * @author kongbinheng
 * @date 2015-11-19 14:30:50
 * @version V1.2.5
 */
public enum AccountBankDataStatus {

	Requested("Requested","待审核"),
	Insufficient("Insufficient","未上传"), //资料不足
	Declined("Declined","审核未通过"),
	Approved("Approved","通过");//审核通过

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
	AccountBankDataStatus(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getNameByCode(String code) {
		Optional<AccountBankDataStatus> res = Stream.of(AccountBankDataStatus.values()).filter(a -> a.getCode().equals(code)).findFirst();
		return res.isPresent() ? res.get().getName() : "";
	}

	public static List<Map<String,String>> getList(){
		List<Map<String,String>> list = new ArrayList();
		List<AccountBankDataStatus> sourceList = Arrays.asList(Insufficient,Requested,Declined,Approved);
		Map<String,String> map ;
		for(AccountBankDataStatus status:sourceList){
			map = new HashMap<>();
			map.put("code",status.getCode());
			map.put("name",status.getName());
			list.add(map);
		}
		return list;
	}
}
