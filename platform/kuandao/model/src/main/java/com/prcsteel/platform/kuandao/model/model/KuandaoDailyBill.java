package com.prcsteel.platform.kuandao.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class KuandaoDailyBill {
	private Integer id;
    private String impAcqSsn;
	private String payeeAcctNo;
	private String payeeVirtualAcctNo;
	private String payeeVirtualAcctName;
	private String payMerAcctNo;
	private String payMerName;
	private String payMerBranchId;
	private String isDebit;
	private String impDate;
	private BigDecimal transactionAmount;
	private BigDecimal balance;
	private String digestCode;
	private String paymentOrderCode;
	private String paymentStatus;
	private Date created;
	private String createdBy;
	private Date lastUpdated;
	private String lastUpdatedBy;
	private Integer modificationNumber;
	private String rowId;
	private String parentRowId;
	
	//交易柜员
	private String tellerId;
	//虚账户分摊日期
	private String virtualShareDate;
	//柜员流水号
	private String tellerSerialNo;
	//冲补标志
	private String correctionEntriesMark;
	//传票组内序号
	private String voucherNo;
		
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
	public String getPayeeAcctNo() {
		return payeeAcctNo;
	}
	public void setPayeeAcctNo(String payeeAcctNo) {
		this.payeeAcctNo = payeeAcctNo;
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
	public String getIsDebit() {
		return isDebit;
	}
	public void setIsDebit(String isDebit) {
		this.isDebit = isDebit;
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
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getDigestCode() {
		return digestCode;
	}
	public void setDigestCode(String digestCode) {
		this.digestCode = digestCode;
	}
	public String getPaymentOrderCode() {
		return paymentOrderCode;
	}
	public void setPaymentOrderCode(String paymentOrderCode) {
		this.paymentOrderCode = paymentOrderCode;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
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
	public String getTellerId() {
		return tellerId;
	}
	public void setTellerId(String tellerId) {
		this.tellerId = tellerId;
	}
	public String getVirtualShareDate() {
		return virtualShareDate;
	}
	public void setVirtualShareDate(String virtualShareDate) {
		this.virtualShareDate = virtualShareDate;
	}
	public String getTellerSerialNo() {
		return tellerSerialNo;
	}
	public void setTellerSerialNo(String tellerSerialNo) {
		this.tellerSerialNo = tellerSerialNo;
	}
	public String getCorrectionEntriesMark() {
		return correctionEntriesMark;
	}
	public void setCorrectionEntriesMark(String correctionEntriesMark) {
		this.correctionEntriesMark = correctionEntriesMark;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	
}
