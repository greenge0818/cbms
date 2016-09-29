package com.prcsteel.platform.smartmatch.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Rest 报价单明细 P0
 *
 * @author peanut
 * @date 2016/6/21
 */
public class QuotationItemsPO implements Serializable {

    /**
     * 报价单id
     */
    private String remoteCode;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 交货地，例：杭州市
     */
    private String city;

    /**
     * 品名UUid
     */
    private String categoryUuid;

    /**
     * 品名名称
     */
    private String CategoryName;

    /**
     * 材质uuid
     */
    private String materialUuid;

    /**
     * 材质名称
     */
    private String materialName;

    /**
     * 规格1
     */
    private String spec1;

    /**
     * 规格2
     */
    private String spec2;

    /**
     * 规格3
     */
    private String spec3;

    /**
     * 钢厂id
     */
    private Long factoryId;

    /**
     * 钢厂名称
     */
    private String factoryName;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 仓库所在城市id
     */
    private String cityId;

    /**
     * 仓库所在城市名称
     */
    private String cityName;

    /**
     * 仓库id
     */
    private String warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 重量
     */
    private String weight;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 记重方式
     */
    private String weightConcept;



    public QuotationItemsPO() {
    }



    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getMaterialUuid() {
        return materialUuid;
    }

    public void setMaterialUuid(String materialUuid) {
        this.materialUuid = materialUuid;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getSpec1() {
        return spec1;
    }

    public void setSpec1(String spec1) {
        this.spec1 = spec1;
    }

    public String getSpec2() {
        return spec2;
    }

    public void setSpec2(String spec2) {
        this.spec2 = spec2;
    }

    public String getSpec3() {
        return spec3;
    }

    public void setSpec3(String spec3) {
        this.spec3 = spec3;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getWeightConcept() {
        return weightConcept;
    }

    public void setWeightConcept(String weightConcept) {
        this.weightConcept = weightConcept;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRemoteCode() {
        return remoteCode;
    }

    public void setRemoteCode(String remoteCode) {
        this.remoteCode = remoteCode;
    }
}
