package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class ConsignOrderChange {
    private Integer id;

    private Long orderId;

    private String code;

    private String deliveryType;

    private Date deliveryEndDate;

    private String feeTaker;

    private BigDecimal shipFee;

    private String outboundTaker;

    private BigDecimal outboundFee;

    private String contractAddress;

    private String transArea;

    private BigDecimal changeRelateAmount;

    private BigDecimal secondBalanceTakeout;

    private String contractCode;

    private String status;
    
    private String payStatus;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Date getDeliveryEndDate() {
        return deliveryEndDate;
    }

    public void setDeliveryEndDate(Date deliveryEndDate) {
        this.deliveryEndDate = deliveryEndDate;
    }

    public String getFeeTaker() {
        return feeTaker;
    }

    public void setFeeTaker(String feeTaker) {
        this.feeTaker = feeTaker;
    }

    public BigDecimal getShipFee() {
        return shipFee;
    }

    public void setShipFee(BigDecimal shipFee) {
        this.shipFee = shipFee;
    }

    public String getOutboundTaker() {
        return outboundTaker;
    }

    public void setOutboundTaker(String outboundTaker) {
        this.outboundTaker = outboundTaker;
    }

    public BigDecimal getOutboundFee() {
        return outboundFee;
    }

    public void setOutboundFee(BigDecimal outboundFee) {
        this.outboundFee = outboundFee;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getTransArea() {
        return transArea;
    }

    public void setTransArea(String transArea) {
        this.transArea = transArea;
    }

    public BigDecimal getChangeRelateAmount() {
        return changeRelateAmount;
    }

    public void setChangeRelateAmount(BigDecimal changeRelateAmount) {
        this.changeRelateAmount = changeRelateAmount;
    }

    public BigDecimal getSecondBalanceTakeout() {
        return secondBalanceTakeout;
    }

    public void setSecondBalanceTakeout(BigDecimal secondBalanceTakeout) {
        this.secondBalanceTakeout = secondBalanceTakeout;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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