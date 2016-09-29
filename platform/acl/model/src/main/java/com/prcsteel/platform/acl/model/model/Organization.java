package com.prcsteel.platform.acl.model.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Organization implements Serializable {
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

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    private Integer isOrg;

    private Integer status;

    private Integer enabled;

    private Integer isDraftAccepted;
    
    public Organization() {
    }

    public Organization(Long id,Long provinceId,Long cityId,Long districtId, String name, Long parentId, Long charger, Date created, String createdBy, Date lastUpdated, String lastUpdatedBy,Integer isOrg, Integer status) {
    	this(id,provinceId,cityId,districtId,name,parentId,charger,created,createdBy,lastUpdated,lastUpdatedBy,isOrg,status,0);
    }
    
    public Organization(Long id,Long provinceId,Long cityId,Long districtId, String name, Long parentId, Long charger, Date created, String createdBy, Date lastUpdated, String lastUpdatedBy,Integer isOrg, Integer status,Integer isDraftAccepted) {

        this.id = id;
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.districtId = districtId;
        this.name = name;
        this.parentId = parentId;
        this.charger = charger;
        this.created = created;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.isOrg=isOrg;
        this.status = status;
        this.isDraftAccepted = isDraftAccepted;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
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