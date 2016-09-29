package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.account.model.model.AccountContact;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by prcsteel on 2015/12/24.
 */
public class OrderContactDto {
    private Long accountId;

    private String accountName;

    private Long contactId;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public BigDecimal getRebateAmount() {
        return rebateAmount;
    }

    public void setRebateAmount(BigDecimal rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    private String contactName;

    private String contactTel;

    private BigDecimal rebateAmount;
    private List<AccountContact> accountContactList;

    public List<AccountContact> getAccountContactList() {
        return accountContactList;
    }

    public void setAccountContactList(List<AccountContact> accountContactList) {
        this.accountContactList = accountContactList;
    }
}
