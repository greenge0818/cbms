package com.prcsteel.platform.kuandao.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentOrderSynchLog{
	/**主键*/
	private Integer id;
	/**交易单号*/
	private String consignOrderCode;
	/**支付单号*/
	private String paymentOrderCode;
	/**产生方式 0：人工取号 1：系统自动匹配*/
	private Integer occurType;
	/**付款会员号*/
	private String payMerId;
	/**付款会员名称*/
  	private String payMerName;
  	/**收款会员号*/
	private String payeeMerId;
	/**收款会员名称*/
  	private String payeeMerName;
  	/**品名*/
	private String nsortName;
	/**订单重量*/
	private  BigDecimal weight;
	/**支付金额，单位：元*/
	private BigDecimal transactionAmount;
	/**处理结果 0：同步失败 1：同步成功*/
	private Integer result;
	/**错误信息*/
	private String errorMsg;

	private Date created;
	
	private String createdBy;
	
	private Date lastUpdated;
	
	private String lastUpdatedBy;
	
	private Integer modificationNumber;
	
	private String rowId;
	
	private String parentRowId;
	
	/**卖方虚拟号*/
	private String payeeMerVirAcctNo;
	/**支付单编号*/
	private Integer paymentOrderId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConsignOrderCode() {
		return consignOrderCode;
	}

	public void setConsignOrderCode(String consignOrderCode) {
		this.consignOrderCode = consignOrderCode;
	}

	public String getPaymentOrderCode() {
		return paymentOrderCode;
	}

	public void setPaymentOrderCode(String paymentOrderCode) {
		this.paymentOrderCode = paymentOrderCode;
	}

	public Integer getOccurType() {
		return occurType;
	}

	public void setOccurType(Integer occurType) {
		this.occurType = occurType;
	}

	public String getPayMerId() {
		return payMerId;
	}

	public void setPayMerId(String payMerId) {
		this.payMerId = payMerId;
	}

	public String getPayMerName() {
		return payMerName;
	}

	public void setPayMerName(String payMerName) {
		this.payMerName = payMerName;
	}

	public String getPayeeMerId() {
		return payeeMerId;
	}

	public void setPayeeMerId(String payeeMerId) {
		this.payeeMerId = payeeMerId;
	}

	public String getPayeeMerName() {
		return payeeMerName;
	}

	public void setPayeeMerName(String payeeMerName) {
		this.payeeMerName = payeeMerName;
	}

	public String getNsortName() {
		return nsortName;
	}

	public void setNsortName(String nsortName) {
		this.nsortName = nsortName;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Integer getModificationNumber() {
		return modificationNumber;
	}

	public void setModificationNumber(Integer modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getParentRowId() {
		return parentRowId;
	}

	public void setParentRowId(String parentRowId) {
		this.parentRowId = parentRowId;
	}

	public String getPayeeMerVirAcctNo() {
		return payeeMerVirAcctNo;
	}

	public void setPayeeMerVirAcctNo(String payeeMerVirAcctNo) {
		this.payeeMerVirAcctNo = payeeMerVirAcctNo;
	}

	public Integer getPaymentOrderId() {
		return paymentOrderId;
	}

	public void setPaymentOrderId(Integer paymentOrderId) {
		this.paymentOrderId = paymentOrderId;
	}
	
	
}