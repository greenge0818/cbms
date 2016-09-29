package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by caochao on 2015/8/28.
 */
public class TradeStatisticsDto {
    private Integer orderCount = 0;                             //交易总笔数
    private Integer consignOrderCount = 0;                      //代运营交易订单数
    private Float consignOrderPercent = 0f;                      //代运营交易订单占比
    private BigDecimal orderTotalAmount = BigDecimal.ZERO;                    //交易总金额
    private BigDecimal orderTotalWeight = BigDecimal.ZERO;                    //交易总重量
    private Float orderAvgWeight = 0f;                           //平均每笔交易重量
    private Integer buyerCount = 0;                             //交易买家数
    private Integer frequentTradeCurMonthCount = 0;             //当月三次以上采购买家
    private Integer frequentTradePrevMonthCount = 0;            //上月三次以上采购买家
    private Integer buyerInCreaseCount = 0;                     //新增买家
    private Integer sellerInCreaseCount = 0;                    //新增卖家
    private Integer tradeSellerCount = 0;                       //代运营交易卖家数
    private Integer sellerTradeCount = 0;                       //代运营卖家交易笔数
    private BigDecimal sellerTradeTotalWeight = BigDecimal.ZERO;              //代运营卖家交易重量
    private BigDecimal sellerTradeTotalAmount = BigDecimal.ZERO; //代运营订单卖家交易金额
    private Integer tempTradeSellerCount = 0;                   //非代运营交易卖家数
    private Integer tempSellerTradeCount = 0;                   //非代运营卖家交易笔数
    private BigDecimal tempSellerTradeTotalWeight = BigDecimal.ZERO;          //非代运营卖家交易重量
    private BigDecimal tempSellerTradeTotalAmount = BigDecimal.ZERO; //非代运营订单卖家交易金额

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getConsignOrderCount() {
        return consignOrderCount;
    }

    public void setConsignOrderCount(Integer consignOrderCount) {
        this.consignOrderCount = consignOrderCount;
    }

    public Float getConsignOrderPercent() {
        if (consignOrderCount != null && orderCount != null && orderCount.equals(0)) {
            consignOrderPercent = 0f;
        } else {
            consignOrderPercent = consignOrderCount * 1f / orderCount;
        }
        return consignOrderPercent;
    }

    public void setConsignOrderPercent(Float consignOrderPercent) {
        this.consignOrderPercent = consignOrderPercent;
    }

    public BigDecimal getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(BigDecimal orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public BigDecimal getOrderTotalWeight() {
        return orderTotalWeight;
    }

    public void setOrderTotalWeight(BigDecimal orderTotalWeight) {
        this.orderTotalWeight = orderTotalWeight;
    }

    public Float getOrderAvgWeight() {
        if (orderTotalWeight != null && orderCount != null && orderCount.equals(0)) {
            orderAvgWeight = 0f;
        } else {
            orderAvgWeight = orderTotalWeight.floatValue() / orderCount;
        }
        return orderAvgWeight;
    }

    public void setOrderAvgWeight(Float orderAvgWeight) {
        this.orderAvgWeight = orderAvgWeight;
    }

    public Integer getBuyerCount() {
        return buyerCount;
    }

    public void setBuyerCount(Integer buyerCount) {
        this.buyerCount = buyerCount;
    }

    public Integer getFrequentTradeCurMonthCount() {
        return frequentTradeCurMonthCount;
    }

    public void setFrequentTradeCurMonthCount(Integer frequentTradeCurMonthCount) {
        this.frequentTradeCurMonthCount = frequentTradeCurMonthCount;
    }

    public Integer getFrequentTradePrevMonthCount() {
        return frequentTradePrevMonthCount;
    }

    public void setFrequentTradePrevMonthCount(Integer frequentTradePrevMonthCount) {
        this.frequentTradePrevMonthCount = frequentTradePrevMonthCount;
    }

    public Integer getBuyerInCreaseCount() {
        return buyerInCreaseCount;
    }

    public void setBuyerInCreaseCount(Integer buyerInCreaseCount) {
        this.buyerInCreaseCount = buyerInCreaseCount;
    }

    public Integer getSellerInCreaseCount() {
        return sellerInCreaseCount;
    }

    public void setSellerInCreaseCount(Integer sellerInCreaseCount) {
        this.sellerInCreaseCount = sellerInCreaseCount;
    }

    public Integer getTradeSellerCount() {
        return tradeSellerCount;
    }

    public void setTradeSellerCount(Integer tradeSellerCount) {
        this.tradeSellerCount = tradeSellerCount;
    }

    public Integer getSellerTradeCount() {
        return sellerTradeCount;
    }

    public void setSellerTradeCount(Integer sellerTradeCount) {
        this.sellerTradeCount = sellerTradeCount;
    }

    public BigDecimal getSellerTradeTotalWeight() {
        return sellerTradeTotalWeight;
    }

    public void setSellerTradeTotalWeight(BigDecimal sellerTradeTotalWeight) {
        this.sellerTradeTotalWeight = sellerTradeTotalWeight;
    }

    public Integer getTempTradeSellerCount() {
        return tempTradeSellerCount;
    }

    public void setTempTradeSellerCount(Integer tempTradeSellerCount) {
        this.tempTradeSellerCount = tempTradeSellerCount;
    }

    public Integer getTempSellerTradeCount() {
        return tempSellerTradeCount;
    }

    public void setTempSellerTradeCount(Integer tempSellerTradeCount) {
        this.tempSellerTradeCount = tempSellerTradeCount;
    }

    public BigDecimal getTempSellerTradeTotalWeight() {
        return tempSellerTradeTotalWeight;
    }

    public void setTempSellerTradeTotalWeight(BigDecimal tempSellerTradeTotalWeight) {
        this.tempSellerTradeTotalWeight = tempSellerTradeTotalWeight;
    }

    public BigDecimal getSellerTradeTotalAmount() {
        return sellerTradeTotalAmount;
    }

    public void setSellerTradeTotalAmount(BigDecimal sellerTradeTotalAmount) {
        this.sellerTradeTotalAmount = sellerTradeTotalAmount;
    }

    public BigDecimal getTempSellerTradeTotalAmount() {
        return tempSellerTradeTotalAmount;
    }

    public void setTempSellerTradeTotalAmount(BigDecimal tempSellerTradeTotalAmount) {
        this.tempSellerTradeTotalAmount = tempSellerTradeTotalAmount;
    }
}
