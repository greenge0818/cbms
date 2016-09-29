package com.prcsteel.platform.order.service;

import java.util.concurrent.Future;

import com.prcsteel.platform.order.model.AppUser;

/**
 * 
 * @author Green.Ge 09/28
 *
 */
public interface CacheService {
	/**
     * 保存缓存数据
     *
     * @param key:健
     * @param exp:超时时间
     * @param o:值,可以是任意对象(Object)
     */
    public Future<Boolean> set(String key, int exp, Object o);
    
    /**
     * 获取缓存数据
     *
     * @param key:健
     * @return
     */
    public Object get(String key);
    
    /**
     * 替换缓存中的数据
     *
     * @param key
     * @param exp
     * @param o
     * @return
     */
    public Future<Boolean> replace(String key, int exp, Object o);
    
    /**
     * 删除缓存
     *
     * @param key
     * @return
     */
    public Future<Boolean> delete(String key);
    
    //只刷新时间，不对值作修改
    public void touch(String key);
    
    public String[] getPushInfoByUserLoginId(String loginId);
	
//    public AppUser getUserByLoginId(String loginId);
}
