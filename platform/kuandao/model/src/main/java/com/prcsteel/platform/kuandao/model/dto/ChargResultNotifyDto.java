package com.prcsteel.platform.kuandao.model.dto;

import java.io.Serializable;

public class ChargResultNotifyDto implements Serializable {

	
	private static final long serialVersionUID = -123302824369293563L;
	
	/**款道支付单号*/
	private	Integer kuandaoPayorderId;
	/**客户编号*/
	private Long accountId;
	/**交易时间*/
	private  Long  transDate;
	/**充值结果 00：成功 01：失败*/
	private String status;
	/**签名 */
	private String sign;
	
	public Integer getKuandaoPayorderId() {
		return kuandaoPayorderId;
	}
	public void setKuandaoPayorderId(Integer kuandaoPayorderId) {
		this.kuandaoPayorderId = kuandaoPayorderId;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Long getTransDate() {
		return transDate;
	}
	public void setTransDate(Long transDate) {
		this.transDate = transDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}
