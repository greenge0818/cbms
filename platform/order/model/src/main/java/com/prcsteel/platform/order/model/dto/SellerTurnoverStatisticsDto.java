package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by Rabbit Mao on 2015/8/20.
 */
public class SellerTurnoverStatisticsDto {
    private Long sellerId;
    private Long ownerId;
    private String sellerName;
    private String ownerName;
    private String consignType;
    private BigDecimal weightRange;
    private BigDecimal amountRange;
    private BigDecimal weightAll;
    private Integer publishCount;
    private Integer dealCount;

    public Integer getDealCount() {
        return dealCount;
    }

    public void setDealCount(Integer dealCount) {
        this.dealCount = dealCount;
    }

    public String getConsignType() {
        return consignType;
    }

    public void setConsignType(String consignType) {
        this.consignType = consignType;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public BigDecimal getWeightRange() {
        return weightRange;
    }

    public void setWeightRange(BigDecimal weightRange) {
        this.weightRange = weightRange;
    }

    public BigDecimal getAmountRange() {
        return amountRange;
    }

    public void setAmountRange(BigDecimal amountRange) {
        this.amountRange = amountRange;
    }

    public BigDecimal getWeightAll() {
        return weightAll;
    }

    public void setWeightAll(BigDecimal weightAll) {
        this.weightAll = weightAll;
    }

    public Integer getPublishCount() {
        return publishCount;
    }

    public void setPublishCount(Integer publishCount) {
        this.publishCount = publishCount;
    }
}
