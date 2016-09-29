package org.prcsteel.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author zhoukun
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface RestMapping {

	String value();
	
	RequestMethod method() default RequestMethod.GET;
}
