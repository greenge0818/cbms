package com.prcsteel.platform.account.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Rolyer on 2016/1/27.
 */
public class ContractInfoDto implements Serializable{
    private String companyName;
    private String contractNo;
    private String year;
    private String month;
    private String date;
    private String contractAddress;
    private String totalQuantity;
    private BigDecimal totalWeight;
    private BigDecimal totalAmount;
    private String totalCapital;
    private String payTypeFirst;
    private String payTypeSecond;
    private String payTypeThird;
    private String payTypeFourth;
    private String deliveryType;
    private String outboundTaker;
    private String feeTaker;
    private String addr;
    private String orgAddr;
    private String orgName;
    private String fax;
    private String orgFax;
    private String legalPersonName;
    private String delayDays;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalCapital() {
        return totalCapital;
    }

    public void setTotalCapital(String totalCapital) {
        this.totalCapital = totalCapital;
    }

    public String getPayTypeFirst() {
        return payTypeFirst;
    }

    public void setPayTypeFirst(String payTypeFirst) {
        this.payTypeFirst = payTypeFirst;
    }

    public String getPayTypeSecond() {
        return payTypeSecond;
    }

    public void setPayTypeSecond(String payTypeSecond) {
        this.payTypeSecond = payTypeSecond;
    }

    public String getPayTypeThird() {
        return payTypeThird;
    }

    public void setPayTypeThird(String payTypeThird) {
        this.payTypeThird = payTypeThird;
    }

    public String getPayTypeFourth() {
        return payTypeFourth;
    }

    public void setPayTypeFourth(String payTypeFourth) {
        this.payTypeFourth = payTypeFourth;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getOutboundTaker() {
        return outboundTaker;
    }

    public void setOutboundTaker(String outboundTaker) {
        this.outboundTaker = outboundTaker;
    }

    public String getFeeTaker() {
        return feeTaker;
    }

    public void setFeeTaker(String feeTaker) {
        this.feeTaker = feeTaker;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getOrgAddr() {
        return orgAddr;
    }

    public void setOrgAddr(String orgAddr) {
        this.orgAddr = orgAddr;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getOrgFax() {
        return orgFax;
    }

    public void setOrgFax(String orgFax) {
        this.orgFax = orgFax;
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }

    public String getDelayDays() {
        return delayDays;
    }

    public void setDelayDays(String delayDays) {
        this.delayDays = delayDays;
    }
}
