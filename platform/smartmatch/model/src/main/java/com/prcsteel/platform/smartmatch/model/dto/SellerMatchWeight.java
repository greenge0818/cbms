package com.prcsteel.platform.smartmatch.model.dto;

public class SellerMatchWeight {
	Long sellerId;//卖家ID
	String sellerName;//卖家名称
	Double weight;//权重
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public SellerMatchWeight(Long sellerId, String sellerName, Double weight) {
		super();
		this.sellerId = sellerId;
		this.sellerName = sellerName;
		this.weight = weight;
	}
	public SellerMatchWeight() {
		super();
	}
	
}
