package org.prcsteel.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author zhoukun
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface RestApi {

	/**
	 * 注入到spring中的bean 名称,默认为接口名
	 * @return
	 */
	String value() default "";
	
	/**
	 * REST服务器配置
	 * @return
	 */
	String restServer();
}
