package com.prcsteel.platform.common.enums;
/**
 * 
 * @author zhoukun
 */
public enum OpLevel {
	Safe(100,"安全"),
	Warning(200,"警告"),
	Dangerous(300,"危险"),
	Damning(400,"毁灭");
	
	String description;
	int level;
	
	OpLevel(int level,String desc){
		this.description = desc;
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public int getLevel() {
		return level;
	}
	
}
