package com.prcsteel.platform.order.service;
/**
 * 读取系统配置
 * @author zhoukun
 */
public interface SysConfigService {

	int getInt(String key);
	
	double getDouble(String key);
	
	String getString(String key);
	
	long getLong(String key);
}
