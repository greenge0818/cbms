package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class CustAccount {
    private Long id;

    private String code;

    private String name;

    private String addr;

    private String zip;

    private String tel;

    private BigDecimal balance;

    private BigDecimal balanceFreeze;

    private BigDecimal balanceSecondSettlement;

    private BigDecimal balanceSecondSettlementFreeze;

    private String fax;

    private String legalPersonName;

    private String mobil;

    private String business;

    private String webSiteUrl;

    private String businessType;

    private Long provinceId;

    private Long cityId;

    private Long districtId;

    private String proxyFactory;

    private BigDecimal proxyQty;

    private String licenseCode;

    private String regAddress;

    private String orgCode;

    private String bankCode;

    private String bankNameMain;

    private String bankNameBranch;

    private Long bankProvincieId;

    private Long bankCityId;

    private String accountCode;

    private String taxCode;

    private Date regTime;

    private Date lastBillTime;

    private Integer buyerDealTotal;
    
    private Integer sellerDealTotal;
    
    private String type;

    private String consignType;

    private Long managerId;

    private Integer status;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr == null ? null : addr.trim();
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip == null ? null : zip.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalanceFreeze() {
        return balanceFreeze;
    }

    public void setBalanceFreeze(BigDecimal balanceFreeze) {
        this.balanceFreeze = balanceFreeze;
    }

    public BigDecimal getBalanceSecondSettlement() {
        return balanceSecondSettlement;
    }

    public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
        this.balanceSecondSettlement = balanceSecondSettlement;
    }

    public BigDecimal getBalanceSecondSettlementFreeze() {
        return balanceSecondSettlementFreeze;
    }

    public void setBalanceSecondSettlementFreeze(BigDecimal balanceSecondSettlementFreeze) {
        this.balanceSecondSettlementFreeze = balanceSecondSettlementFreeze;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName == null ? null : legalPersonName.trim();
    }

    public String getMobil() {
        return mobil;
    }

    public void setMobil(String mobil) {
        this.mobil = mobil == null ? null : mobil.trim();
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business == null ? null : business.trim();
    }

    public String getWebSiteUrl() {
        return webSiteUrl;
    }

    public void setWebSiteUrl(String webSiteUrl) {
        this.webSiteUrl = webSiteUrl == null ? null : webSiteUrl.trim();
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public String getProxyFactory() {
        return proxyFactory;
    }

    public void setProxyFactory(String proxyFactory) {
        this.proxyFactory = proxyFactory == null ? null : proxyFactory.trim();
    }

    public BigDecimal getProxyQty() {
        return proxyQty;
    }

    public void setProxyQty(BigDecimal proxyQty) {
        this.proxyQty = proxyQty;
    }

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode == null ? null : licenseCode.trim();
    }

    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress == null ? null : regAddress.trim();
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public String getBankNameMain() {
        return bankNameMain;
    }

    public void setBankNameMain(String bankNameMain) {
        this.bankNameMain = bankNameMain == null ? null : bankNameMain.trim();
    }

    public String getBankNameBranch() {
        return bankNameBranch;
    }

    public void setBankNameBranch(String bankNameBranch) {
        this.bankNameBranch = bankNameBranch == null ? null : bankNameBranch.trim();
    }

    public Long getBankProvincieId() {
        return bankProvincieId;
    }

    public void setBankProvincieId(Long bankProvincieId) {
        this.bankProvincieId = bankProvincieId;
    }

    public Long getBankCityId() {
        return bankCityId;
    }

    public void setBankCityId(Long bankCityId) {
        this.bankCityId = bankCityId;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode == null ? null : accountCode.trim();
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode == null ? null : taxCode.trim();
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Date getLastBillTime() {
        return lastBillTime;
    }

    public void setLastBillTime(Date lastBillTime) {
        this.lastBillTime = lastBillTime;
    }

    public Integer getBuyerDealTotal() {
		return buyerDealTotal;
	}

	public void setBuyerDealTotal(Integer buyerDealTotal) {
		this.buyerDealTotal = buyerDealTotal;
	}

	public Integer getSellerDealTotal() {
		return sellerDealTotal;
	}

	public void setSellerDealTotal(Integer sellerDealTotal) {
		this.sellerDealTotal = sellerDealTotal;
	}

	public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getConsignType() {
        return consignType;
    }

    public void setConsignType(String consignType) {
        this.consignType = consignType == null ? null : consignType.trim();
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        this.createdBy = createdBy == null ? null : createdBy.trim();
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
        this.lastUpdatedBy = lastUpdatedBy == null ? null : lastUpdatedBy.trim();
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
        this.rowId = rowId == null ? null : rowId.trim();
    }

    public String getParentRowId() {
        return parentRowId;
    }

    public void setParentRowId(String parentRowId) {
        this.parentRowId = parentRowId == null ? null : parentRowId.trim();
    }
}