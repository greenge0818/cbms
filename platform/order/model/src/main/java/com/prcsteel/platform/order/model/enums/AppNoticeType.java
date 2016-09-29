package com.prcsteel.platform.order.model.enums;

/**
 * @Title: AppNoticeType.java
 * @Package com.prcsteel.platform.order.model.enums
 * @Description: APP短信通知类型
 * @author Green.Ge
 * @date 2015年9月16日 下午7:06:27
 * @version V1.0
 */
public enum AppNoticeType {
	Approve("审核交易单开单"),
	ApprovePass("交易单开单通过"),
	ApproveDeclined("交易单开单未通过"),
	ChargeSuccess("充值成功"),
	ApprovePayApply("审核付款申请"),
	PayApplyDeclined("付款申请未通过"),
	PayApplyPass("交易单已付款成功");
	// 成员变量
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	// 构造方法
	private AppNoticeType( String name) {
		this.name = name;
	}
}
