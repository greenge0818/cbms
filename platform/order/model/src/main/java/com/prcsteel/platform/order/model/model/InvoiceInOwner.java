package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class InvoiceInOwner {
    private Long id;

    private Long orgFromId;

    private String orgFromName;

    private Long orgToId;

    private String orgToName;

    private Long sellerId;

    private String sellerName;

    private String nsortName;

    private String material;

    private String spec;

    private BigDecimal notInvoicedAmount;

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

    public Long getOrgFromId() {
        return orgFromId;
    }

    public void setOrgFromId(Long orgFromId) {
        this.orgFromId = orgFromId;
    }

    public String getOrgFromName() {
        return orgFromName;
    }

    public void setOrgFromName(String orgFromName) {
        this.orgFromName = orgFromName;
    }

    public Long getOrgToId() {
        return orgToId;
    }

    public void setOrgToId(Long orgToId) {
        this.orgToId = orgToId;
    }

    public String getOrgToName() {
        return orgToName;
    }

    public void setOrgToName(String orgToName) {
        this.orgToName = orgToName;
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

    public BigDecimal getNotInvoicedAmount() {
        return notInvoicedAmount;
    }

    public void setNotInvoicedAmount(BigDecimal notInvoicedAmount) {
        this.notInvoicedAmount = notInvoicedAmount;
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

    public InvoiceInOwner() {
    }

    public InvoiceInOwner(Long orgFromId, String orgFromName, Long orgToId,
                          String orgToName, Long sellerId, String sellerName, String nsortName, String material,
                          String spec, BigDecimal notInvoicedAmount, String loginId) {
        Date now = new Date();
        this.orgFromId = orgFromId;
        this.orgFromName = orgFromName;
        this.orgToId = orgToId;
        this.orgToName = orgToName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.nsortName = nsortName;
        this.material = material;
        this.spec = spec;
        this.notInvoicedAmount = notInvoicedAmount;
        this.created = now;
        this.createdBy = loginId;
        this.lastUpdated = now;
        this.lastUpdatedBy = loginId;
        this.modificationNumber = 0;
    }
}