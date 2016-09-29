package com.prcsteel.platform.order.model.enums;

/**
 * @Title: Type.java
 * @Package com.prcsteel.cbms.persist.model
 * @author lixiang
 * @date 2015年7月28日 下午11:22:27
 * @version V1.0
 */

public enum Type {
	FIRST_PAYMENT("1", "初次付款"),
	SECONDARY_SETTLEMENT("2", "二次结算"),
	WITHDRAWALW("3", "账户提现"),
	WITHDRAWALW_FREEZE("4", "提现冻结"),
	WITHDRAWALW_THAW("5", "提现解冻"),
	WITHDRAWALW_CONFIRM("6", "提现确认"),
	RELATION("7", "关联"),
	DEDUCTIONS("8", "抵扣"),
	SECONDWITHDRAWALW("9", "二次结算提现冻结"),
	SECONDWITHDRAWALW_THAW("10", "二次结算提现解冻"),
	SECONDWITHDRAWALW_CONFIRM("11", "二次结算提现确认"),
	PAYMENT_CONFIRM("12", "付款确认"),
	ARRIVAL_EXCEPTION ("13", "到账异常"),
	SECONDARY_SETTLEMENT_FREEZE("14", "抵扣二次结算账户欠款冻结"),
	SECONDARY_SETTLEMENT_DIKB("15", "抵扣二次结算账户欠款解冻"),
	SECONDARY_SETTLEMENT_CONFIRM("16", "抵扣二次结算账户欠款确认"),
	SELLER_PAYMENT_APPLY("17", "卖家付款申请冻结"),
	SELLER_PAYMENT_THAW("18", "卖家付款申请解冻"),
	SELLER_PAYMENT_CONFIRM("19", "卖家付款申请确认"),
	SELLER_PAYMENT_OUT("20", "卖家付款申请转出"),
	SECONDWITHDRAWALW_OUT("21", "二次结算提现转出"),
	BUYER_SECONDARY_BACK("22", "买家二次结算回退"),
	SELLER_SECONDARY_BACK("23", "卖家二次结算回退"),
	BALANCES_BACK("24", "买家资金账户和二次结算回退"),
	ACCEPT_DRAFT_RECHARGE("25", "银票充值"),
	ACCEPT_DRAFT_BACK("26", "银票回退"),
	ACCEPT_DRAFT_LOCK("27", "锁定银票"),
	ACCEPT_DRAFT_UNLOCK("28", "解锁银票"),
	REBATE("29", "折让金额"),
	ROLLBACK_REBATE("30", "回滚折让金额"),
	ADVANCE_PAYMENT_CONFIRM("31", "预付款付款确认");

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
	private Type(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
