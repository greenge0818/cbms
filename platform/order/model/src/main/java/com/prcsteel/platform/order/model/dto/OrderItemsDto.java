package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class OrderItemsDto {
	private Date creationTime;
	
	private String code;
	    
	private String accountName;
	    
	private BigDecimal bringamount;
	
	private BigDecimal notOpenWeight;
	
	private BigDecimal notOpenAmount;
	
	private String nsortName;

    private String material;

    private String spec;

    private BigDecimal dealPrice;
    
    private BigDecimal usedAmount;

    private BigDecimal usedWeight;

    private BigDecimal actualPickWeightServer;
    
    private BigDecimal invoiceAmount;
    
    private BigDecimal invoiceWeight;
    
    private BigDecimal allowanceBuyerAmount;
    
    private BigDecimal allowanceWeight;
    
    private Long orderId;
    
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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getAllowanceWeight() {
		return allowanceWeight;
	}

	public void setAllowanceWeight(BigDecimal allowanceWeight) {
		this.allowanceWeight = allowanceWeight;
	}

	public BigDecimal getAllowanceBuyerAmount() {
		return allowanceBuyerAmount;
	}

	public void setAllowanceBuyerAmount(BigDecimal allowanceBuyerAmount) {
		this.allowanceBuyerAmount = allowanceBuyerAmount;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public BigDecimal getInvoiceWeight() {
		return invoiceWeight;
	}

	public void setInvoiceWeight(BigDecimal invoiceWeight) {
		this.invoiceWeight = invoiceWeight;
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

	public BigDecimal getDealPrice() {
		return dealPrice;
	}

	public void setDealPrice(BigDecimal dealPrice) {
		this.dealPrice = dealPrice;
	}

	public BigDecimal getUsedAmount() {
		return usedAmount;
	}

	public void setUsedAmount(BigDecimal usedAmount) {
		this.usedAmount = usedAmount;
	}

	public BigDecimal getUsedWeight() {
		return usedWeight;
	}

	public void setUsedWeight(BigDecimal usedWeight) {
		this.usedWeight = usedWeight;
	}

	public BigDecimal getActualPickWeightServer() {
		return actualPickWeightServer;
	}

	public void setActualPickWeightServer(BigDecimal actualPickWeightServer) {
		this.actualPickWeightServer = actualPickWeightServer;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public BigDecimal getBringamount() {
		return bringamount;
	}

	public void setBringamount(BigDecimal bringamount) {
		this.bringamount = bringamount;
	}

	public BigDecimal getNotOpenWeight() {
		return notOpenWeight;
	}

	public void setNotOpenWeight(BigDecimal notOpenWeight) {
		this.notOpenWeight = notOpenWeight;
	}

	public BigDecimal getNotOpenAmount() {
		return notOpenAmount;
	}

	public void setNotOpenAmount(BigDecimal notOpenAmount) {
		this.notOpenAmount = notOpenAmount;
	}
	
}
