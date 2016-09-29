package com.prcsteel.platform.account.model.dto;

import com.prcsteel.platform.account.model.model.Account;

/**
 * Created by caochao on 2016/5/25.
 */
public class AccountForAppDto extends Account {
    private String proxyFactory;
    private String contact;
    private String contactTel;
    private String nsortName;

    @Override
    public String getProxyFactory() {
        return proxyFactory;
    }

    @Override
    public void setProxyFactory(String proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getNsortName() {
        return nsortName;
    }

    public void setNsortName(String nsortName) {
        this.nsortName = nsortName;
    }
}
