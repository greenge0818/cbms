package com.prcsteel.platform.account.model.enums;

/**
 * （客户）合同模板状态
 * Created by rolyer on 16-1-20.
 */
public enum ContractTemplateStatus {
	PENDING("PENDING","待审核"),
	APPROVED("APPROVED","审核通过"),
	DISAPPROVED("DISAPPROVED","审核未通过");

	private String value;
	private String desc;

	/**
	 * 构造方法
	 * @param value
	 * @param desc
	 */
	private ContractTemplateStatus(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
