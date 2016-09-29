package com.prcsteel.platform.order.model.enums;

public enum ReportSellerInvoiceInOperationType {

	FirstPayment("firstPayment","初次付款"),
	SecondSettlement("secondSettlement","二结"),
	OrderClose("orderClose","订单关闭"),
	Allowance("allowance","采购调价单号：%s"),
	UnAllowance("unAllowance","调价回滚（采购调价单号：%s）"),
	InvoiceIn("invoiceIn","票号：%s"),
	UnInvoiceIn("UnInvoiceIn","票号：%s");
	
	// 成员变量
	private String operation;
	private String remark;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getRemark(String... args) {
		return String.format(remark, args);
	}

	// 构造方法
	private ReportSellerInvoiceInOperationType(String operation, String remark) {
		this.operation = operation;
		this.remark = remark;
	}
}
