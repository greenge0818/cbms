package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

/***
 * @author  peanut
 * @description 需补齐买、卖家凭证的已开票订单表对象
 * @date 2016/04/12
 */
public class BusiConsignOrderCertificateInvoice {

    /*****主健ID*****/
    private Long id;

    /*****交易单号*****/
    private String code;

    /*****凭证号*****/
    private String credentialCode;

    /*****买家ID*****/
    private Long buyerId;

    /*****卖家ID*****/
    private Long sellerId;

    /*****买家名称*****/
    private String buyerName;

    /*****卖家名称*****/
    private String sellerName;

    /*****交易员ID*****/
    private Long ownerId;

    /*****交易员名称*****/
    private String ownerName;

    /*****件数*****/
    private Integer quantity;

    /*****总重量*****/
    private BigDecimal totalWeight;

    /*****实提总重量*****/
    private BigDecimal actualPickTotalWeight;

    /*****总金额*****/
    private BigDecimal totalAmount;

    /*****实提总金额*****/
    private BigDecimal actualPickTotalAmount;

    /*****凭证状态*****/
    private String status;

    /*****开单日期*****/
    private String openOrderDate;

    /*****凭证创建日期*****/
    private String printDate;

    /*****距开单时间已有（天）*****/
    private int durationDay;

    /*****回收截止日期*****/
    private String expiryDay;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer total;

    public BusiConsignOrderCertificateInvoice() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getCredentialCode() {
        return credentialCode;
    }

    public void setCredentialCode(String credentialCode) {
        this.credentialCode = credentialCode == null ? null : credentialCode.trim();
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName == null ? null : buyerName.trim();
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName == null ? null : sellerName.trim();
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
        this.ownerName = ownerName == null ? null : ownerName.trim();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public BigDecimal getActualPickTotalWeight() {
        return actualPickTotalWeight;
    }

    public void setActualPickTotalWeight(BigDecimal actualPickTotalWeight) {
        this.actualPickTotalWeight = actualPickTotalWeight;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getActualPickTotalAmount() {
        return actualPickTotalAmount;
    }

    public void setActualPickTotalAmount(BigDecimal actualPickTotalAmount) {
        this.actualPickTotalAmount = actualPickTotalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getOpenOrderDate() {
        return openOrderDate;
    }

    public void setOpenOrderDate(String openOrderDate) {
        this.openOrderDate = openOrderDate;
    }

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
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
        this.createdBy = createdBy == null ? null : createdBy.trim();
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
        this.lastUpdatedBy = lastUpdatedBy == null ? null : lastUpdatedBy.trim();
    }

    public void setTotal(Integer total){this.total=total;}

    public Integer getTotal() {return total;}

    public int getDurationDay() { return durationDay; }

    public void setDurationDay(int durationDay) {this.durationDay = durationDay;}

    public String getExpiryDay() { return expiryDay; }

    public void setExpiryDay(String expiryDay) {this.expiryDay = expiryDay;}
}