package com.prcsteel.platform.kuandao.model.enums;

public enum AccountModifyStatusEnum {
	
	unmodify(0,"未修改"),namemodify(1,"客户名称被修改"),mobilemodify(2,"款道电话被修改"),nameandmobilemodify(3,"客户名称、款道电话被修改"),idnomodify(4,"组织机构代码被修改")
	,nameandidmodify(5,"客户名称、组织机构代码被修改"),mobileandidnomodify(6,"款道电话、组织机构代码被修改")
	,all(7,"客户名称、款道电话、组织机构代码被修改");
	
	
	private int status;
	
	private String text;
	
	private AccountModifyStatusEnum(int status,String text){
		this.status = status;
		this.text = text;
	}
	
	public int getStatus(){
		return this.status;
	}
	
	public String getText(){
		return this.text;
	}
}
