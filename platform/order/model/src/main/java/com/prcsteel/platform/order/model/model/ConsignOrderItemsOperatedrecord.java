package com.prcsteel.platform.order.model.model;

import java.util.Date;

public class ConsignOrderItemsOperatedrecord {
    private Integer id;

    private Long orderId;

    private Integer changeOrderId;

    private String setToStatus;

    private Long operatorId;

    private String operatorName;

    private String note;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    public ConsignOrderItemsOperatedrecord() {
    }

    public ConsignOrderItemsOperatedrecord(Long orderId, Integer changeOrderId, String setToStatus, Long operatorId, String operatorName, Date created, String createdBy, Date lastUpdated, String lastUpdatedBy) {
        this.orderId = orderId;
        this.changeOrderId = changeOrderId;
        this.setToStatus = setToStatus;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.created = created;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Integer getChangeOrderId() {
        return changeOrderId;
    }

    public void setChangeOrderId(Integer changeOrderId) {
        this.changeOrderId = changeOrderId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getSetToStatus() {
        return setToStatus;
    }

    public void setSetToStatus(String setToStatus) {
        this.setToStatus = setToStatus;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
}