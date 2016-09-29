package com.prcsteel.platform.order.service.impl;

import java.io.IOException;
import java.util.concurrent.Future;

import com.prcsteel.platform.common.constants.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.common.service.CacheService;
import com.prcsteel.platform.order.model.AppUser;
import com.prcsteel.platform.order.service.OrderCacheService;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

/**
 * Created by Green on 15-9-28.
 */
@Service("orderCacheService")
public class OrderCacheServiceImpl implements OrderCacheService {

    private static Logger LOG = LoggerFactory.getLogger(OrderCacheServiceImpl.class);
	
    private MemcachedClient client;
    
    private CacheServiceImpl cacheService;
    @Value("${memcached.server.addressAndPort}")
    private String memcacheAddressAndPort;

    public void init() {
        if (client == null) {
            try {
                client = new MemcachedClient(AddrUtil.getAddresses(memcacheAddressAndPort));
            } catch (IOException e) {
                LOG.error("can't init memcached client!", e);
            }
        }
    }

    public String[] getPushInfoByUserLoginId(String loginId){
    	String token = (String) get(loginId);
    	AppUser user=null;
    	if(token!=null){
    		user = (AppUser) get(token);
    	}else{
    		return null;
    	}
		if(user==null){
			return null;
		}

		//设备注册alise 推送目标
		return new String[]{user.getUser().getId()+"_"+user.getDeviceNo(),user.getDeviceType()};
	}


    /**
     * 保存缓存数据
     *
     * @param key:健
     * @param exp:超时时间
     * @param o:值,可以是任意对象(Object)
     */
    public Future<Boolean> set(String key, int exp, Object o) {
        client.delete(key);
        return client.set(key, exp, o);
    }

    /**
     * 获取缓存数据
     *
     * @param key:健
     * @return
     */
    public Object get(String key) {
        return client.get(key);
    }

    /**
     * 替换缓存中的数据
     *
     * @param key
     * @param exp
     * @param o
     * @return
     */
    public Future<Boolean> replace(String key, int exp, Object o) {
        return client.replace(key, exp, o);
    }

    /**
     * 删除缓存
     *
     * @param key
     * @return
     */
    public Future<Boolean> delete(String key) {
        return client.delete(key);
    }

    //只刷新时间，不对值作修改
    public void touch(String key){
        client.touch(key, Constant.MEMCACHESESSIONOUT);
    }

    /**
     * 得到缓存服务器客户端
     *
     * @return
     */
    public MemcachedClient getClient() {
        return client;
    }

    /**
     * 设置缓存服务器客户端
     *
     * @param client the client to set
     */
    public void setClient(MemcachedClient client) {
        this.client = client;
    }
    
}
