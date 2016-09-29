package com.prcsteel.platform.smartmatch.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class QuotationOrderItemsDto {
	private Integer id;
	private Integer quotationOrderId;
	private Date quotationOrderUpdateTime;
	private String sellerName;
	private String categoryName;
	private String materialName;
	private String spec;
	private String factoryName;
	private String warehouseName;
	private int quantity;
	private BigDecimal weight;
	private BigDecimal dealPrice;
	private BigDecimal costPrice;
	private String ownerName;
	private BigDecimal totalAmount;
	private String remark;
	private String remarkDesc;//报价单汇总备注
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuotationOrderId() {
		return quotationOrderId;
	}
	public void setQuotationOrderId(Integer quotationOrderId) {
		this.quotationOrderId = quotationOrderId;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Date getQuotationOrderUpdateTime() {
		return quotationOrderUpdateTime;
	}
	public void setQuotationOrderUpdateTime(Date quotationOrderUpdateTime) {
		this.quotationOrderUpdateTime = quotationOrderUpdateTime;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public BigDecimal getDealPrice() {
		return dealPrice;
	}
	public void setDealPrice(BigDecimal dealPrice) {
		this.dealPrice = dealPrice;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmoun(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemarkDesc() {
		return remarkDesc;
	}
	public void setRemarkDesc(String remarkDesc) {
		this.remarkDesc = remarkDesc;
	}
	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
}
