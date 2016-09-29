package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.prcsteel.platform.order.model.model.ConsignOrderItems;

/**
 * 打印卖家凭证的dto
 * @author tuxianming
 */
public class OrderTradeCertificateDto{
	private Long orderId;			//订单id
	private String code;			//订单code
	private String buyerName;		//买家名字
	private Long sellerId;
	private String sellerName;		//卖家名字
	private BigDecimal totalWeight;	//总重量
	private BigDecimal totalAmount;	//总金额
	
	private String openOrderDate;	//开单日期
	
	private String ownerName;		//交易员
	private int quantity;			//总数量
	private int actualPickTotalQuantity; 		//实提总数量
	private BigDecimal actualPickTotalWeight;	//实提总重量
	private BigDecimal actualPickTotalAmount;	//实提总金额

	private String printBuyerTradeCertificateStatus; //打印卖家凭证的打印状态 
	private int durationDay; 	//距离开单的时间
	private String payStatus;   // 订单状态
	private Date secondaryTime;//二结时间
	private Boolean settingValue;   // 0 不须审核通过也能开票,1 必须审核通过才能开票

	private String orderOrgName; //订单所属服务中心名称
	
	public Boolean getSettingValue() {
		return settingValue;
	}
	public void setSettingValue(Boolean settingValue) {
		this.settingValue = settingValue;
	}
	private List<ConsignOrderItems> items = new ArrayList<ConsignOrderItems>();
	
	
	
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
	public int getDurationDay() {
		return durationDay;
	}
	public OrderTradeCertificateDto setDurationDay(int durationDay) {
		this.durationDay = durationDay;
		return this;
	}
	public String getPrintBuyerTradeCertificateStatus() {
		return printBuyerTradeCertificateStatus;
	}
	public OrderTradeCertificateDto setPrintBuyerTradeCertificateStatus(String printBuyerTradeCertificateStatus) {
		this.printBuyerTradeCertificateStatus = printBuyerTradeCertificateStatus;
		return this;
	}
	public String getCode() {
		return code;
	}
	public OrderTradeCertificateDto setCode(String code) {
		this.code = code;
		return this;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public OrderTradeCertificateDto setBuyerName(String buyerName) {
		this.buyerName = buyerName;
		return this;
	}
	public BigDecimal getTotalWeight() {
		return totalWeight;
	}
	public OrderTradeCertificateDto setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
		return this;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public OrderTradeCertificateDto setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
		return this;
	}
	public String getOpenOrderDate() {
		return openOrderDate;
	}
	public OrderTradeCertificateDto setOpenOrderDate(String openOrderDate) {
		this.openOrderDate = openOrderDate;
		return this;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public OrderTradeCertificateDto setOwnerName(String ownerName) {
		this.ownerName = ownerName;
		return this;
	}
	public int getQuantity() {
		return quantity;
	}
	public OrderTradeCertificateDto setQuantity(int quantity) {
		this.quantity = quantity;
		return this;
	}
	public BigDecimal getActualPickTotalWeight() {
		return actualPickTotalWeight;
	}
	public OrderTradeCertificateDto setActualPickTotalWeight(BigDecimal actualPickTotalWeight) {
		this.actualPickTotalWeight = actualPickTotalWeight;
		return this;
	}
	public BigDecimal getActualPickTotalAmount() {
		return actualPickTotalAmount;
	}
	public OrderTradeCertificateDto setActualPickTotalAmount(BigDecimal actualPickTotalAmount) {
		this.actualPickTotalAmount = actualPickTotalAmount;
		return this;
	}
	public String getSellerName() {
		return sellerName;
	}
	public OrderTradeCertificateDto setSellerName(String sellerName) {
		this.sellerName = sellerName;
		return this;
	}
	public Long getOrderId() {
		return orderId;
	}
	public OrderTradeCertificateDto setOrderId(Long orderId) {
		this.orderId = orderId;
		return this;
	}
	public int getActualPickTotalQuantity() {
		return actualPickTotalQuantity;
	}
	public OrderTradeCertificateDto setActualPickTotalQuantity(int actualPickTotalQuantity) {
		this.actualPickTotalQuantity = actualPickTotalQuantity;
		return this;
	}
	public List<ConsignOrderItems> getItems() {
		return items;
	}
	public void setItems(List<ConsignOrderItems> items) {
		this.items = items;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getOrderOrgName() {
		return orderOrgName;
	}

	public void setOrderOrgName(String orderOrgName) {
		this.orderOrgName = orderOrgName;
	}
}
