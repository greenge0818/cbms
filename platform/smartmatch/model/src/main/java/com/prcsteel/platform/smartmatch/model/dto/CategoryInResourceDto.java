package com.prcsteel.platform.smartmatch.model.dto;

import java.math.BigDecimal;

/**
 * Created by Rolyer on 2015/11/30.
 */
public class CategoryInResourceDto {

    /**
     * 资源ID
     */
    private Long id;
    /**
     * 大类
     */
    private String categoryGroupName;
    private String categoryGroupUuid;
    /**
     * 品名
     */
    private String categoryName;
    private String categoryUuid;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 城市
     */
    private Long cityId;

    /**
     * 规格
     */
    private String spec;

    /**
     * 规格种数
     */
    private Integer varietyQuantity;

    /**
     * 价格偏差率
     */
    private BigDecimal priceDeviation;

    /**
     * 库存价格
     */
    private BigDecimal price;

    /**
     * 材质
     */
    private String materialName;

    /**
     * 厂家
     */
    private String factoryName;

    /**
     * 仓库
     */
    private String warehouseName;

    /**
     * 卖家
     */
    private String sellerName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryGroupName() {
        return categoryGroupName;
    }

    public void setCategoryGroupName(String categoryGroupName) {
        this.categoryGroupName = categoryGroupName;
    }

    public String getCategoryGroupUuid() {
        return categoryGroupUuid;
    }

    public void setCategoryGroupUuid(String categoryGroupUuid) {
        this.categoryGroupUuid = categoryGroupUuid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Integer getVarietyQuantity() {
        return varietyQuantity;
    }

    public void setVarietyQuantity(Integer varietyQuantity) {
        this.varietyQuantity = varietyQuantity;
    }

    public BigDecimal getPriceDeviation() {
        return priceDeviation;
    }

    public void setPriceDeviation(BigDecimal priceDeviation) {
        this.priceDeviation = priceDeviation;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
}
