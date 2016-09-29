package com.prcsteel.donet.iv.finance.model.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 提现金额
 * Created by lcw36 on 2015/9/7.
 */
@XmlRootElement(name = "CbmsAmount", namespace = "http://tempuri.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class CbmsAmount {

    @XmlElement(name = "MobilePhone", namespace = "http://tempuri.org/")
    private String mobilePhone;

    @XmlElement(name = "Amount", namespace = "http://tempuri.org/")
    private double amount;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
