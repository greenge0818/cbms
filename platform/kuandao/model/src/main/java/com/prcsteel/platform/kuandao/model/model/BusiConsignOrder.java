package com.prcsteel.platform.kuandao.model.model;

import java.math.BigDecimal;

public class BusiConsignOrder {
	/**交易单id*/
	private Long consignOrderId;
	/**交易订单号*/
	private String code;
	/**客户编号id*/
	private Long acctId;
	/**客户姓名*/
	private String acctName;
	/**交易订单商品重量*/
	private BigDecimal weight;
	/**交易订单金额*/
	private BigDecimal amount;
	
	
	public Long getConsignOrderId() {
		return consignOrderId;
	}
	public void setConsignOrderId(Long consignOrderId) {
		this.consignOrderId = consignOrderId;
	}
	public Long getAcctId() {
		return acctId;
	}
	public void setAcctId(Long acctId) {
		this.acctId = acctId;
	}
	public String getAcctName() {
		return acctName;
	}
	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
}
