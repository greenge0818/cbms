package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class ReportSellerInvoiceIn {
    private Long id;

    private Date happenTime;

    private Long sellerId;

    private String sellerName;

    private Long orderId;

    private String orderCode;

    private String contractCode;

    private BigDecimal orderAmount;

    private BigDecimal invoiceInAmount;

    private BigDecimal invoiceInBalance;

    private String operationType;

    private String remark;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(Date happenTime) {
        this.happenTime = happenTime;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getInvoiceInAmount() {
        return invoiceInAmount;
    }

    public void setInvoiceInAmount(BigDecimal invoiceInAmount) {
        this.invoiceInAmount = invoiceInAmount;
    }

    public BigDecimal getInvoiceInBalance() {
        return invoiceInBalance;
    }

    public void setInvoiceInBalance(BigDecimal invoiceInBalance) {
        this.invoiceInBalance = invoiceInBalance;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
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

	@Override
	public String toString() {
		return "ReportSellerInvoiceIn [id=" + id + ", happenTime=" + happenTime + ", sellerId=" + sellerId
				+ ", sellerName=" + sellerName + ", orderId=" + orderId + ", orderCode=" + orderCode + ", contractCode="
				+ contractCode + ", orderAmount=" + orderAmount + ", invoiceInAmount=" + invoiceInAmount
				+ ", invoiceInBalance=" + invoiceInBalance + ", operationType=" + operationType + ", remark=" + remark
				+ ", created=" + created + ", createdBy=" + createdBy + ", lastUpdated=" + lastUpdated
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", modificationNumber=" + modificationNumber + "]";
	}
}