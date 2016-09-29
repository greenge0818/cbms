package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.BusiConsignOrderCredential;

public class ConsignOrderCredentialDto extends BusiConsignOrderCredential{
	private String code;
	private String buyerName;
	private String sellerName;
	private Long buyerId;
	private Long orderId;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	

	private String submitDateStr;
	private int durationDay;
	private String batchBuyerCredentialCode;
	private String batchSellerCredentialCode;
	private Long sellerId;
	private String settingValue;//1 需要审核， 0 不需要审核
	
	public String getSettingValue() {
		return settingValue;
	}
	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}
	public String getBuyerName() {
		return buyerName;
	}
	
	public ConsignOrderCredentialDto setBuyerName(String buyerName) {
		this.buyerName = buyerName;
		return this;
	}
	public String getSellerName() {
		return sellerName;
	}
	public ConsignOrderCredentialDto setSellerName(String sellerName) {
		this.sellerName = sellerName;
		return this;
	}
	public Long getOrderId() {
		return orderId;
	}
	public ConsignOrderCredentialDto setOrderId(Long orderId) {
		this.orderId = orderId;
		return this;
	}
	public String getSubmitDateStr() {
		return submitDateStr;
	}
	public void setSubmitDateStr(String submitDateStr) {
		this.submitDateStr = submitDateStr;
	}
	public int getDurationDay() {
		return durationDay;
	}
	public void setDurationDay(int durationDay) {
		this.durationDay = durationDay;
	}
	public String getBatchBuyerCredentialCode() {
		return batchBuyerCredentialCode;
	}
	public void setBatchBuyerCredentialCode(String batchBuyerCredentialCode) {
		this.batchBuyerCredentialCode = batchBuyerCredentialCode;
	}
	public String getBatchSellerCredentialCode() {
		return batchSellerCredentialCode;
	}
	public void setBatchSellerCredentialCode(String batchSellerCredentialCode) {
		this.batchSellerCredentialCode = batchSellerCredentialCode;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	
}
