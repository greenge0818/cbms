package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.prcsteel.platform.account.model.enums.AccountInvoiceType;

/**
 * Created by rolyer on 15-9-16.
 */
public class ChecklistDetailDto {
    private Long id;
    private Date applyTime;
    private String buyerName;
    private String nsortName;
    private String material;
    private String spec;
    private String typeOfSpec;
    private BigDecimal weight;
    private BigDecimal amount;
    private String orgName;
    private String invoiceType;

    private BigDecimal totalAmount;
    private BigDecimal totalWeight;
    
    public String getInvoiceType() {
    	return AccountInvoiceType.getValue(invoiceType);
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

	public String getTypeOfSpec() {
		return typeOfSpec;
	}

	public void setTypeOfSpec(String typeOfSpec) {
		this.typeOfSpec = typeOfSpec;
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
}
