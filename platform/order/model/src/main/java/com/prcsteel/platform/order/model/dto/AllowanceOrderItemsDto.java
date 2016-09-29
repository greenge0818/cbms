package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class AllowanceOrderItemsDto {
	
	private Long id;

	private Long detailItemId;
	
	private Long orderId;
	
	private Date orderTime;
	
	private Long orderDetailId;
	
	private String orderCode;
	
	private String contractCode;
	
	private Long buyerId;

	private String buyerName;
	
	private Long sellerId;

	private String sellerName;
	
	private int totalQuantity;
	
	private BigDecimal totalWeight = BigDecimal.ZERO;
	
	private BigDecimal totalAmount = BigDecimal.ZERO;
	
	private BigDecimal totalActualWeight = BigDecimal.ZERO;
	
	private BigDecimal totalActualAmount = BigDecimal.ZERO;
	
	private String nsortName; 
	
	private String material;
	
	private String spec;
	
	private BigDecimal actualWeight = BigDecimal.ZERO;
	
	private BigDecimal actualAmount = BigDecimal.ZERO;
	
	private BigDecimal allowanceWeight = BigDecimal.ZERO;
	
	private BigDecimal allowanceAmount = BigDecimal.ZERO;
	
	private BigDecimal unAllowanceWeight = BigDecimal.ZERO;
	
	private BigDecimal unAllowanceAmount = BigDecimal.ZERO;

	private String allowanceCode;
	
	private String note;

	private Long buyerDepartmentId;

	private String buyerDepartmentName;

	private Long sellerDepartmentId;

	private String sellerDepartmentName;

	private Boolean isShowBuyerDept;

	private Boolean isShowSellerDept;

	private Boolean isShowDept;		// reb_allowance表

	private Long accountId;			// reb_allowance表字段

	private String accountName; 	// reb_allowance表字段

	private Long departmentId; 	 	// reb_allowance表字段

	private String departmentName;	// reb_allowance表字段

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDetailItemId() {
		return detailItemId;
	}

	public void setDetailItemId(Long detailItemId) {
		this.detailItemId = detailItemId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
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

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
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

	public BigDecimal getTotalActualWeight() {
		return totalActualWeight;
	}

	public void setTotalActualWeight(BigDecimal totalActualWeight) {
		this.totalActualWeight = totalActualWeight;
	}

	public BigDecimal getTotalActualAmount() {
		return totalActualAmount;
	}

	public void setTotalActualAmount(BigDecimal totalActualAmount) {
		this.totalActualAmount = totalActualAmount;
	}

	public String getNsortName() {
		return nsortName;
	}

	public void setNsortName(String nsortName) {
		this.nsortName = nsortName;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
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

	public BigDecimal getAllowanceWeight() {
		return allowanceWeight;
	}

	public void setAllowanceWeight(BigDecimal allowanceWeight) {
		this.allowanceWeight = allowanceWeight;
	}

	public BigDecimal getAllowanceAmount() {
		return allowanceAmount;
	}

	public void setAllowanceAmount(BigDecimal allowanceAmount) {
		this.allowanceAmount = allowanceAmount;
	}

	public BigDecimal getUnAllowanceWeight() {
		return unAllowanceWeight;
	}

	public void setUnAllowanceWeight(BigDecimal unAllowanceWeight) {
		this.unAllowanceWeight = unAllowanceWeight;
	}

	public BigDecimal getUnAllowanceAmount() {
		return unAllowanceAmount;
	}

	public void setUnAllowanceAmount(BigDecimal unAllowanceAmount) {
		this.unAllowanceAmount = unAllowanceAmount;
	}


	public BigDecimal getAbsAllowanceWeight(){
		return new BigDecimal(Math.abs(this.getAllowanceWeight().doubleValue()));
	}

	public BigDecimal getAbsAllowanceAmount(){
		return new BigDecimal(Math.abs(this.getAllowanceAmount().doubleValue()));
	}

	public String getAllowanceCode() {
		return allowanceCode;
	}

	public void setAllowanceCode(String allowanceCode) {
		this.allowanceCode = allowanceCode;
	}

	public Long getBuyerDepartmentId() {
		return buyerDepartmentId;
	}

	public void setBuyerDepartmentId(Long buyerDepartmentId) {
		this.buyerDepartmentId = buyerDepartmentId;
	}

	public String getBuyerDepartmentName() {
		return buyerDepartmentName;
	}

	public void setBuyerDepartmentName(String buyerDepartmentName) {
		this.buyerDepartmentName = buyerDepartmentName;
	}

	public Long getSellerDepartmentId() {
		return sellerDepartmentId;
	}

	public void setSellerDepartmentId(Long sellerDepartmentId) {
		this.sellerDepartmentId = sellerDepartmentId;
	}

	public String getSellerDepartmentName() {
		return sellerDepartmentName;
	}

	public void setSellerDepartmentName(String sellerDepartmentName) {
		this.sellerDepartmentName = sellerDepartmentName;
	}

	public Boolean getIsShowBuyerDept() {
		return isShowBuyerDept;
	}

	public void setIsShowBuyerDept(Boolean isShowBuyerDept) {
		this.isShowBuyerDept = isShowBuyerDept;
	}

	public Boolean getIsShowSellerDept() {
		return isShowSellerDept;
	}

	public void setIsShowSellerDept(Boolean isShowSellerDept) {
		this.isShowSellerDept = isShowSellerDept;
	}

	public Boolean getIsShowDept() {
		return isShowDept;
	}

	public void setIsShowDept(Boolean isShowDept) {
		this.isShowDept = isShowDept;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}
