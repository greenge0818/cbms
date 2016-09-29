package com.prcsteel.platform.order.web.vo;

/**
 * Created by kongbinheng on 15-8-5.
 */
public class InvoiceComputeVo {
    private Long id;
    private Long ownerId;
    private String ownerName;
    private Long orgId;
    private String orgName;
    private Long buyerId;
    private String buyerName;
    private Double  applyAmount;
    private Boolean isIndependent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public Double getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Double applyAmount) {
        this.applyAmount = applyAmount;
    }

    public Boolean getIsIndependent() {
        return isIndependent;
    }

    public void setIsIndependent(Boolean isIndependent) {
        this.isIndependent = isIndependent;
    }
}
