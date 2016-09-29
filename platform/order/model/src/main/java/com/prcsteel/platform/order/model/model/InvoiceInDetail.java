package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class InvoiceInDetail {
    private Long id;

    private Long invoiceInId;

    private String nsortName;

    private String material;

    private String spec;

    private BigDecimal weight;

    private BigDecimal noTaxAmount;

    private BigDecimal taxAmount;

    private BigDecimal amount;

    private String nsortNameComb;

    private String typeOfSpec;

    private String status;

    private BigDecimal actualAmount;

    private BigDecimal checkWeight;

    private BigDecimal checkNoTaxAmount;

    private BigDecimal checkTaxAmount;

    private BigDecimal checkAmount;

    private Long aliasId;
    private String aliasName;
    private String categoryName;
    private Long categoryId;
    
    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    private String ext1;

    private String ext2;

    private String ext3;

    private Integer ext4;

    private Integer ext5;

    private Integer ext6;

    private Date ext7;

    private Long ext8;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvoiceInId() {
        return invoiceInId;
    }

    public void setInvoiceInId(Long invoiceInId) {
        this.invoiceInId = invoiceInId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getCheckWeight() {
        return checkWeight;
    }

    public void setCheckWeight(BigDecimal checkWeight) {
        this.checkWeight = checkWeight;
    }

    public BigDecimal getCheckNoTaxAmount() {
        return checkNoTaxAmount;
    }

    public void setCheckNoTaxAmount(BigDecimal checkNoTaxAmount) {
        this.checkNoTaxAmount = checkNoTaxAmount;
    }

    public BigDecimal getCheckTaxAmount() {
        return checkTaxAmount;
    }

    public void setCheckTaxAmount(BigDecimal checkTaxAmount) {
        this.checkTaxAmount = checkTaxAmount;
    }

    public BigDecimal getCheckAmount() {
        return checkAmount;
    }

    public void setCheckAmount(BigDecimal checkAmount) {
        this.checkAmount = checkAmount;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Integer getModificationNumber() {
        return modificationNumber;
    }

    public void setModificationNumber(Integer modificationNumber) {
        this.modificationNumber = modificationNumber;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getParentRowId() {
        return parentRowId;
    }

    public void setParentRowId(String parentRowId) {
        this.parentRowId = parentRowId;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public Integer getExt4() {
        return ext4;
    }

    public void setExt4(Integer ext4) {
        this.ext4 = ext4;
    }

    public Integer getExt5() {
        return ext5;
    }

    public void setExt5(Integer ext5) {
        this.ext5 = ext5;
    }

    public Integer getExt6() {
        return ext6;
    }

    public void setExt6(Integer ext6) {
        this.ext6 = ext6;
    }

    public Date getExt7() {
        return ext7;
    }

    public void setExt7(Date ext7) {
        this.ext7 = ext7;
    }

    public Long getExt8() {
        return ext8;
    }

    public void setExt8(Long ext8) {
        this.ext8 = ext8;
    }

    @Override
    public String toString() {
        return "InvoiceInDetail{" +
                "id=" + id +
                ", invoiceInId=" + invoiceInId +
                ", nsortName='" + nsortName + '\'' +
                ", material='" + material + '\'' +
                ", spec='" + spec + '\'' +
                ", weight=" + weight +
                ", noTaxAmount=" + noTaxAmount +
                ", taxAmount=" + taxAmount +
                ", amount=" + amount +
                ", nsortNameComb='" + nsortNameComb + '\'' +
                ", typeOfSpec='" + typeOfSpec + '\'' +
                ", status='" + status + '\'' +
                ", actualAmount=" + actualAmount +
                ", checkWeight=" + checkWeight +
                ", checkNoTaxAmount=" + checkNoTaxAmount +
                ", checkTaxAmount=" + checkTaxAmount +
                ", checkAmount=" + checkAmount +
                ", created=" + created +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdated=" + lastUpdated +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", modificationNumber=" + modificationNumber +
                ", rowId='" + rowId + '\'' +
                ", parentRowId='" + parentRowId + '\'' +
                ", ext1='" + ext1 + '\'' +
                ", ext2='" + ext2 + '\'' +
                ", ext3='" + ext3 + '\'' +
                ", ext4=" + ext4 +
                ", ext5=" + ext5 +
                ", ext6=" + ext6 +
                ", ext7=" + ext7 +
                ", ext8=" + ext8 +
                '}';
    }

	public Long getAliasId() {
		return aliasId;
	}

	public void setAliasId(Long aliasId) {
		this.aliasId = aliasId;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}