package com.prcsteel.platform.common.service;

import com.prcsteel.platform.common.enums.OpType;

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
public @interface OpLog {
	/**
	 * 操作类型
	 */
	OpType value();
}
