package com.prcsteel.platform.smartmatch.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/** 
 * 资源表资源来源类型
 * @author  peanut
 * @date 创建时间：2015年12月4日 上午10:49:23   
 */
public enum ResourceSourceType {
	/**
	 * 原有 -》 INQUIRY,//询价;
	 *	NORMAL,//新增;
	 *	UPLOAD //上传;
	 *
	 *  修改后 -》
	 *     inquiry -》 询价资源
	 *     normal -》新增资源  （新增后待审核）
	 *     upload -》上传导入的资源（新增后待审核）
	 *     daily_common-》日常资源 （已经审核的资源）
	 *     history_transaction （历史成交资源）
	 *     
	 *     新增与导入都归纳为日常资源 （已跟陈锋确认）
	 *     全部资源分成“日常资源、询价资源、历史成交”三个类型，是否异常两种状态；
	 */
		INQUIRY("inquiry","询价資源"),//询价;
		DAILY_COMMON("daily_common","日常资源"),
	    HISTORY_TRANSACTION("history_transaction", "历史成交资源")
		;//上传;

	
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
		ResourceSourceType(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public static String getNameByCode(String code) {
			Optional<QuotationOrderStatus> res = Stream.of(QuotationOrderStatus.values()).filter(a -> a.getCode().equals(code)).findFirst();
			return res.isPresent() ? res.get().getName() : "";
		}
	
		
		
}
