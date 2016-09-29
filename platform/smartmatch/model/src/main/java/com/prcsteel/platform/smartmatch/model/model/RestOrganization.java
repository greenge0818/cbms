package com.prcsteel.platform.smartmatch.model.model;

import java.math.BigDecimal;

/**
 * Created by caochao on 2016/6/21.
 */
public class RestOrganization {
    private Long id;

    private Long provinceId;

    private Long cityId;

    private String cityName;

    private Long districtId;

    private String seqCode;

    private String name;

    private Long parentId;

    private Long charger;

    private String address;

    private String tel;

    private String fax;

    private BigDecimal creditLimit;

    private BigDecimal creditLimitUsed;

    private String contractAddress;

    private String invoicedHost;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    private Integer isOrg;

    private Integer status;

    private Integer enabled;

    private Integer isDraftAccepted;

    public RestOrganization() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public String getSeqCode() {
        return seqCode;
    }

    public void setSeqCode(String seqCode) {
        this.seqCode = seqCode;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditLimitUsed() {
        return creditLimitUsed;
    }

    public void setCreditLimitUsed(BigDecimal creditLimitUsed) {
        this.creditLimitUsed = creditLimitUsed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getCharger() {
        return charger;
    }

    public void setCharger(Long charger) {
        this.charger = charger;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Integer getModificationNumber() {
        return modificationNumber;
    }

    public void setModificationNumber(Integer modificationNumber) {
        this.modificationNumber = modificationNumber;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getParentRowId() {
        return parentRowId;
    }

    public void setParentRowId(String parentRowId) {
        this.parentRowId = parentRowId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getInvoicedHost() {
        return invoicedHost;
    }

    public void setInvoicedHost(String invoicedHost) {
        this.invoicedHost = invoicedHost;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getIsOrg() {
        return isOrg;
    }

    public void setIsOrg(Integer isOrg) {
        this.isOrg = isOrg;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getIsDraftAccepted() {
        return isDraftAccepted;
    }

    public void setIsDraftAccepted(Integer isDraftAccepted) {
        this.isDraftAccepted = isDraftAccepted;
    }
}
