package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class InvoiceOutItemDetailDto {
    private Long id;

    private Long invoiceOutMainId;

    private String nsortName;

    private String material;

    private String spec;

    private BigDecimal weight;

    private BigDecimal price;

    private BigDecimal noTaxAmount;

    private BigDecimal taxAmount;

    private BigDecimal amount;

    private Date exportTime;

    private Date created;
    
    private String orgName;
    
    private String buyerName;
    
    private String code;

    private Long buyerId;

    private String taxCode;
    private String addr;
    private String tel;
    private String bankCode;
    private String bankNameMain;
    private String bankNameBranch;
    private Long applyDetailId;
    private Long checklistId;
    private String signOfSpec; //类型符号：无，Φ，△，折扣
    private String bankAccount;
    private Long orgId;
    private String invoicedHost;
    private String invoiceType;
    
    //nsortName  对应的大类名， 目前主要用于同步到小蜜蜂里面有用上，见ReceiptServiceImpl
    private String categoryName;
    
    public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvoiceOutMainId() {
        return invoiceOutMainId;
    }

    public void setInvoiceOutMainId(Long invoiceOutMainId) {
        this.invoiceOutMainId = invoiceOutMainId;
    }

    public String getNsortName() {
        return nsortName;
    }

    public void setNsortName(String nsortName) {
        this.nsortName = nsortName == null ? null : nsortName.trim();
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material == null ? null : material.trim();
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec == null ? null : spec.trim();
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

    public void setNoTaxAmount(BigDecimal noTaxAmount) {
        this.noTaxAmount = noTaxAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getExportTime() {
        return exportTime;
    }

    public void setExportTime(Date exportTime) {
        this.exportTime = exportTime;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankNameMain() {
        return bankNameMain;
    }

    public void setBankNameMain(String bankNameMain) {
        this.bankNameMain = bankNameMain;
    }

    public String getBankNameBranch() {
        return bankNameBranch;
    }

    public void setBankNameBranch(String bankNameBranch) {
        this.bankNameBranch = bankNameBranch;
    }

    public Long getApplyDetailId() {
        return applyDetailId;
    }

    public void setApplyDetailId(Long applyDetailId) {
        this.applyDetailId = applyDetailId;
    }

    public Long getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }

    public String getSignOfSpec() {
        return signOfSpec;
    }

    public void setSignOfSpec(String signOfSpec) {
        this.signOfSpec = signOfSpec;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getInvoicedHost() {
        return invoicedHost;
    }

    public void setInvoicedHost(String invoicedHost) {
        this.invoicedHost = invoicedHost;
    }

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
    
}