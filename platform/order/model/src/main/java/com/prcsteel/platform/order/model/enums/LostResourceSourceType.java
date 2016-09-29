package com.prcsteel.platform.order.model.enums;
/** 
 * 缺失资源来源类型
 * @author peanut <p>2016年2月25日 上午10:43:43</p>  
 */
public enum LostResourceSourceType {
	NOLIMITED("","不限"),
	PURCHASEORDER("1","采购单"),
	HISTORYTRANSACTION("2","历史成交");
	
	private String value;
	private String name ;
	LostResourceSourceType(String value,String name ){
		this.value=value;
		this.name=name;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
