package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

import com.prcsteel.platform.common.constants.InvoiceConstant;

/**
 * Created by Rabbit Mao on 2015/8/19.
 */
public class UnInvoicedDto {
    /* <resultMap id="UnInvoicedDto" type="com.prcsteel.cbms.persist.dto.UnInvoicedDto">
        <result column="item_id" property="itemId" jdbcType="BIGINT"/>
        <result column="buyer_name" property="buyerName" jdbcType="VARCHAR"/>
        <result column="tax_code" property="taxCode" jdbcType="VARCHAR"/>
        <result column="nsort_name" property="nsortName" jdbcType="VARCHAR"/>
        <result column="spec" property="spec" jdbcType="VARCHAR"/>
        <result column="material" property="material" jdbcType="VARCHAR"/>
        <result column="weight" property="weight" jdbcType="DECIMAL"/>
        <result column="price" property="price" jdbcType="DECIMAL"/>
        <result column="no_tax_amount" property="noTaxAmount" jdbcType="DECIMAL"/>
        <result column="tax_amount" property="taxAmount" jdbcType="DECIMAL"/>
        <result column="total_amount" property="totalAmount" jdbcType="DECIMAL"/>
        <result column="order_code" property="orderCode" jdbcType="VARCHAR"/>
        <result column="invoice_status" property="invoiceStatus" jdbcType="INTEGER"/>
    </resultMap>*/
    private Long itemId;
    private String buyerName;
    private String taxCode;
    private String nsortName;
    private String spec;
    private String material;
    private BigDecimal weight;
    private BigDecimal price;
    private BigDecimal noTaxAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String orderCode;
    private Integer invoiceStatus;
    
    private String departmentName;//增加按部门显示 add afeng
    
    private Long departmentCount;//部门个数
    
    public Long getDepartmentCount() {
		return departmentCount;
	}
    private String orgName;

	public void setDepartmentCount(Long departmentCount) {
		this.departmentCount = departmentCount;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
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

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getNoTaxAmount() {
        return noTaxAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
        this.noTaxAmount = totalAmount.divide(InvoiceConstant.TAXRATE.add(BigDecimal.ONE), 2, BigDecimal.ROUND_HALF_UP);
        this.taxAmount = totalAmount.subtract(this.noTaxAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.totalAmount = totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
