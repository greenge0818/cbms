package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class CustAccountTransLogDto {
	private Long tid;

	private Long accountId;

	private String consignOrderCode;

	private String serialNumber;

	private String applyType;

	private BigDecimal amount;

	private Long applyerId;

	private String applyerName;

	private Date serialTime;

	private String type;

	private BigDecimal currentBalance;

	private String payType;

	private Date created;

	private Long cid;

	private String cName;

	private BigDecimal balance;

	private BigDecimal balanceSecondSettlement;

	private String associationType;
	
	private Long oid;
	
	private String oName;

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	public String getoName() {
		return oName;
	}

	public void setoName(String oName) {
		this.oName = oName;
	}

	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getConsignOrderCode() {
		return consignOrderCode;
	}

	public void setConsignOrderCode(String consignOrderCode) {
		this.consignOrderCode = consignOrderCode;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getApplyerId() {
		return applyerId;
	}

	public void setApplyerId(Long applyerId) {
		this.applyerId = applyerId;
	}

	public String getApplyerName() {
		return applyerName;
	}

	public void setApplyerName(String applyerName) {
		this.applyerName = applyerName;
	}

	public Date getSerialTime() {
		return serialTime;
	}

	public void setSerialTime(Date serialTime) {
		this.serialTime = serialTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getBalanceSecondSettlement() {
		return balanceSecondSettlement;
	}

	public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
		this.balanceSecondSettlement = balanceSecondSettlement;
	}

	public String getAssociationType() {
		return associationType;
	}

	public void setAssociationType(String associationType) {
		this.associationType = associationType;
	}
}
