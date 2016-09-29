package com.prcsteel.platform.kuandao.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class KuandaoDepositJournalDto implements Serializable{
	private static final long serialVersionUID = 7309186642666769241L;
	/**主键*/
	 private Integer id;
	 /**汇入流水号*/
     private String impAcqSsn;
     /**收款虚账号*/
	 private String payeeVirtualAcctNo;
	 /**收款虚账名称*/
	 private String payeeVirtualAcctName;
	 /**付款会员账号*/
	 private String payMerAcctNo;
	 /**付款会员名称*/
	 private String payMerName;
	 /**付款方开户行*/
	 private String payMerBranchId;
	 /**汇入日期*/
	 private String impDate;
	 /**汇入金额*/
	 private BigDecimal transactionAmount;
	 /**汇款状态 00：未匹配 01：已匹配 02：已退款*/
	 private String impStatus;
	 /**用途*/
	 private String purpose;
	 
	 private String RemitDetails;
	 
	 private Integer warnType;
	 
	 private Date created;
	 
	 private String createdBy;
	 
	 private Date lastUpdated;
	 
	 private String lastUpdatedBy;
	 
	 private Integer modificationNumber;
	 
	 private String rowId;
	 
	 private String parentRowId;
	 
	 private String paymentOrderCode;
	 
	 //网关流水
	private String acqSsn;
	 //下单时的入账日期
	private String settDate;
	 //退款或退货标识，用于接收页面参数反馈银行
	private Integer transactionType;
	 //退款单编号
	private String refundCode;

	private String startDate;
	 
	private String endDate;
	
	private Integer paymentOrderId;
	
	 
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

	public Integer getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

	public String getRefundCode() {
		return refundCode;
	}

	public void setRefundCode(String refundCode) {
		this.refundCode = refundCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImpAcqSsn() {
		return impAcqSsn;
	}

	public void setImpAcqSsn(String impAcqSsn) {
		this.impAcqSsn = impAcqSsn;
	}

	public String getPayeeVirtualAcctNo() {
		return payeeVirtualAcctNo;
	}

	public void setPayeeVirtualAcctNo(String payeeVirtualAcctNo) {
		this.payeeVirtualAcctNo = payeeVirtualAcctNo;
	}

	public String getPayeeVirtualAcctName() {
		return payeeVirtualAcctName;
	}

	public void setPayeeVirtualAcctName(String payeeVirtualAcctName) {
		this.payeeVirtualAcctName = payeeVirtualAcctName;
	}

	public String getPayMerAcctNo() {
		return payMerAcctNo;
	}

	public void setPayMerAcctNo(String payMerAcctNo) {
		this.payMerAcctNo = payMerAcctNo;
	}

	public String getPayMerName() {
		return payMerName;
	}

	public void setPayMerName(String payMerName) {
		this.payMerName = payMerName;
	}

	public String getPayMerBranchId() {
		return payMerBranchId;
	}

	public void setPayMerBranchId(String payMerBranchId) {
		this.payMerBranchId = payMerBranchId;
	}

	public String getImpDate() {
		return impDate;
	}

	public void setImpDate(String impDate) {
		this.impDate = impDate;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getImpStatus() {
		return impStatus;
	}

	public void setImpStatus(String impStatus) {
		this.impStatus = impStatus;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getRemitDetails() {
		return RemitDetails;
	}

	public void setRemitDetails(String remitDetails) {
		RemitDetails = remitDetails;
	}

	public Integer getWarnType() {
		return warnType;
	}

	public void setWarnType(Integer warnType) {
		this.warnType = warnType;
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

	public String getPaymentOrderCode() {
		return paymentOrderCode;
	}

	public void setPaymentOrderCode(String paymentOrderCode) {
		this.paymentOrderCode = paymentOrderCode;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getPaymentOrderId() {
		return paymentOrderId;
	}

	public void setPaymentOrderId(Integer paymentOrderId) {
		this.paymentOrderId = paymentOrderId;
	}
	 
	 
}
