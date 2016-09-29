package com.prcsteel.platform.kuandao.model.enums;

public enum OperateTypeEnum {
	
	manual(0,"人工"),automatic(1,"系统自动");

	private int type;
	
	private String description;
	
	private OperateTypeEnum(int type,String description){
		this.type = type;
		this.description = description;
	}
	
	public int getType(){
		return this.type;
	}
	
	public String getDescription(){
		return this.description;
	}
}
