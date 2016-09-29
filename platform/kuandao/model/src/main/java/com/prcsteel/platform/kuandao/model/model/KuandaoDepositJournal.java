package com.prcsteel.platform.kuandao.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class KuandaoDepositJournal {
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

	 private String lastImpStatus;
	 
	 
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

	public String getLastImpStatus() {
		return lastImpStatus;
	}

	public void setLastImpStatus(String lastImpStatus) {
		this.lastImpStatus = lastImpStatus;
	}
	 
	 
}
