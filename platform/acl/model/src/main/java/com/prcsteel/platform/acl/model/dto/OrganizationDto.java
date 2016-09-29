package com.prcsteel.platform.acl.model.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Rabbit Mao on 2015/7/18.
 */
public class OrganizationDto {
    private Long provinceId;
    private Long cityId;
    private Long districtId;
    private String seqCode;
    private String orgName;
    private BigDecimal creditLimit;
    private String chargerName;
    private int userCount;
    private int status;
    private String parentOrgName;
    private String address;
    private String contractAddress;
    private String fax;
    private String tel;
    private int isOrg;
    private List<String> deliveryTypeList;

    private Long parentOrgId;
    private Long orgId;
    private Long chargerId;
    private String invoicedHost;
    private Long enabled;

    private Integer isDraftAccepted;
    
    public Long getEnabled() {
		return enabled;
	}

	public void setEnabled(Long enabled) {
		this.enabled = enabled;
	}

	public int getIsOrg() {
        return isOrg;
    }

    public void setIsOrg(int isOrg) {
        this.isOrg = isOrg;
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

    public Long getProvinceId() {
        return provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public Long getDistrictId() {
        return districtId;
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

    public List<String> getDeliveryTypeList() {
        return deliveryTypeList;
    }

    public void setDeliveryTypeList(List<String> deliveryTypeList) {
        this.deliveryTypeList = deliveryTypeList;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }

    public Long getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(Long parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public Long getChargerId() {
        return chargerId;
    }

    public void setChargerId(Long chargerId) {
        this.chargerId = chargerId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String getChargerName() {
        return chargerName;
    }

    public void setChargerName(String chargerName) {
        this.chargerName = chargerName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getSeqCode() {
        return seqCode;
    }

    public void setSeqCode(String seqCode) {
        this.seqCode = seqCode;
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
