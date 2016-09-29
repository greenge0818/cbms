package com.prcsteel.platform.order.model.dto;

import java.util.Date;

/**
 * Created by caochao on 2015/8/25.
 */
public class BuyerTradeStatisticsDto {
  private String companyName;
    private String contactName;
    private Integer orderCount;
    private Integer tempOrderCount;
    private Integer contactOrderCount;
    private Double frequencyOfContact;
    private Integer totalOrderCount;
    private Double frequencyOfCompany;
    private Date firstTradeTime;
    private Date lastestTradeTime;
    private Integer num;//历史采购数量
    private String isFirst;//是否首次

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getTempOrderCount() {
        return tempOrderCount;
    }

    public void setTempOrderCount(Integer tempOrderCount) {
        this.tempOrderCount = tempOrderCount;
    }

    public Integer getContactOrderCount() {
        return contactOrderCount;
    }

    public void setContactOrderCount(Integer contactOrderCount) {
        this.contactOrderCount = contactOrderCount;
    }

    public Double getFrequencyOfContact() {
        return frequencyOfContact;
    }

    public void setFrequencyOfContact(Double frequencyOfContact) {
        this.frequencyOfContact = frequencyOfContact;
    }

    public Integer getTotalOrderCount() {
        return totalOrderCount;
    }

    public void setTotalOrderCount(Integer totalOrderCount) {
        this.totalOrderCount = totalOrderCount;
    }

    public Double getFrequencyOfCompany() {
        return frequencyOfCompany;
    }

    public void setFrequencyOfCompany(Double frequencyOfCompany) {
        this.frequencyOfCompany = frequencyOfCompany;
    }

    public Date getFirstTradeTime() {
        return firstTradeTime;
    }

    public void setFirstTradeTime(Date firstTradeTime) {
        this.firstTradeTime = firstTradeTime;
    }

    public Date getLastestTradeTime() {
        return lastestTradeTime;
    }

    public void setLastestTradeTime(Date lastestTradeTime) {
        this.lastestTradeTime = lastestTradeTime;
    }
}
