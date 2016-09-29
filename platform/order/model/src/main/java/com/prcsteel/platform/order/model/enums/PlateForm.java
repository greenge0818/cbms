package com.prcsteel.platform.order.model.enums;
/**
 * 操作平台
 * @author Green.Ge
 */
public enum PlateForm {
	Web("网站"),
	App("手机");
	String name;
	PlateForm(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
