package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;

/**
 * 订单详情 
 * @author tuxianming
 */
public class OrderItemDetailDto extends ConsignOrder{
	
	List<ConsignOrderItems> items = new ArrayList<ConsignOrderItems>();
	
	private String createdStr;
	
	private BigDecimal totalAmount;					//总金额
	private BigDecimal totalWeight;					//总重量(吨)
	private BigDecimal actualPickTotalAmount;		//实提总金额
	private BigDecimal actualPickTotalWeight;		//实提总重量
	private Long sellerId;
	private String sellerName;
	
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public List<ConsignOrderItems> getItems() {
		return items;
	}
	public void setItems(List<ConsignOrderItems> items) {
		this.items = items;
	}
	
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	public BigDecimal getActualPickTotalAmount() {
		return actualPickTotalAmount;
	}
	public void setActualPickTotalAmount(BigDecimal actualPickTotalAmount) {
		this.actualPickTotalAmount = actualPickTotalAmount;
	}
	public BigDecimal getActualPickTotalWeight() {
		return actualPickTotalWeight;
	}
	public void setActualPickTotalWeight(BigDecimal actualPickTotalWeight) {
		this.actualPickTotalWeight = actualPickTotalWeight;
	}
	public String getCreatedStr() {
		return createdStr;
	}
	public void setCreatedStr(String createdStr) {
		this.createdStr = createdStr;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	
}
