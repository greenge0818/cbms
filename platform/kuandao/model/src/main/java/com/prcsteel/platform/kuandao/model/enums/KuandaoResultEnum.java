package com.prcsteel.platform.kuandao.model.enums;

public enum KuandaoResultEnum {
	
	nodata(0,"数据不存在"),
	success(1,"成功"),
	readonly(2,"只读"),
	duplicate(3,"重复数据"),
	limitBank(4,"受限行"),
	businesserror(-1,"业务逻辑处理失败"),
	systemerror(-2,"系统错误"),
	dataoperateerror(-3,"数据处理失败"),
	notnull(-4,"非空错误"),
	timeout(-999,"超时");

	private Integer code;
	
	private String text;
	
	private KuandaoResultEnum(Integer code,String text){
		this.code = code;
		this.text = text;
	}
	
	public Integer getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

	
}
