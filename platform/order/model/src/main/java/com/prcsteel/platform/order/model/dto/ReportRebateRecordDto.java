package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

public class ReportRebateRecordDto {
    private Long id;

    private String rebateTime;

    private Long accountId;

    private String accountName;

    private Long contactId;

    private String contactName;

    private BigDecimal weight;

    private BigDecimal amount;

    private BigDecimal rebateAmount;
    
    private BigDecimal rebateBalance;

    private String code;
    
    private String type;
    
    private BigDecimal moneyReduce;

	public BigDecimal getRebateBalance() {
		return rebateBalance;
	}

	public void setRebateBalance(BigDecimal rebateBalance) {
		this.rebateBalance = rebateBalance;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the rebateTime
	 */
	public String getRebateTime() {
		return rebateTime;
	}

	/**
	 * @param rebateTime the rebateTime to set
	 */
	public void setRebateTime(String rebateTime) {
		this.rebateTime = rebateTime;
	}

	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the contactId
	 */
	public Long getContactId() {
		return contactId;
	}

	/**
	 * @param contactId the contactId to set
	 */
	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * @param contactName the contactName to set
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	/**
	 * @return the weight
	 */
	public BigDecimal getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the rebateAmount
	 */
	public BigDecimal getRebateAmount() {
		return rebateAmount;
	}

	/**
	 * @param rebateAmount the rebateAmount to set
	 */
	public void setRebateAmount(BigDecimal rebateAmount) {
		this.rebateAmount = rebateAmount;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getMoneyReduce() {
		return moneyReduce;
	}

	public void setMoneyReduce(BigDecimal moneyReduce) {
		this.moneyReduce = moneyReduce;
	}
   
}