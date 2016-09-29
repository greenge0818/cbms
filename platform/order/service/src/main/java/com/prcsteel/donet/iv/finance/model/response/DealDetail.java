package com.prcsteel.donet.iv.finance.model.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 提现详情
 * Created by lcw36 on 2015/9/7.
 */
@XmlRootElement(name = "DealDetail", namespace = "http://tempuri.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class DealDetail {

    @XmlElement(name = "PayInID", namespace = "http://tempuri.org/")
    private  Integer payInID;

    @XmlElement(name = "MobilePhone", namespace = "http://tempuri.org/")
    private String mobilePhone;

    @XmlElement(name = "Money", namespace = "http://tempuri.org/")
    private double money;

    @XmlElement(name = "Balance", namespace = "http://tempuri.org/")
    private double balance;

    @XmlElement(name = "DealDate", namespace = "http://tempuri.org/")
    private String dealDate;

    @XmlElement(name = "Remark", namespace = "http://tempuri.org/")
    private String remark;

    public Integer getPayInID() {
        return payInID;
    }

    public void setPayInID(Integer payInID) {
        this.payInID = payInID;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
