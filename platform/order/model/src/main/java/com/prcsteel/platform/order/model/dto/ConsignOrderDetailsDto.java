package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author zhoukun
 */
public class ConsignOrderDetailsDto {

	private Date orderDateTime;
	
	private String orderNumber;
	
	private String buyerFullName;
	
	private String buyerTrader;
	
	private Integer orderStatus;
	
	private String sellerFullName;
	
	private Long sellerId;
	
	private String nsortName;
	
	private String norms;
	
	private String materials;
	
	private String factory;
	
	private BigDecimal dealPrice;
	
	private BigDecimal weight;
	
	private BigDecimal amount;
	
	private BigDecimal actualWeight;
	
	private BigDecimal actualAmount;

	public Date getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(Date orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getBuyerFullName() {
		return buyerFullName;
	}

	public void setBuyerFullName(String buyerFullName) {
		this.buyerFullName = buyerFullName;
	}

	public String getBuyerTrader() {
		return buyerTrader;
	}

	public void setBuyerTrader(String buyerTrader) {
		this.buyerTrader = buyerTrader;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getSellerFullName() {
		return sellerFullName;
	}

	public void setSellerFullName(String sellerFullName) {
		this.sellerFullName = sellerFullName;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getNsortName() {
		return nsortName;
	}

	public void setNsortName(String nsortName) {
		this.nsortName = nsortName;
	}

	public String getNorms() {
		return norms;
	}

	public void setNorms(String norms) {
		this.norms = norms;
	}

	public String getMaterials() {
		return materials;
	}

	public void setMaterials(String materials) {
		this.materials = materials;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public BigDecimal getDealPrice() {
		return dealPrice;
	}

	public void setDealPrice(BigDecimal dealPrice) {
		this.dealPrice = dealPrice;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getActualWeight() {
		return actualWeight;
	}

	public void setActualWeight(BigDecimal actualWeight) {
		this.actualWeight = actualWeight;
	}

	public BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}
	
}
