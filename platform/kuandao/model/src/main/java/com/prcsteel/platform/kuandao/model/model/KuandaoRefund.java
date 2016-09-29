package com.prcsteel.platform.kuandao.model.model;

import java.math.BigDecimal;
import java.util.Date;
public class KuandaoRefund {
	private Integer id;
	//汇入记录Id
    private Integer impId;
    //退款单编号
    private String refundCode;
    //退款原因
    private String refundReason;
    //退货标识 0：退货 1：退款
    private Integer transactionType;
    //原网关流水号
    private String oacqSsn;
    //浦发返回网关流水
    private String acqSsn;
    //入账日期
    private String osttDate;
    //退款金额
    private BigDecimal transactionAmount;
    //终端号
    private String termCode;
    //行方交易日期
    private String settDate;
    //交易结果 0：交易失败 1：交易成功
    private Integer result;
	private Date created;
	private String createdBy;
    private Date lastUpdated;
    private String lastUpdatedBy;
    private Integer modificationNumber;
    private String rowId;
    private String parentRowId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getImpId() {
		return impId;
	}
	public void setImpId(Integer impId) {
		this.impId = impId;
	}
	public String getRefundCode() {
		return refundCode;
	}
	public void setRefundCode(String refundCode) {
		this.refundCode = refundCode;
	}
	public String getRefundReason() {
		return refundReason;
	}
	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}
	public Integer getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}
	public String getOacqSsn() {
		return oacqSsn;
	}
	public void setOacqSsn(String oacqSsn) {
		this.oacqSsn = oacqSsn;
	}
	public String getAcqSsn() {
		return acqSsn;
	}
	public void setAcqSsn(String acqSsn) {
		this.acqSsn = acqSsn;
	}
	public String getOsttDate() {
		return osttDate;
	}
	public void setOsttDate(String osttDate) {
		this.osttDate = osttDate;
	}
	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getTermCode() {
		return termCode;
	}
	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}
	public String getSettDate() {
		return settDate;
	}
	public void setSettDate(String settDate) {
		this.settDate = settDate;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
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
    
    
}
