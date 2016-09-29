package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.prcsteel.platform.order.model.model.AllowanceOrderDetailItem;

public class RebateDetailDto extends  AllowanceOrderDetailItem{
	private Long orderId;
	
	private String orderCode;
	
	private Long allowanceId;
	
	private Date orderTime;//开单时间
	
	private BigDecimal discountedAmount;//折后金额
	
	private String accountName;
	
	private BigDecimal discountedWeight;//折后重量
	
	private String allowanceManner;//折让方式
	
	private Long allowanceItemId;//折让订单关联表id
	
  	private Long allowanceDetilId;//订单详情id
  	
  	private Long sellerAllowanceId;//卖家折让单id

	private Date orderCreated;
	private Long sellerId;
	private String sellerName;
	private String contractCode;
	private Integer totalQuantity;
	private BigDecimal totalWeight;
	private BigDecimal totalAmount;
	private BigDecimal totalActualWeight;
	private BigDecimal totalActualAmount;
  	
	public Long getSellerAllowanceId() {
		return sellerAllowanceId;
	}

	public void setSellerAllowanceId(Long sellerAllowanceId) {
		this.sellerAllowanceId = sellerAllowanceId;
	}

	public Long getAllowanceItemId() {
		return allowanceItemId;
	}

	public void setAllowanceItemId(Long allowanceItemId) {
		this.allowanceItemId = allowanceItemId;
	}

	public Long getAllowanceDetilId() {
		return allowanceDetilId;
	}

	public void setAllowanceDetilId(Long allowanceDetilId) {
		this.allowanceDetilId = allowanceDetilId;
	}

	public String getAllowanceManner() {
		return allowanceManner;
	}

	public void setAllowanceManner(String allowanceManner) {
		this.allowanceManner = allowanceManner;
	}

	public BigDecimal getDiscountedWeight() {
		return discountedWeight;
	}

	public void setDiscountedWeight(BigDecimal discountedWeight) {
		this.discountedWeight = discountedWeight;
	}

	public BigDecimal getTotalActualAmount() {
		return totalActualAmount;
	}

	public void setTotalActualAmount(BigDecimal totalActualAmount) {
		this.totalActualAmount = totalActualAmount;
	}

	public BigDecimal getTotalActualWeight() {
		return totalActualWeight;
	}

	public void setTotalActualWeight(BigDecimal totalActualWeight) {
		this.totalActualWeight = totalActualWeight;
	}

	public Date getOrderCreated() {
		return orderCreated;
	}

	public void setOrderCreated(Date orderCreated) {
		this.orderCreated = orderCreated;
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

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
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

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public BigDecimal getDiscountedAmount() {
		return discountedAmount;
	}

	public void setDiscountedAmount(BigDecimal discountedAmount) {
		this.discountedAmount = discountedAmount;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

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

	public Long getAllowanceId() {
		return allowanceId;
	}

	public void setAllowanceId(Long allowanceId) {
		this.allowanceId = allowanceId;
	}

	public BigDecimal getAbsAllowanceWeight(){
		return new BigDecimal(Math.abs(this.getAllowanceWeight().doubleValue()));
	}

	public BigDecimal getAbsAllowanceAmount(){
		return new BigDecimal(Math.abs(this.getAllowanceAmount().doubleValue()));
	}
	
}
