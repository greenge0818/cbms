package com.prcsteel.platform.account.model.dto;

/**
 * Created by dengxiyan on 2016/3/5.
 */
public class CompanyDto {
    private Long id;
    private String name;
    private Long accountTag;
    private String orgName;
    private String regTime;
    private Integer status;
    private String purchaseAgreementStatus;
    private String consignAgreementStatus;
    private String invoiceDataStatus;
    private String bankDataStatus;//打款资料状态
    private String cardInfoStatus;
    private String purchaseAgreementReason;
    private String consignAgreementReason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAccountTag() {
        return accountTag;
    }

    public void setAccountTag(Long accountTag) {
        this.accountTag = accountTag;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPurchaseAgreementStatus() {
        return purchaseAgreementStatus;
    }

    public void setPurchaseAgreementStatus(String purchaseAgreementStatus) {
        this.purchaseAgreementStatus = purchaseAgreementStatus;
    }

    public String getConsignAgreementStatus() {
        return consignAgreementStatus;
    }

    public void setConsignAgreementStatus(String consignAgreementStatus) {
        this.consignAgreementStatus = consignAgreementStatus;
    }

    public String getInvoiceDataStatus() {
        return invoiceDataStatus;
    }

    public void setInvoiceDataStatus(String invoiceDataStatus) {
        this.invoiceDataStatus = invoiceDataStatus;
    }

    public String getBankDataStatus() {
        return bankDataStatus;
    }

    public void setBankDataStatus(String bankDataStatus) {
        this.bankDataStatus = bankDataStatus;
    }

    public String getCardInfoStatus() {
        return cardInfoStatus;
    }

    public void setCardInfoStatus(String cardInfoStatus) {
        this.cardInfoStatus = cardInfoStatus;
    }

    public String getPurchaseAgreementReason() {
        return purchaseAgreementReason;
    }

    public void setPurchaseAgreementReason(String purchaseAgreementReason) {
        this.purchaseAgreementReason = purchaseAgreementReason;
    }

    public String getConsignAgreementReason() {
        return consignAgreementReason;
    }

    public void setConsignAgreementReason(String consignAgreementReason) {
        this.consignAgreementReason = consignAgreementReason;
    }
}
