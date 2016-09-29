package com.prcsteel.platform.smartmatch.model.enums;

/** 
 * 资源计重方式
 * @author  peanut
 * @date 创建时间：2015年12月15日 下午4:15:20   
 */
public enum WeightConceptType {
	POUND("磅计"),//磅计
	METER("理计"),//理计	
	COPYCODE("抄码");//抄码
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	WeightConceptType(String name){
		this.name = name;
	}
}