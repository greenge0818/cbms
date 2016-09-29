package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.enums.CertificateStatus;

import java.math.BigDecimal;

/**
 * @author peanut
 * @version 1.0
 * @description  需补齐买、卖家凭证的已开票订单Dto
 * @date 2016/4/13 10:28
 */
public class CertificateInvoiceDto {

    /*****记录id*****/
    private Long id;

    /*****订单Id*****/
    private Long orderId;

    /*****交易单号*****/
    private String code;

    /*****买家Id*****/
    private Long buyerId;

    /*****买家名称*****/
    private String buyerName;

    /*****卖家Id*****/
    private Long sellerId;

    /*****卖家名称*****/
    private String sellerName;

    /*****总重量*****/
    private BigDecimal totalWeight;

    /*****总金额*****/
    private BigDecimal totalAmount;

    /*****开单日期*****/
    private String openOrderDate;

    /*****交易员ID*****/
    private Long ownerId;

    /*****交易员名称*****/
    private String ownerName;

    /*****总数量*****/
    private int quantity;

    /*****实提总数量*****/
    private int actualPickTotalQuantity;

    /*****实提总重量*****/
    private BigDecimal actualPickTotalWeight;

    /*****实提总金额*****/
    private BigDecimal actualPickTotalAmount;

    /*****距离开单的时间*****/
    private int durationDay;

    /*****凭证号*****/
    private String credentialCode;

    /*****创建凭证日期*****/
    private String printDate;

    /*****凭证状态
     * @see CertificateStatus
     * **/
    private String certificateStatus;

    /*****客户类型 :买家 buyer、卖家seller*****/
    private String accountType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOpenOrderDate() {
        return openOrderDate;
    }

    public void setOpenOrderDate(String openOrderDate) {
        this.openOrderDate = openOrderDate;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getActualPickTotalQuantity() {
        return actualPickTotalQuantity;
    }

    public void setActualPickTotalQuantity(int actualPickTotalQuantity) {
        this.actualPickTotalQuantity = actualPickTotalQuantity;
    }

    public BigDecimal getActualPickTotalWeight() {
        return actualPickTotalWeight;
    }

    public void setActualPickTotalWeight(BigDecimal actualPickTotalWeight) {
        this.actualPickTotalWeight = actualPickTotalWeight;
    }

    public BigDecimal getActualPickTotalAmount() {
        return actualPickTotalAmount;
    }

    public void setActualPickTotalAmount(BigDecimal actualPickTotalAmount) {
        this.actualPickTotalAmount = actualPickTotalAmount;
    }

    public int getDurationDay() {
        return durationDay;
    }

    public void setDurationDay(int durationDay) {
        this.durationDay = durationDay;
    }

    public String getCredentialCode() {
        return credentialCode;
    }

    public void setCredentialCode(String credentialCode) {
        this.credentialCode = credentialCode;
    }

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public String getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(String certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
