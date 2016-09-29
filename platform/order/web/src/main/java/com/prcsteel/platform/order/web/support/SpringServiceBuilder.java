package com.prcsteel.platform.order.web.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.prcsteel.framework.nido.core.ServiceBuilder;

/**
 * 
 * @author zhoukun
 */
@Component("springServiceBuilder")
public class SpringServiceBuilder implements ServiceBuilder,ApplicationContextAware {

	ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public Object build(String clsName, Class<?> cls) {
		try{
			return applicationContext.getBean(clsName);
		}catch(NoSuchBeanDefinitionException e){
			if(cls != null){
				return applicationContext.getBean(cls);
			}
		}
		return null;
	}

}
