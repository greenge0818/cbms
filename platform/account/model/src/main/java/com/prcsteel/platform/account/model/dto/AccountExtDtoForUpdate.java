package com.prcsteel.platform.account.model.dto;

import com.prcsteel.platform.acl.model.model.User;

/**
 * Created by dengxiyan on 2016/5/5.
 */
public class AccountExtDtoForUpdate {
    private User user;
    private Long accountId;
    private String oldSellerConsignAgreementStatus;
    private String newSellerConsignAgreementStatus;
    private String oldAnnualPurchaseAgreementStatus;
    private String newAnnualPurchaseAgreementStatus;
    private String auditPass;//1审核通过 0不通过
    private String sellerConsignAgreementDeclineReason;
    private String annualPurchaseAgreementDeclineReason;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getOldSellerConsignAgreementStatus() {
        return oldSellerConsignAgreementStatus;
    }

    public void setOldSellerConsignAgreementStatus(String oldSellerConsignAgreementStatus) {
        this.oldSellerConsignAgreementStatus = oldSellerConsignAgreementStatus;
    }

    public String getNewSellerConsignAgreementStatus() {
        return newSellerConsignAgreementStatus;
    }

    public void setNewSellerConsignAgreementStatus(String newSellerConsignAgreementStatus) {
        this.newSellerConsignAgreementStatus = newSellerConsignAgreementStatus;
    }

    public String getOldAnnualPurchaseAgreementStatus() {
        return oldAnnualPurchaseAgreementStatus;
    }

    public void setOldAnnualPurchaseAgreementStatus(String oldAnnualPurchaseAgreementStatus) {
        this.oldAnnualPurchaseAgreementStatus = oldAnnualPurchaseAgreementStatus;
    }

    public String getNewAnnualPurchaseAgreementStatus() {
        return newAnnualPurchaseAgreementStatus;
    }

    public void setNewAnnualPurchaseAgreementStatus(String newAnnualPurchaseAgreementStatus) {
        this.newAnnualPurchaseAgreementStatus = newAnnualPurchaseAgreementStatus;
    }

    public String getSellerConsignAgreementDeclineReason() {
        return sellerConsignAgreementDeclineReason;
    }

    public void setSellerConsignAgreementDeclineReason(String sellerConsignAgreementDeclineReason) {
        this.sellerConsignAgreementDeclineReason = sellerConsignAgreementDeclineReason;
    }

    public String getAnnualPurchaseAgreementDeclineReason() {
        return annualPurchaseAgreementDeclineReason;
    }

    public void setAnnualPurchaseAgreementDeclineReason(String annualPurchaseAgreementDeclineReason) {
        this.annualPurchaseAgreementDeclineReason = annualPurchaseAgreementDeclineReason;
    }

    public String getAuditPass() {
        return auditPass;
    }

    public void setAuditPass(String auditPass) {
        this.auditPass = auditPass;
    }
}
