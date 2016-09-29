package com.prcsteel.platform.order.model.enums;

/**
 * tuxianming
 * @author Administrator
 *
 */
public enum ReportBuyerInvoiceOutType {
	Approved("1", "关联合同"),
	SecondSettle("2", "二结"),
	CloseOrder("3", "订单关闭"),
	Discount("4", "销售调价"),
	DiscountRollback("5", "调价回滚"),
	Invoice("6", "开销项票");
	
	private String code;
	private String desc;
	
	private ReportBuyerInvoiceOutType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
