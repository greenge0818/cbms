package org.prcsteel.rest.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;

/**
 * 
 * @author zhoukun
 */
public class ReflectMethodInfo {

	private RestApi restApi;
	
	private Method method;
	
	private Parameter[] urlParams;
	
	private Parameter bodyParameter;
	
	private Class<?> returnType;// 如果是VOID，该字段为空
}
