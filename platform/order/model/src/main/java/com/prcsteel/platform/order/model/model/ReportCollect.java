package com.prcsteel.platform.order.model.model;


import java.math.BigDecimal;

/**
 * 服务中心提成统计报表  sheet2
 * wangxiao
 * Created by prcsteel on 2016/1/18
 */
public class ReportCollect {

    private String  buyerOrgName;//买家服务中心

    private String buyerJobNumber;//买家交易员工号

    private String buyerOwnerName;//买家交易员,

    private String sellerOrgName;//卖家服务中心

    private String sellerJobNumber; //卖家交易员工号;

    private String sellerOwnerName;//卖家交易员,

    private BigDecimal buyerCount;//买家交易提成

    private BigDecimal sellerCount;//新增客户数提成金额

    private String buyerShop; //

    private BigDecimal buyerSum;//提成总额

    private BigDecimal sellerSum;

    public BigDecimal getBuyerCount() {
        return buyerCount;
    }

    public void setBuyerCount(BigDecimal buyerCount) {
        this.buyerCount = buyerCount;
    }

    public BigDecimal getSellerCount() {
        return sellerCount;
    }

    public void setSellerCount(BigDecimal sellerCount) {
        this.sellerCount = sellerCount;
    }

    public BigDecimal getBuyerSum() {
        return buyerSum;
    }

    public void setBuyerSum(BigDecimal buyerSum) {
        this.buyerSum = buyerSum;
    }

    public BigDecimal getSellerSum() {
        return sellerSum;
    }

    public void setSellerSum(BigDecimal sellerSum) {
        this.sellerSum = sellerSum;
    }

    public String getBuyerOrgName() {
        return buyerOrgName;
    }

    public void setBuyerOrgName(String buyerOrgName) {
        this.buyerOrgName = buyerOrgName;
    }

    public String getBuyerJobNumber() {
        return buyerJobNumber;
    }

    public void setBuyerJobNumber(String buyerJobNumber) {
        this.buyerJobNumber = buyerJobNumber;
    }

    public String getBuyerOwnerName() {
        return buyerOwnerName;
    }

    public void setBuyerOwnerName(String buyerOwnerName) {
        this.buyerOwnerName = buyerOwnerName;
    }

    public String getSellerOrgName() {
        return sellerOrgName;
    }

    public void setSellerOrgName(String sellerOrgName) {
        this.sellerOrgName = sellerOrgName;
    }

    public String getSellerJobNumber() {
        return sellerJobNumber;
    }

    public void setSellerJobNumber(String sellerJobNumber) {
        this.sellerJobNumber = sellerJobNumber;
    }

    public String getSellerOwnerName() {
        return sellerOwnerName;
    }

    public void setSellerOwnerName(String sellerOwnerName) {
        this.sellerOwnerName = sellerOwnerName;
    }


    public String getBuyerShop() {
        return buyerShop;
    }

    public void setBuyerShop(String buyerShop) {
        this.buyerShop = buyerShop;
    }
}
