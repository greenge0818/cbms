package com.prcsteel.platform.smartmatch.model.model;

import java.util.Date;

public class PurchaseOrderItemAttributes {
    private Long id;

    private Long purchaseOrderItemId;

    private String attributeUuid;

    private String value;

    private String remark;

    private Date created;

    private String createdBy;

    public PurchaseOrderItemAttributes() {
    }

    public PurchaseOrderItemAttributes(Long purchaseOrderItemId, String attributeUuid, String value, Date created, String createdBy) {
        this.purchaseOrderItemId = purchaseOrderItemId;
        this.value = value;
        this.attributeUuid = attributeUuid;
        this.created = created;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPurchaseOrderItemId() {
        return purchaseOrderItemId;
    }

    public void setPurchaseOrderItemId(Long purchaseOrderItemId) {
        this.purchaseOrderItemId = purchaseOrderItemId;
    }

    public String getAttributeUuid() {
        return attributeUuid;
    }

    public void setAttributeUuid(String attributeUuid) {
        this.attributeUuid = attributeUuid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}