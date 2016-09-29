package com.prcsteel.platform.smartmatch.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.smartmatch.service.CacheManageService;
import com.prcsteel.platform.smartmatch.service.MemReFreshService;

/**
 * 刷新缓存服务层实现类
 * @author prcsteel
 *
 */
@Service("memReFreshService")
public class MemReFreshServiceImpl implements MemReFreshService,ApplicationContextAware ,InitializingBean{

	//spring 上下文
	private ApplicationContext applicationContext = null;
	
	//所有的实现了刷新缓存的接口的bean
	private Map<String,CacheManageService> cachesBeans = new HashMap<String,CacheManageService>();
	
	@Override
	public void refresh(String key) {
		if(key == null){
			return;
		}
		int count = 0 ;
		if(!cachesBeans.isEmpty()){
			for(String mapKey : cachesBeans.keySet()){
				CacheManageService service = cachesBeans.get(mapKey);
				if(key.equals(mapKey)){
					service.refreshCache(key);
					count ++;
				}
			}
		}
		
		if(count == 0){
			throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "没有找到当前key值对应的缓存!");
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String,CacheManageService> beans = this.applicationContext.getBeansOfType(CacheManageService.class);
		if(beans != null){
			for(String beanName : beans.keySet()){
				CacheManageService service = beans.get(beanName);
				if(service.getCacheKey() != null){
					String []  keys= service.getCacheKey().split(",");
					if(keys.length > 0){
						for(String key : keys){
							if(this.cachesBeans.keySet().contains(key)){
								throw new RuntimeException("当前缓存的key值重复：" + key);
							}
							this.cachesBeans.put(key, service);
						}
					}
				}
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
