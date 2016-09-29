package com.prcsteel.platform.order.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.prcsteel.platform.common.constants.Constant;

public class InvoiceInDetailFormDto implements Serializable {

	private static final long serialVersionUID = 5440596298053544678L;

	private Long invoiceDetailId;

    private String nsortName;

    private String material;
    
    private Long aliasId;  		
    private Long categoryId; 	
    private String categoryName; 

    private String spec;

    private double weight;

    private double noTaxAmount;

    private double taxAmount;

    private String nsortNameComb;

    private String typeOfSpec;

    private List<InvoiceInDetailOrderitemFormDto> orderItems;
    
    public double getAmount(){
    	return new BigDecimal(noTaxAmount + taxAmount).setScale(Constant.MONEY_PRECISION, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
	public Long getInvoiceDetailId() {
		return invoiceDetailId;
	}

	public void setInvoiceDetailId(Long invoiceDetailId) {
		this.invoiceDetailId = invoiceDetailId;
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

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		
		this.weight = weight;
	}

	public double getNoTaxAmount() {
		return noTaxAmount;
	}

	public void setNoTaxAmount(double noTaxAmount) {
		this.noTaxAmount = noTaxAmount;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getNsortNameComb() {
		return nsortNameComb;
	}

	public void setNsortNameComb(String nsortNameComb) {
		this.nsortNameComb = nsortNameComb;
	}

	public String getTypeOfSpec() {
		return typeOfSpec;
	}

	public void setTypeOfSpec(String typeOfSpec) {
		this.typeOfSpec = typeOfSpec;
	}

	public List<InvoiceInDetailOrderitemFormDto> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<InvoiceInDetailOrderitemFormDto> orderItems) {
		this.orderItems = orderItems;
	}

	public Long getAliasId() {
		return aliasId;
	}

	public void setAliasId(Long aliasId) {
		this.aliasId = aliasId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}