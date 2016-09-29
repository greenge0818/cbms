package com.prcsteel.platform.smartmatch.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum ResourceStatus {
	/**
	 * 原有status对应代码： '1' : '挂牌'	
              		   '0' : '未挂牌'
                       '-1': '异常'
                       '2' : '历史成交'
       	修改后，挂牌(已审核)-》hang_up
       	              未挂牌 (未审核)-》 un_hang_up
       	              异常状态 -》abnormal_state
       	    历史成交放在resourceType中
	 */ 
	APPROVED("approved", "审核通过"), 
	DECLINED("declined", "审核未通过");

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
	ResourceStatus(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getNameByCode(String code) {
		Optional<QuotationOrderStatus> res = Stream.of(QuotationOrderStatus.values())
				.filter(a -> a.getCode().equals(code)).findFirst();
		return res.isPresent() ? res.get().getName() : "";
	}
}
