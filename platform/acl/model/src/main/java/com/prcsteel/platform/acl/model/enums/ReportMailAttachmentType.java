package com.prcsteel.platform.acl.model.enums;

public enum ReportMailAttachmentType {

	DAILY("1", "CBMS系统代运营业务日报"), MONTHLY("2","CBMS系统代运营业务月报");

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
	private ReportMailAttachmentType(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
