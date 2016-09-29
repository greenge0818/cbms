package com.prcsteel.platform.smartmatch.service;

/**
 * 刷新缓存接口
 * @author prcsteel
 *
 */
public interface CacheManageService {
	/**
	 * 获取当前缓存的key值,多个用逗号隔开
	 * @return
	 */
	public String getCacheKey();
	/**
	 * 
	 * @param key
	 */
	public void refreshCache(String key);

}
