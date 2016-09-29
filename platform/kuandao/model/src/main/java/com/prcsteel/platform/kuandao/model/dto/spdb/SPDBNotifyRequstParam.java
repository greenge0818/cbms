package com.prcsteel.platform.kuandao.model.dto.spdb;

import java.io.Serializable;

/**
 * 
 * @Title SPDBNotifyRequstParam.java
 * @Package com.prcsteel.platform.kuandao.dto
 * @Description 浦发通知请求参数类
 * @author zjshan
 *
 * @date 2016年5月27日 下午1:58:56
 */
public class SPDBNotifyRequstParam implements Serializable {

	private static final long serialVersionUID = 6250639296563100602L;
	
	private String transName;
	
	private String Plain;
	
	private String Signature;

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public String getPlain() {
		return Plain;
	}

	public void setPlain(String plain) {
		Plain = plain;
	}

	public String getSignature() {
		return Signature;
	}

	public void setSignature(String signature) {
		Signature = signature;
	}


}
