
    /**  
    * @Title: OrderInfoForCertificateDto.java
    * @Package com.prcsteel.platform.order.model.dto
    * @Description: TODO(用一句话描述该文件做什么)
    * @author Green.Ge
    * @date 2016年4月11日
    * @version V1.0  
    */
    
package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
    * @ClassName: OrderInfoForCertificateDto
    * @Description: 凭证明细中的订单信息
    * @author Green.Ge
    * @date 2016年4月11日
    *
    */

public class OrderInfoForCertificateDto {
	Long orderId;		//订单id
	String orderCode;	//订单编号
	String orderCreated;//订单创建时间	
	Long buyerId;		//买家ID
	String buyerName;	//买家名称
	Long sellerId;		//卖家ID
	String sellerName;	//卖家名称
	Long ownerId;		//交易员ID
	String ownerName;	//交易员姓名
	Integer totalQuantity;//总数量
	BigDecimal totalWeight;//总重量
	BigDecimal totalAmount;//总金额
	Integer actualPickTotalQuantity;//实提数量
	BigDecimal actualPickTotalWeight;//实提重量
	BigDecimal actualPickTotalAmount;//实提金额
	private String payStatus;   // 订单状态
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public Date getSecondaryTime() {
		return secondaryTime;
	}
	public void setSecondaryTime(Date secondaryTime) {
		this.secondaryTime = secondaryTime;
	}
	private Date secondaryTime;//二结时间
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getOrderCreated() {
		return orderCreated;
	}
	public void setOrderCreated(String orderCreated) {
		this.orderCreated = orderCreated;
	}
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
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
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public Integer getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public BigDecimal getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Integer getActualPickTotalQuantity() {
		return actualPickTotalQuantity;
	}
	public void setActualPickTotalQuantity(Integer actualPickTotalQuantity) {
		this.actualPickTotalQuantity = actualPickTotalQuantity;
	}
	public BigDecimal getActualPickTotalWeight() {
		return actualPickTotalWeight;
	}
	public void setActualPickTotalWeight(BigDecimal actualPickTotalWeight) {
		this.actualPickTotalWeight = actualPickTotalWeight;
	}
	public BigDecimal getActualPickTotalAmount() {
		return actualPickTotalAmount;
	}
	public void setActualPickTotalAmount(BigDecimal actualPickTotalAmount) {
		this.actualPickTotalAmount = actualPickTotalAmount;
	}
}
