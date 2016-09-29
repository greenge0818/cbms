package com.prcsteel.platform.account.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class AccountExt {
    private Integer id;

    private Long custAccountId;

    private String code;

    private String name;

    private String addr;

    private String zip;

    private String mailAddr;

    private String tel;

    private String fax;

    private String legalPersonName;

    private String mobil;

    private String webSiteUrl;

    private Long provinceId;

    private Long cityId;

    private Long districtId;

    private String proxyFactory;

    private BigDecimal proxyQty;

    private String licenseCode;

    private String regAddress;

    private String creditCode;

    private String orgCode;

    private String bankCode;

    private String bankNameMain;

    private String bankNameBranch;

    private Long bankProvinceId;

    private Long bankCityId;

    private String accountCode;

    private String taxCode;

    private String taxTel;

    private String taxBankNameMain;

    private String taxBankNameBranch;

    private String bankNumber;

    private Date regTime;

    private String invoiceType;

    private String invoiceSpeed;

    private String invoiceDataStatus;

    private String invoiceDataDeclineReason;

    private String bankDataStatus;

    private String bankDataReason;

    private String bankDataReminded;

    private String cardInfoStatus;

    private String cardInfoStatusDeclineReason;

    private String annualPurchaseAgreementStatus;

    private String annualPurchaseAgreementDeclineReason;

    private String sellerConsignAgreementStatus;

    private String sellerConsignAgreementDeclineReason;
    
    private String annualPurchaseBarCode;
    
    private String sellerConsignBarCode;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    private Boolean isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCustAccountId() {
        return custAccountId;
    }

    public void setCustAccountId(Long custAccountId) {
        this.custAccountId = custAccountId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getMailAddr() {
        return mailAddr;
    }

    public void setMailAddr(String mailAddr) {
        this.mailAddr = mailAddr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }

    public String getMobil() {
        return mobil;
    }

    public void setMobil(String mobil) {
        this.mobil = mobil;
    }

    public String getWebSiteUrl() {
        return webSiteUrl;
    }

    public void setWebSiteUrl(String webSiteUrl) {
        this.webSiteUrl = webSiteUrl;
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
        this.proxyFactory = proxyFactory;
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
        this.licenseCode = licenseCode;
    }

    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankNameMain() {
        return bankNameMain;
    }

    public void setBankNameMain(String bankNameMain) {
        this.bankNameMain = bankNameMain;
    }

    public String getBankNameBranch() {
        return bankNameBranch;
    }

    public void setBankNameBranch(String bankNameBranch) {
        this.bankNameBranch = bankNameBranch;
    }

    public Long getBankProvinceId() {
        return bankProvinceId;
    }

    public void setBankProvinceId(Long bankProvinceId) {
        this.bankProvinceId = bankProvinceId;
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
        this.accountCode = accountCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTaxTel() {
        return taxTel;
    }

    public void setTaxTel(String taxTel) {
        this.taxTel = taxTel;
    }

    public String getTaxBankNameMain() {
        return taxBankNameMain;
    }

    public void setTaxBankNameMain(String taxBankNameMain) {
        this.taxBankNameMain = taxBankNameMain;
    }

    public String getTaxBankNameBranch() {
        return taxBankNameBranch;
    }

    public void setTaxBankNameBranch(String taxBankNameBranch) {
        this.taxBankNameBranch = taxBankNameBranch;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceSpeed() {
        return invoiceSpeed;
    }

    public void setInvoiceSpeed(String invoiceSpeed) {
        this.invoiceSpeed = invoiceSpeed;
    }

    public String getInvoiceDataStatus() {
        return invoiceDataStatus;
    }

    public void setInvoiceDataStatus(String invoiceDataStatus) {
        this.invoiceDataStatus = invoiceDataStatus;
    }

    public String getInvoiceDataDeclineReason() {
        return invoiceDataDeclineReason;
    }

    public void setInvoiceDataDeclineReason(String invoiceDataDeclineReason) {
        this.invoiceDataDeclineReason = invoiceDataDeclineReason;
    }

    public String getBankDataStatus() {
        return bankDataStatus;
    }

    public void setBankDataStatus(String bankDataStatus) {
        this.bankDataStatus = bankDataStatus;
    }

    public String getBankDataReason() {
        return bankDataReason;
    }

    public void setBankDataReason(String bankDataReason) {
        this.bankDataReason = bankDataReason;
    }

    public String getBankDataReminded() {
        return bankDataReminded;
    }

    public void setBankDataReminded(String bankDataReminded) {
        this.bankDataReminded = bankDataReminded;
    }

    public String getCardInfoStatus() {
        return cardInfoStatus;
    }

    public void setCardInfoStatus(String cardInfoStatus) {
        this.cardInfoStatus = cardInfoStatus;
    }

    public String getAnnualPurchaseAgreementStatus() {
        return annualPurchaseAgreementStatus;
    }

    public void setAnnualPurchaseAgreementStatus(String annualPurchaseAgreementStatus) {
        this.annualPurchaseAgreementStatus = annualPurchaseAgreementStatus;
    }

    public String getAnnualPurchaseAgreementDeclineReason() {
        return annualPurchaseAgreementDeclineReason;
    }

    public void setAnnualPurchaseAgreementDeclineReason(String annualPurchaseAgreementDeclineReason) {
        this.annualPurchaseAgreementDeclineReason = annualPurchaseAgreementDeclineReason;
    }

    public String getSellerConsignAgreementStatus() {
        return sellerConsignAgreementStatus;
    }

    public void setSellerConsignAgreementStatus(String sellerConsignAgreementStatus) {
        this.sellerConsignAgreementStatus = sellerConsignAgreementStatus;
    }

    public String getSellerConsignAgreementDeclineReason() {
        return sellerConsignAgreementDeclineReason;
    }

    public void setSellerConsignAgreementDeclineReason(String sellerConsignAgreementDeclineReason) {
        this.sellerConsignAgreementDeclineReason = sellerConsignAgreementDeclineReason;
    }
    
    public String getAnnualPurchaseBarCode() {
		return annualPurchaseBarCode;
	}

	public void setAnnualPurchaseBarCode(String annualPurchaseBarCode) {
		this.annualPurchaseBarCode = annualPurchaseBarCode;
	}

	public String getSellerConsignBarCode() {
		return sellerConsignBarCode;
	}

	public void setSellerConsignBarCode(String sellerConsignBarCode) {
		this.sellerConsignBarCode = sellerConsignBarCode;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCardInfoStatusDeclineReason() {
        return cardInfoStatusDeclineReason;
    }

    public void setCardInfoStatusDeclineReason(String cardInfoStatusDeclineReason) {
        this.cardInfoStatusDeclineReason = cardInfoStatusDeclineReason;
    }
}