package com.prcsteel.platform.order.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 
* @Title: AllowanceStatus.java 
* @Package com.prcsteel.platform.order.model.enums
* @Description: 折让状态
* @author lixiang   
* @date 2015年11月18日 上午8:58:11 
* @version V1.0
 */
public enum AllowanceStatus {
	ToSubmit("to_submit","待提交"),
	ToAudit("to_audit","待审核"),
	Approved("approved","已审核"),
	NotThrough("not_through","未通过"),
	Closed("closed","已关闭");
	// 成员变量
	private String key;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	// 构造方法
	private AllowanceStatus(String key, String name) {
		this.key = key;
		this.name = name;
	}

	public static String getNameByKey(String key) {
		Optional<AllowanceStatus> res = Stream.of(AllowanceStatus.values()).filter(a -> a.getKey().equals(key)).findFirst();
		return res.isPresent() ? res.get().getName() : "";
	}
}
