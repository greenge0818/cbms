package com.prcsteel.platform.order.web.support;

/**
 * excel 样式
 * @author tuxianming
 * @date 20160622
 */
public class ObjectExcelStyle {
	
	public static ObjectExcelStyle build(){
		return new ObjectExcelStyle();
	}
	
	public enum StyleType{  //定义你要给单元格设置的样式类型
		FONT_COLOR;
	}
	
	private StyleType type; //
	private Object value;	//样式值
	
	//单元格所在的位置，  x = col, y=row
	private int x;		//excel cell point
	private int y;		//excel cell point
	
	public StyleType getType() {
		return type;
	}
	public ObjectExcelStyle setType(StyleType type) {
		this.type = type;
		return this;
	}
	public Object getValue() {
		return value;
	}
	public ObjectExcelStyle setValue(Object value) {
		this.value = value;
		return this;
	}
	public int getX() {
		return x;
	}
	public ObjectExcelStyle setX(int x) {
		this.x = x;
		return this;
	}
	public int getY() {
		return y;
	}
	public ObjectExcelStyle setY(int y) {
		this.y = y;
		return this;
	}
	
}
