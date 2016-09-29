package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.prcsteel.platform.order.model.enums.InvoiceInBordereauxStatus;

/**
 * Created by dq on 14/12/2015.
 */
public class InvoiceInBordereauxDto {

	private Long orderitemId;
	
	private Long orderId;
	
	private Date orderTime;
	
	private String orderCode;
	
	private String sellerId;
	
	private String sellerName;
	
	private String nsortName;
	
	private String material;
	
	private String spec;
	
	private BigDecimal actualWeight = BigDecimal.ZERO;
	
	private BigDecimal actualAmount = BigDecimal.ZERO;
	
	private BigDecimal allowanceAmount = null;
	
	private BigDecimal allowanceWeight = null;
	
	private BigDecimal amount = null;
	
	private BigDecimal weight = null;
	
	private BigDecimal unAmount = null;
	
	private BigDecimal unWeight = null;
	
	private BigDecimal totalNoWeight = BigDecimal.ZERO;
	
	private BigDecimal totalNoAmount = BigDecimal.ZERO;
	
	private String status = InvoiceInBordereauxStatus.No.getValue();
	
	private String departmentName;//增加部门名称 add lixiang
	    
	private Long departmentCount;//部门个数
	
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getDepartmentCount() {
		return departmentCount;
	}

	public void setDepartmentCount(Long departmentCount) {
		this.departmentCount = departmentCount;
	}

	public BigDecimal getTotalNoWeight() {
		return totalNoWeight;
	}

	public void setTotalNoWeight(BigDecimal totalNoWeight) {
		this.totalNoWeight = totalNoWeight;
	}

	public BigDecimal getTotalNoAmount() {
		return totalNoAmount;
	}

	public void setTotalNoAmount(BigDecimal totalNoAmount) {
		this.totalNoAmount = totalNoAmount;
	}

	public Long getOrderitemId() {
		return orderitemId;
	}

	public void setOrderitemId(Long orderitemId) {
		this.orderitemId = orderitemId;
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

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
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

	public BigDecimal getAllowanceAmount() {
		return allowanceAmount;
	}

	public void setAllowanceAmount(BigDecimal allowanceAmount) {
		this.allowanceAmount = allowanceAmount;
	}

	public BigDecimal getAllowanceWeight() {
		return allowanceWeight;
	}

	public void setAllowanceWeight(BigDecimal allowanceWeight) {
		this.allowanceWeight = allowanceWeight;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getUnAmount() {
		return unAmount;
	}

	public void setUnAmount(BigDecimal unAmount) {
		this.unAmount = unAmount;
	}

	public BigDecimal getUnWeight() {
		return unWeight;
	}

	public void setUnWeight(BigDecimal unWeight) {
		this.unWeight = unWeight;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}
