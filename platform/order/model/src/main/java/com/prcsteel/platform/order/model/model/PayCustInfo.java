package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;

/**
 * 新增付款 卖家展示信息
 * Created by chengui on 2016/3/7.
 */
public class PayCustInfo {

    private String name; //卖家名称

    private String bankName; //开户银行

    private String bankAccountCode; //银行账号

    private BigDecimal balanceSecondSettlement; //二次结算账户余额
    
    private String bankDataStatus; //打款资料状态
    
    private Long bankId;//银行id
    
    private Long isDefault;//是否默认银行
 
    private String bankStatus;//银行资料审核状态
    
    private Long isDeleted;//是否删除
    
	public Long getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Long isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getBankStatus() {
		return bankStatus;
	}

	public void setBankStatus(String bankStatus) {
		this.bankStatus = bankStatus;
	}

	public Long getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Long isDefault) {
		this.isDefault = isDefault;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
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

	public BigDecimal getBalanceSecondSettlement() {
		return balanceSecondSettlement;
	}

	public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
		this.balanceSecondSettlement = balanceSecondSettlement;
	}

	public String getBankDataStatus() {
		return bankDataStatus;
	}

	public void setBankDataStatus(String bankDataStatus) {
		this.bankDataStatus = bankDataStatus;
	}
	
}