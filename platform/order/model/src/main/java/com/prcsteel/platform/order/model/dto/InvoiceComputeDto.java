package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kongbinheng on 2015/8/3.
 */
public class InvoiceComputeDto {
    private Long applyId;
    private Long poolOutId;
    private Long poolOutDetailId;
    private Long orgId;
    private String orgName;
    private Long ownerId;
    private String ownerName;
    private Long buyerId;
    private String buyerName;
    private String nsortName;
    private String material;
    private String spec;
    private BigDecimal totalAmount;
    private BigDecimal totalWeight;
    private BigDecimal sentAmount;
    private BigDecimal sentWeight;
    private BigDecimal applyAmount;
    private BigDecimal actualAmount;
    private Date created;
    private String createdBy;
    private Date lastUpdated;
    private String lastUpdatedBy;

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public Long getPoolOutId() {
        return poolOutId;
    }

    public void setPoolOutId(Long poolOutId) {
        this.poolOutId = poolOutId;
    }

    public Long getPoolOutDetailId() {
        return poolOutDetailId;
    }

    public void setPoolOutDetailId(Long poolOutDetailId) {
        this.poolOutDetailId = poolOutDetailId;
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

    public BigDecimal getSentAmount() {
        return sentAmount;
    }

    public void setSentAmount(BigDecimal sentAmount) {
        this.sentAmount = sentAmount;
    }

    public BigDecimal getSentWeight() {
        return sentWeight;
    }

    public void setSentWeight(BigDecimal sentWeight) {
        this.sentWeight = sentWeight;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
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
}
 