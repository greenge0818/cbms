package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by Rabbit Mao on 2015/7/18.
 */
public class EditOrganizationDto {
    private Long parentOrganizationId;
    private Long chagerId;
    private String organizationName;
    private String parentOrganizationName;
    private String code;
    private BigDecimal creditLimit;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Long getParentOrganizationId() {
        return parentOrganizationId;
    }

    public void setParentOrganizationId(Long parentOrganizationId) {
        this.parentOrganizationId = parentOrganizationId;
    }

    public Long getChagerId() {
        return chagerId;
    }

    public void setChagerId(Long chagerId) {
        this.chagerId = chagerId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getParentOrganizationName() {
        return parentOrganizationName;
    }

    public void setParentOrganizationName(String parentOrganizationName) {
        this.parentOrganizationName = parentOrganizationName;
    }
}
