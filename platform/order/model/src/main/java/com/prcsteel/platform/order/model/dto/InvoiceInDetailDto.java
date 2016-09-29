package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

import com.prcsteel.platform.order.model.model.InvoiceInDetail;

/**
 * Created by lcw on 8/5/2015.
 */
public class InvoiceInDetailDto extends InvoiceInDetail {
    private Long poolInId;
    private Long poolInDetailId;
    private BigDecimal shouldAmount;
    private BigDecimal shouldWeight;
    private BigDecimal price;
    private String specName;
    private Long buyerId;
    private String buyerName;

    public Long getPoolInId() {
        return poolInId;
    }

    public void setPoolInId(Long poolInId) {
        this.poolInId = poolInId;
    }

    public Long getPoolInDetailId() {
        return poolInDetailId;
    }

    public void setPoolInDetailId(Long poolInDetailId) {
        this.poolInDetailId = poolInDetailId;
    }

    public BigDecimal getShouldWeight() {
        return shouldWeight;
    }

    public void setShouldWeight(BigDecimal shouldWeight) {
        this.shouldWeight = shouldWeight;
    }

    public BigDecimal getShouldAmount() {
        return shouldAmount;
    }

    public void setShouldAmount(BigDecimal shouldAmount) {
        this.shouldAmount = shouldAmount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
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

}
