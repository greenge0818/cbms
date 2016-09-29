package com.prcsteel.platform.acl.model.enums;
/**
 * 
 * @author Green.Ge
 * @date 09/22
 * @description 角色类型
 */
public enum RoleType {
	CEO("CEO"),
	COO("COO"),
	CFO("CFO"),
	Manager("服务中心总经理"),
	Server("内勤"),
	Trader("交易员"),
	Casher("出纳"),
	LeadCasher("总出纳"),
	Accounter("核算会计"),
	//add by caosulin@prcsteel.com 20160601 增加网销人员和网销主管
	NetSaler("网销人员"),
	NetSalesManager("网销主管");
	
	private String name;
	private RoleType(String name) {
		this.name=name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
