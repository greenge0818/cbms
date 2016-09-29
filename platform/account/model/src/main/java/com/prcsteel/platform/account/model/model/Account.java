package com.prcsteel.platform.account.model.model;

import com.prcsteel.platform.common.utils.CbmsNumberUtil;

import java.math.BigDecimal;
import java.util.Date;

public class Account {
    private Long id;

    private String code;

    private String name;

    private String addr;

    private String zip;

    private String mailAddr;

    private String tel;

    private BigDecimal balance;

    private BigDecimal balanceFreeze;

    private BigDecimal balanceSecondSettlement;

    private BigDecimal proxyQty;

    private BigDecimal balanceSecondSettlementFreeze;

    private BigDecimal balanceRebate;

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

    private String licenseCode;

    private String regAddress;

    private String orgCode;

    private String bankNameMain;

    private String bankCode;

    private String bankNameBranch;

    private Long bankProvinceId;

    private Long bankCityId;

    private String accountCode;

    private String taxCode;

    private Date regTime;

    private Date lastBillTime;

    private Integer buyerDealTotal;

    private Integer sellerDealTotal;

    private String type;

    private Long accountTag;

    private String consignType;

    private Long managerId;

    private Long orgId;

    private String orgName;

    private Integer status;
    
    private String invoiceType;
    
    private String checkInvoiceType;
    
    private String invoiceDataStatus;
    
    private String invoiceDataDeclineReason;

    private String bankDataStatus;

    private String bankDataReason;

    private String bankDataReminded;

    private String registerDate;

    private BigDecimal registerCapital;

    private String supplierLabel;

    private String buyerLabel;
    
    private BigDecimal sellerSingleTradeWeight;
    
    private BigDecimal sellerAllTradeWeight;
    
    private BigDecimal sellerAllTradeQuality;
    
    private Integer isSellerPercent;

    private String paymentLabel;

	private String invoiceSpeed;

    private String belongOrg; //chengui 部门归属服务中心

    private String deptFax; //chengui 部门传真

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;
    
    private Integer isAcceptDraftCharged;

    private Boolean isDeleted;

    private Long parentId;

    private BigDecimal creditAmount;

    private String structureType;

    private String tagName;

    private BigDecimal creditAmountUsed;

    private Integer isAutoSecondPayment;
    
    private boolean especially;

    private BigDecimal kdBalanceSecondSettlement;//提供给款道的二结余额

    public BigDecimal getKdBalanceSecondSettlement() {
        return kdBalanceSecondSettlement;
    }

    public void setKdBalanceSecondSettlement(BigDecimal kdBalanceSecondSettlement) {
        this.kdBalanceSecondSettlement = kdBalanceSecondSettlement;
    }

    public boolean isEspecially() {
		return especially;
	}

	public void setEspecially(boolean especially) {
		this.especially = especially;
	}

	public Integer getIsAcceptDraftCharged() {
        return isAcceptDraftCharged == null ? 0 : isAcceptDraftCharged;
    }

    public void setIsAcceptDraftCharged(Integer isAcceptDraftCharged) {
        this.isAcceptDraftCharged = isAcceptDraftCharged;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCheckInvoiceType() {
        return checkInvoiceType;
    }

    public void setCheckInvoiceType(String checkInvoiceType) {
        this.checkInvoiceType = checkInvoiceType;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

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

    public BigDecimal getProxyQty() {
        return proxyQty;
    }

    public void setProxyQty(BigDecimal proxyQty) {
        this.proxyQty = proxyQty;
    }

    public BigDecimal getBalanceSecondSettlementFreeze() {
        return balanceSecondSettlementFreeze;
    }

    public void setBalanceSecondSettlementFreeze(BigDecimal balanceSecondSettlementFreeze) {
        this.balanceSecondSettlementFreeze = balanceSecondSettlementFreeze;
    }

    public BigDecimal getBalanceRebate() {
        return balanceRebate;
    }

    public void setBalanceRebate(BigDecimal balanceRebate) {
        this.balanceRebate = balanceRebate;
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

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getWebSiteUrl() {
        return webSiteUrl;
    }

    public void setWebSiteUrl(String webSiteUrl) {
        this.webSiteUrl = webSiteUrl;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getBankNameMain() {
        return bankNameMain;
    }

    public void setBankNameMain(String bankNameMain) {
        this.bankNameMain = bankNameMain;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
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
        this.type = type;
    }

    public Long getAccountTag() {
        return accountTag;
    }

    public void setAccountTag(Long accountTag) {
        this.accountTag = accountTag;
    }

    public String getConsignType() {
        return consignType;
    }

    public void setConsignType(String consignType) {
        this.consignType = consignType;
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

    public Date getCreated() {
        return created;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public BigDecimal getRegisterCapital() {
        return registerCapital;
    }

    public void setRegisterCapital(BigDecimal registerCapital) {
        this.registerCapital = registerCapital;
    }

    public String getSupplierLabel() {
        return supplierLabel;
    }

    public void setSupplierLabel(String supplierLabel) {
        this.supplierLabel = supplierLabel;
    }

    public String getBuyerLabel() {
        return buyerLabel;
    }

    public void setBuyerLabel(String buyerLabel) {
        this.buyerLabel = buyerLabel;
    }

    public String getPaymentLabel() {
        return paymentLabel;
    }

    public void setPaymentLabel(String paymentLabel) {
        this.paymentLabel = paymentLabel;
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

    public String getInvoiceSpeed() {
        return invoiceSpeed;
    }

    public void setInvoiceSpeed(String invoiceSpeed) {
        this.invoiceSpeed = invoiceSpeed;
    }

	public BigDecimal getSellerSingleTradeWeight() {
		return sellerSingleTradeWeight;
	}

	public void setSellerSingleTradeWeight(BigDecimal sellerSingleTradeWeight) {
		this.sellerSingleTradeWeight = sellerSingleTradeWeight;
	}

	public BigDecimal getSellerAllTradeWeight() {
		return sellerAllTradeWeight;
	}

	public void setSellerAllTradeWeight(BigDecimal sellerAllTradeWeight) {
		this.sellerAllTradeWeight = sellerAllTradeWeight;
	}

	public BigDecimal getSellerAllTradeQuality() {
		return sellerAllTradeQuality;
	}

	public void setSellerAllTradeQuality(BigDecimal sellerAllTradeQuality) {
		this.sellerAllTradeQuality = sellerAllTradeQuality;
	}

	public Integer getIsSellerPercent() {
		return isSellerPercent;
	}

	public void setIsSellerPercent(Integer isSellerPercent) {
		this.isSellerPercent = isSellerPercent;
	}
    

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getStructureType() {
        return structureType;
    }

    public void setStructureType(String structureType) {
        this.structureType = structureType;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public BigDecimal getCreditAmountUsed() {
        return creditAmountUsed;
    }

    public void setCreditAmountUsed(BigDecimal creditAmountUsed) {
        this.creditAmountUsed = creditAmountUsed;
    }

    public Integer getIsAutoSecondPayment() {
        return isAutoSecondPayment;
    }

    public void setIsAutoSecondPayment(Integer isAutoSecondPayment) {
        this.isAutoSecondPayment = isAutoSecondPayment;
    }

    public BigDecimal getBalanceCreditAmount(){
        return CbmsNumberUtil.buildMoney(this.creditAmount).subtract(CbmsNumberUtil.buildMoney(this.creditAmountUsed));
    }

    public String getBelongOrg() {
        return belongOrg;
    }

    public void setBelongOrg(String belongOrg) {
        this.belongOrg = belongOrg;
    }

    public String getDeptFax() {
        return deptFax;
    }

    public void setDeptFax(String deptFax) {
        this.deptFax = deptFax;
    }
}




