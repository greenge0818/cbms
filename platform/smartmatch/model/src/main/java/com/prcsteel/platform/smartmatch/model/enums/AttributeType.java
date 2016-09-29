package com.prcsteel.platform.smartmatch.model.enums;

/**
 * 属性类型
 * create by  peanut on 15-11-23
 *
 */
public enum AttributeType {
	ATTR_INPUT("input","文本"),
	ATTR_CHECKBOX("checkbox","多选"),
	ATTR_RADIO("radio","单选"),
	ATTR_SELECT("select","下拉选择");
	
	private String valEn;
	private String valCn;
	
	AttributeType(String valEn,String valCn){
		this.valEn=valEn;
		this.valCn=valCn;
	}
	public String getValEn() {
		return valEn;
	}
	public void setValEn(String valEn) {
		this.valEn = valEn;
	}
	public String getValCn() {
		return valCn;
	}
	public void setValCn(String valCn) {
		this.valCn = valCn;
	}
	/**
	 * 取得中文名
	 * @param valEn
	 * @return
	 */
	public static String getCnName(String valEn){
		for (AttributeType a: AttributeType.values()) {
            if (a.getValEn().equals(valEn)) {
                return a.getValCn();
            }
        }
		return "";
	}
}
