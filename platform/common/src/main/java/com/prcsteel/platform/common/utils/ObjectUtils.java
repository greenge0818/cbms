package com.prcsteel.platform.common.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ObjectUtils {
	/**
	 * 判断一个对象是否为基本数据类型。
	 * 
	 * @param obj 要判断的对象。
	 * @return true 表示为基本数据类型。
	 */
	public static boolean isBaseDataType(Object obj){
		if(obj==null){
			return false;
		}
		Class<? extends Object> clazz = obj.getClass();
		return (clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Byte.class)
				|| clazz.equals(Long.class) || clazz.equals(Double.class) || clazz.equals(Float.class)
				|| clazz.equals(Character.class) || clazz.equals(Short.class) || clazz.equals(BigDecimal.class)
				|| clazz.equals(BigInteger.class) || clazz.equals(Boolean.class) || clazz.equals(Date.class)
				|| clazz.isPrimitive());
	}
}
