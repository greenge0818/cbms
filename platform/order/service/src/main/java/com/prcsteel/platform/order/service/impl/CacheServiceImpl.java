package com.prcsteel.platform.order.service.impl;

import java.io.IOException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.order.service.CacheService;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import org.springframework.stereotype.Service;

/**
 * Created by Green on 15-9-28.
 */
@Service("cacheServiceWeChat")
public class CacheServiceImpl implements CacheService {

    private static Logger LOG = LoggerFactory.getLogger(CacheServiceImpl.class);
	
    private MemcachedClient client;
    
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

//    private String getAddresses() {
//        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("cbms.properties");
//        Properties p = new Properties();
//        try {
//            p.load(inputStream);
//        } catch (Exception e1) {
//            LOG.error("error read web.properties", e1);
//        }
//        return p.getProperty("memcached.server.addressAndPort", memcacheAddressAndPort);
//    }

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

    @Override
    public String[] getPushInfoByUserLoginId(String loginId) {
        return new String[0];
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
