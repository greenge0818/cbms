package com.prcsteel.platform.kuandao.common.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.prcsteel.platform.kuandao.model.dto.spdb.BaseTransaction;

/**
 * 
 * @Title BeanUtil.java
 * @Package com.prcsteel.platform.kuandao.common.util
 * @Description 报文转换为bean类
 * @author zjshan
 *
 * @date 2016年5月27日 下午4:15:23
 */
public class BeanUtil<T extends BaseTransaction> {
	
	private static final String DELIMIT = "|";
	private static final String DELIMITREGEX = "\\|";
	private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);
	
	public T stringToBean(String plain,Class<T> clazz){
		T bean = null;
		if(plain == null || plain.indexOf(DELIMIT) == -1){
			return bean;
		}
		
		try {
			Map<String, Object> map = getMap(plain);
			bean = clazz.newInstance();
			BeanUtils.copyProperties(bean,map);
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("parse String to bean failed",e);
		} catch (InvocationTargetException e) {
			logger.error("convert map to bean failed",e);
		} 
		return bean;
	}

	private Map<String, Object> getMap(String plain) {
		Map<String,Object> map = Maps.newHashMap();
		
		String[] entrys = plain.split(DELIMITREGEX);
		for(String entry : entrys){
			if(StringUtils.isNotEmpty(entry)){
				String[] keyValue = entry.split("=");
				String key = keyValue[0];
				if(keyValue.length != 2 || StringUtils.isEmpty(key)){
					logger.error("parse String to bean failed, there is empty entry in "+ plain);
					continue;
				}
				key = firstLetterLowCase(key);
				map.put(key, keyValue[1]);
			}
		}
		return map;
	}
	 
	private String firstLetterLowCase(String key){
		char[] charArr = key.toCharArray();
		if(charArr[0] <  92){
			charArr[0] += 32;
		}
		return String.valueOf(charArr);
	}

}
