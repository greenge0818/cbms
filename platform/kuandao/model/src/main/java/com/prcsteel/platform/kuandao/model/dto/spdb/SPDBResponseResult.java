package com.prcsteel.platform.kuandao.model.dto.spdb;

import java.io.Serializable;

/**
 * 
 * @Title SPDBResponseResult.java
 * @Package com.prcsteel.platform.kuandao.dto
 * @Description 浦发返回报文封装类
 * @author zjshan
 *
 * @date 2016年5月27日 下午4:21:24
 */
public class SPDBResponseResult implements Serializable {

	private static final long serialVersionUID = 4429010936988204001L;

	private boolean success;
	
	private String errCode;
	
	private String errMsg;
	
	private String plain;
	
	private String transName;
	
	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}


	public String getPlain() {
		return plain;
	}

	public void setPlain(String plain) {
		this.plain = plain;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}


}
