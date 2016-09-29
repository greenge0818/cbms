package com.prcsteel.platform.account.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class AccountTransLogsDto {
	
	private String applyType;//申请类型
	
	private String consignOrderCode;//订单关联号
	
	private Date serialTime;//流水时间
	
	private BigDecimal amount;//二次结算发生额

	private BigDecimal currentBalance;//二次结算当前余额
	
	private BigDecimal cashHappenBalance;//账户发生额

	private BigDecimal cashCurrentBalance;//账户余额
	
	private BigDecimal amountAdd;//二次结算账户增加
	
	private BigDecimal amountReduce;//二次结算账户减少
	
	private BigDecimal balanceAdd;//账户余额增加
	
	private BigDecimal balanceReduce;//账户余额减少

	private String associationType;//关联类型
	
	private String applyerName;//业务员

	
	public String getConsignOrderCode() {
		return consignOrderCode;
	}

	public void setConsignOrderCode(String consignOrderCode) {
		this.consignOrderCode = consignOrderCode;
	}

	public String getApplyerName() {
		return applyerName;
	}

	public void setApplyerName(String applyerName) {
		this.applyerName = applyerName;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public Date getSerialTime() {
		return serialTime;
	}

	public void setSerialTime(Date serialTime) {
		this.serialTime = serialTime;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public BigDecimal getCashHappenBalance() {
		return cashHappenBalance;
	}

	public void setCashHappenBalance(BigDecimal cashHappenBalance) {
		this.cashHappenBalance = cashHappenBalance;
	}

	public BigDecimal getCashCurrentBalance() {
		return cashCurrentBalance;
	}

	public void setCashCurrentBalance(BigDecimal cashCurrentBalance) {
		this.cashCurrentBalance = cashCurrentBalance;
	}

	public BigDecimal getAmountAdd() {
		return amountAdd;
	}

	public void setAmountAdd(BigDecimal amountAdd) {
		this.amountAdd = amountAdd;
	}

	public BigDecimal getAmountReduce() {
		return amountReduce;
	}

	public void setAmountReduce(BigDecimal amountReduce) {
		this.amountReduce = amountReduce;
	}

	public BigDecimal getBalanceAdd() {
		return balanceAdd;
	}

	public void setBalanceAdd(BigDecimal balanceAdd) {
		this.balanceAdd = balanceAdd;
	}

	public BigDecimal getBalanceReduce() {
		return balanceReduce;
	}

	public void setBalanceReduce(BigDecimal balanceReduce) {
		this.balanceReduce = balanceReduce;
	}

	public String getAssociationType() {
		return associationType;
	}

	public void setAssociationType(String associationType) {
		this.associationType = associationType;
	}
	
}
