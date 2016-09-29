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
@Target(value={ElementType.PARAMETER})
public @interface UrlParam {

	/**
	 * URL参数名称
	 * 如果标记为一个Object对象，且设置了参数名，则会取此Object的toString方法，如果未指定参数名，则会遍历Object中的所有字段，替换URL中的点位符
	 * @return
	 */
	String value() default "";
}
