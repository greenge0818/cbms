package com.prcsteel.platform.order.web.support;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import org.apache.log4j.Logger;

import com.prcsteel.platform.common.constants.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Created by rolyer on 15-8-21.
 */
public class CbmsMemcached {

    private static Logger LOG = Logger.getLogger(CbmsMemcached.class);
    private static CbmsMemcached _instance = null;

    private MemcachedClient client;

    public static synchronized CbmsMemcached getInstance() {
        if (_instance == null) {
            _instance = new CbmsMemcached();
        }
        return _instance;
    }

    public CbmsMemcached() {
        if (client == null) {
            try {
                client = new MemcachedClient(AddrUtil.getAddresses(getAddresses()));
            } catch (IOException e) {
                LOG.error("can't init memcached client!", e);
            }
        }
    }

    private String getAddresses() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("cbms.properties");
        Properties p = new Properties();
        try {
            p.load(inputStream);
        } catch (IOException e1) {
            LOG.error("error read web.properties", e1);
        }
        return p.getProperty("memcached.server.addressAndPort", "localhost:11211");
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


//    public static void main(String[] args) throws IOException {
//        //MemcachedClient client = new MemcachedClient(AddrUtil.getAddresses("localhost:11211"));
//		//client.set("我是KEY", 10, "我是存储中的值");
//
//        CbmsMemcached.getInstance().set("我是KEY", 1*60, "我是存储中的值");
//
//		System.out.println(CbmsMemcached.getInstance().get("我是KEY"));
//    }
}
