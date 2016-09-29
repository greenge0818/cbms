package com.prcsteel.platform.kuandao.model.dto.spdb;

import java.io.Serializable;

import com.csii.payment.client.core.MerchantSignVerify;

/**
 * 
 * @Title SPDBRequstParam.java
 * @Package com.prcsteel.platform.kuandao.dto
 * @Description 浦发请求参数类
 * @author zjshan
 *
 * @date 2016年5月27日 下午1:58:56
 */
public class SPDBRequstParam implements Serializable {

	private static final long serialVersionUID = 6250639296563100602L;
	
	private final String transName;
	
	private final String plain;
	
	private final String signature;

	public SPDBRequstParam(String transName, String plain){
		this.transName = transName;
		this.plain = plain.replaceAll(" ", "");
		this.signature = MerchantSignVerify.merchantSignData_ABA(plain);
	}
	public String getTransName() {
		return transName;
	}

	public String getPlain() {
		return plain;
	}

	public String getSignature() {
		return signature;
	}

}
