package com.prcsteel.platform.kuandao.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountKuanDto implements Serializable {
	
	
	private static final long serialVersionUID = -3357724732555762341L;
	//客户编号
	private Long accountId;
	//充值金额
	private BigDecimal amount;
	//交易时间
	private  Long  transDate;
	//操作人员
	private String operatorName;
	//款道支付单号
	private	String kuandaoPayorderId;
	//操作人员id
	private  Long operatorId;


	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getTransDate() {
		return transDate;
	}

	public void setTransDate(Long transDate) {
		this.transDate = transDate;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getKuandaoPayorderId() {
		return kuandaoPayorderId;
	}

	public void setKuandaoPayorderId(String kuandaoPayorderId) {
		this.kuandaoPayorderId = kuandaoPayorderId;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
}
