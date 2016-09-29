package org.prcsteel.rest.proxy;

import java.lang.reflect.Proxy;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

/**
 * 
 * @author zhoukun
 */
@Component("restApiFactoryBean")
public class RestApiFactoryBean {

	@Resource
	private RestInvocationHandler restInvocationHandler;
	
	public Object buildApi(Class<?> apiClass){
		return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{ apiClass }, restInvocationHandler);
	}
}
