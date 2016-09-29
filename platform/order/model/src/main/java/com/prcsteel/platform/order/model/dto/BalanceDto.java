package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by rolyer on 15-8-5.
 */
public class BalanceDto {

    private String nsortName;
    private String material;
    private String spec;
    private BigDecimal total;

    public String getNsortName() {
        return nsortName;
    }

    public void setNsortName(String nsortName) {
        this.nsortName = nsortName;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}