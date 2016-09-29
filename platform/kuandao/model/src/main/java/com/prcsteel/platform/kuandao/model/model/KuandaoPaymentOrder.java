package com.prcsteel.platform.kuandao.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class KuandaoPaymentOrder{
	/**主键*/
    private Integer id;
    /**支付单号*/
    private String paymentOrderCode;
    /**交易订单号外键*/
    private Long consignOrderId;
    /**交易单号*/
    private String consignOrderCode;
    /**汇入流水号*/
    private String impAcqSsn;
    /**产生方式 0：人工取号 1：系统自动匹配*/
    private Integer occurType;
    /**支付金额，单位：元*/
    private BigDecimal transactionAmount;
    /**收款会员号*/
    private String payeeMerId;
    /**收款会员名称*/
    private String payeeMerName;
    /**收款会员虚拟账号*/
    private String payeeMerVirAcctNo;
    /**付款会员号*/
    private String payMerId;
    /**付款会员名称*/
    private String payMerName;
    /**品名*/
    private String nsortName;
    /**订单重量*/
    private BigDecimal weight;
    /**状态  00-已匹配 01全部确认 02未匹配 03部分确认 04部分退货 05全部退货 06撤销 07部分退货、部分确认 08系统撤销 09处理中 11失败*/
    private String status;
    /**提交状态 0：待提交 1：已提交 2：待再次提交*/
    private Integer submitStatus;
    /**提交失败信息*/
    private String submitErrorMsg;
    /**网关流水*/
	private String acqSsn;
	 /**交易日期*/
	private String settDate;
	
	private Date created;
	
	private String createdBy;
	 
	private Date lastUpdated;

	private String lastUpdatedBy;

	private Integer modificationNumber;

	private String rowId;
	
	private String parentRowId;
	
	private Integer lastSubmitStatus;
	
	private String lastStatus;

	/**充值状态 0：待充值 1：已充值*/
	private Integer chargStatus;
	
	/**前一次充值状态 0：待充值 1：已充值*/
	private Integer lastChargStatus;
	/**充值时间*/
	private Date chargTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPaymentOrderCode() {
		return paymentOrderCode;
	}

	public void setPaymentOrderCode(String paymentOrderCode) {
		this.paymentOrderCode = paymentOrderCode;
	}

	public Long getConsignOrderId() {
		return consignOrderId;
	}

	public void setConsignOrderId(Long consignOrderId) {
		this.consignOrderId = consignOrderId;
	}

	public String getImpAcqSsn() {
		return impAcqSsn;
	}

	public void setImpAcqSsn(String impAcqSsn) {
		this.impAcqSsn = impAcqSsn;
	}

	public Integer getOccurType() {
		return occurType;
	}

	public void setOccurType(Integer occurType) {
		this.occurType = occurType;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSubmitStatus() {
		return submitStatus;
	}

	public void setSubmitStatus(Integer submitStatus) {
		this.submitStatus = submitStatus;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getAcqSsn() {
		return acqSsn;
	}

	public void setAcqSsn(String acqSsn) {
		this.acqSsn = acqSsn;
	}

	public String getSettDate() {
		return settDate;
	}

	public void setSettDate(String settDate) {
		this.settDate = settDate;
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
	
	public String getSubmitErrorMsg() {
		return submitErrorMsg;
	}

	public void setSubmitErrorMsg(String submitErrorMsg) {
		this.submitErrorMsg = submitErrorMsg;
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

	public String getConsignOrderCode() {
		return consignOrderCode;
	}

	public void setConsignOrderCode(String consignOrderCode) {
		this.consignOrderCode = consignOrderCode;
	}

	public String getPayeeMerVirAcctNo() {
		return payeeMerVirAcctNo;
	}

	public void setPayeeMerVirAcctNo(String payeeMerVirAcctNo) {
		this.payeeMerVirAcctNo = payeeMerVirAcctNo;
	}

	public Integer getLastSubmitStatus() {
		return lastSubmitStatus;
	}

	public void setLastSubmitStatus(Integer lastSubmitStatus) {
		this.lastSubmitStatus = lastSubmitStatus;
	}

	public String getLastStatus() {
		return lastStatus;
	}

	public void setLastStatus(String lastStatus) {
		this.lastStatus = lastStatus;
	}

	public Integer getChargStatus() {
		return chargStatus;
	}

	public void setChargStatus(Integer chargStatus) {
		this.chargStatus = chargStatus;
	}

	public Integer getLastChargStatus() {
		return lastChargStatus;
	}

	public void setLastChargStatus(Integer lastChargStatus) {
		this.lastChargStatus = lastChargStatus;
	}

	public Date getChargTime() {
		return chargTime;
	}

	public void setChargTime(Date chargTime) {
		this.chargTime = chargTime;
	}
	
}
