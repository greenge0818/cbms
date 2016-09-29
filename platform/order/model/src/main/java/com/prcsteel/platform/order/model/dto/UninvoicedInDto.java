package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

import com.prcsteel.platform.common.constants.InvoiceConstant;

/**
 * Created by Rabbit Mao on 2015/9/11.
 */
public class UninvoicedInDto {
    private Long id;
    private String sellerName;
    private String taxCode;
    private String nsortName;
    private String material;
    private String spec;
    private BigDecimal weight;
    private BigDecimal costPrice;
    private BigDecimal noTaxAmount;
    private BigDecimal taxAmount;
    private BigDecimal amount;
    private String orderCode;
    private String status;
    private String orgName;
    
    private String departmentName;//增加部门名称 add lixiang
    
    private Long departmentCount;//部门个数

    public Long getDepartmentCount() {
		return departmentCount;
	}

	public void setDepartmentCount(Long departmentCount) {
		this.departmentCount = departmentCount;
	}
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
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

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getNoTaxAmount() {
        return noTaxAmount;
    }

    public void setAmount(BigDecimal amount) {
        this.noTaxAmount = amount.divide(InvoiceConstant.TAXRATE.add(BigDecimal.ONE), 2, BigDecimal.ROUND_HALF_UP);
        this.taxAmount = amount.subtract(this.noTaxAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
