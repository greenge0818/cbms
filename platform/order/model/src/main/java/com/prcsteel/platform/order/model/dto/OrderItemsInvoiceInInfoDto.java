package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by prcsteel on 2016/1/4.
 */
public class OrderItemsInvoiceInInfoDto {
    private String created;
    private String areaCode;
    private String code;
    private String sellerName;
    private String nsortNameComb;
    private String typeOfSpec;
    private BigDecimal price;
    private BigDecimal checkWeight;
    private BigDecimal checkAmount;
    private BigDecimal weight;
    private BigDecimal amount;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getNsortNameComb() {
        return nsortNameComb;
    }

    public void setNsortNameComb(String nsortNameComb) {
        this.nsortNameComb = nsortNameComb;
    }

    public String getTypeOfSpec() {
        return typeOfSpec;
    }

    public void setTypeOfSpec(String typeOfSpec) {
        this.typeOfSpec = typeOfSpec;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCheckWeight() {
        return checkWeight;
    }

    public void setCheckWeight(BigDecimal checkWeight) {
        this.checkWeight = checkWeight;
    }

    public BigDecimal getCheckAmount() {
        return checkAmount;
    }

    public void setCheckAmount(BigDecimal checkAmount) {
        this.checkAmount = checkAmount;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
