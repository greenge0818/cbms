package com.prcsteel.platform.kuandao.model.dto.spdb;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Title BaseTransaction.java
 * @Package com.prcsteel.platform.kuandao.dto.spdb
 * @Description 浦发交易接口基础类<p><b>各接口报文务必继承<b/></p>
 * @author zjshan
 *
 * @date 2016年5月27日 下午4:25:06
 */
public class BaseTransaction implements Serializable{

	private static final Logger logger = LoggerFactory.getLogger(BaseTransaction.class);
	
	private static final long serialVersionUID = 6897346106451167698L;

	private String tranAbbr;
	
	private String mercCode;

	public String getTranAbbr() {
		return tranAbbr;
	}

	public void setTranAbbr(String tranAbbr) {
		this.tranAbbr = tranAbbr;
	}

	public String getMercCode() {
		return mercCode;
	}

	public void setMercCode(String mercCode) {
		this.mercCode = mercCode;
	}
	
	@Override
	public String toString(){
		StringBuilder plainText = new StringBuilder();

		PropertyDescriptor[] propDescs = PropertyUtils.getPropertyDescriptors(this.getClass());
		for(PropertyDescriptor propDesc : propDescs){
			String fieldName = propDesc.getName();
			if(PropertyUtils.isReadable(this, fieldName) && PropertyUtils.isWriteable(this, fieldName)){
				char[] charArr = fieldName.toCharArray();
				if(charArr[0] >  96){
					charArr[0] -= 32;
				}
				fieldName = String.valueOf(charArr);
				plainText.append(fieldName).append("=");
				Method readMethod = propDesc.getReadMethod();
				try {
					Object value = readMethod.invoke(this);
					if(value != null){
						plainText.append(value);
					}
				plainText.append("|");
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					logger.error("Class[" +this.getClass().getName() + "]", e);
				}
			}
			
		}
		return plainText.toString();
	}
}
