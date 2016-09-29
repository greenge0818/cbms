package com.prcsteel.platform.common.service.impl;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.service.CacheService;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Created by Rolyer on 2016/6/22.
 */
@Service("commonCacheServiceImpl")
public class CommonCacheServiceImpl implements CacheService {

    private static Logger LOG = LoggerFactory.getLogger(CommonCacheServiceImpl.class);

    private MemcachedClient client;

    @Value("${memcached.server.addressAndPort}")
    private String memcacheAddressAndPort = "localhost:11211";

    public void init() {
        if (client == null) {
            try {
                client = new MemcachedClient(AddrUtil.getAddresses(memcacheAddressAndPort));
            } catch (IOException e) {
                LOG.error("can't init memcached client!", e);
            }
        }
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

