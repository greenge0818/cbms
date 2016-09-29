package com.prcsteel.platform.common.service;

import com.prcsteel.platform.common.service.OpParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author zhoukun
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OpParamGroup {
	OpParam[] value();
}
