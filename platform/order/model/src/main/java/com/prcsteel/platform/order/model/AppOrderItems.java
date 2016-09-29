package com.prcsteel.platform.order.model;

import java.math.BigDecimal;
import java.util.Date;

public class AppOrderItems {

    private String nsortName;//品名

    private String material;//材质

    private String spec;//品规

    private String factory;//钢厂

    private String warehouse;//仓库

    private String city;//仓库所在城市

    private BigDecimal dealPrice;//单价
    private BigDecimal weight;//单价

    private String weightConcept;//记重方式

    private BigDecimal actualPickWeight;

    public BigDecimal getActualPickWeight() {
        return actualPickWeight;
    }

    public void setActualPickWeight(BigDecimal actualPickWeight) {
        this.actualPickWeight = actualPickWeight;
    }

    public String getNsortName() {
        return nsortName;
    }

    public void setNsortName(String nsortName) {
        this.nsortName = nsortName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public BigDecimal getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(BigDecimal dealPrice) {
        this.dealPrice = dealPrice;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getWeightConcept() {
        return weightConcept;
    }

    public void setWeightConcept(String weightConcept) {
        this.weightConcept = weightConcept;
    }
}