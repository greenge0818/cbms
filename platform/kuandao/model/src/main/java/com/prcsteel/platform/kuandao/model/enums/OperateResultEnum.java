package com.prcsteel.platform.kuandao.model.enums;

public enum OperateResultEnum {
	
	success(1,"成功"),fail(0,"失败");
	
	private int result;
	
	private String description;
	
	private OperateResultEnum(int result,String description){
		this.result = result;
		this.description = description;
	}

	public int getResult(){
		return this.result;
	}
	
	public String getDescription(){
		return this.description;
	}
}
