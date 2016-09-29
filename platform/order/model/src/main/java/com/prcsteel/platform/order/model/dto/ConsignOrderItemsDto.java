package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by Rolyer on 2015/12/2.
 */
public class ConsignOrderItemsDto {
    private String categoryName;
    private String material;
    private String spec;
    private String factory;
    private String warehouse;
    private BigDecimal price;
    private String sellerName;
    private Long cityId;
    private String categoryUuid;
    private BigDecimal priceDeviation;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public BigDecimal getPriceDeviation() {
        return priceDeviation;
    }

    public void setPriceDeviation(BigDecimal priceDeviation) {
        this.priceDeviation = priceDeviation;
    }
}
