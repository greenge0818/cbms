package com.prcsteel.platform.account.model.dto;

import java.math.BigDecimal;

/**
 * 新增付款 前端请求的实体参数
 * Created by chengui on 2016/3/7.
 */
public class PaymentCreateDto {
	
	private Long traderId; //交易员Id
	
	private String trader; //交易员
	
	private String paymentType; //交易类型
	
	private String name; //卖家名称
	
	private Long departmentId;//部门id
	
	private String departmentName;//部门名称

    private String bankName; //开户银行

    private String bankAccountCode; //银行账号
    
    private BigDecimal payAmount; //申请付款金额
    
    private String paymentDrafts; //银票支付

    private BigDecimal balanceSecondSettlement; //二次结算账户余额
    
    private String status; //打款资料状态
    
    private String createdBy; //操作人
    
    private String code; //付款申请单号
    
    private Long orgId;//部门ID
    
    private String orgName;//部门名称
    
    private Long bankId;//银行id
    
	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getTraderId() {
		return traderId;
	}

	public void setTraderId(Long traderId) {
		this.traderId = traderId;
	}

	public String getTrader() {
		return trader;
	}

	public void setTrader(String trader) {
		this.trader = trader;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccountCode() {
		return bankAccountCode;
	}

	public void setBankAccountCode(String bankAccountCode) {
		this.bankAccountCode = bankAccountCode;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getPaymentDrafts() {
		return paymentDrafts;
	}

	public void setPaymentDrafts(String paymentDrafts) {
		this.paymentDrafts = paymentDrafts;
	}

	public BigDecimal getBalanceSecondSettlement() {
		return balanceSecondSettlement;
	}

	public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
		this.balanceSecondSettlement = balanceSecondSettlement;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

    
}
