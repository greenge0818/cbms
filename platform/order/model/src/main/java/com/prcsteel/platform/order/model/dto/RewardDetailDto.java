package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by dengxiyan on 2015/9/18.
 * 提成报表：提成详细信息
 */
public class RewardDetailDto {

    private Long orgId;
    private String orgName;
    private Long managerId;
    private String managerName;
    private String groupName;
    private BigDecimal currOrgBuyerTempWeight;
    private BigDecimal currOrgBuyerConsignWeight;
    private BigDecimal otherOrgBuyerTempWeight;
    private BigDecimal otherOrgBuyerConsignWeight;
    private BigDecimal currOrgSellerTempWeight;
    private BigDecimal currOrgSellerConsignWeight;
    private BigDecimal otherOrgSellerTempWeight;
    private BigDecimal otherOrgSellerConsignWeight;

    private BigDecimal buyerRewardAmount;
    private BigDecimal sellerRewardAmount;

    private String groupBy;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public BigDecimal getCurrOrgBuyerTempWeight() {
        return currOrgBuyerTempWeight;
    }

    public void setCurrOrgBuyerTempWeight(BigDecimal currOrgBuyerTempWeight) {
        this.currOrgBuyerTempWeight = currOrgBuyerTempWeight;
    }

    public BigDecimal getCurrOrgBuyerConsignWeight() {
        return currOrgBuyerConsignWeight;
    }

    public void setCurrOrgBuyerConsignWeight(BigDecimal currOrgBuyerConsignWeight) {
        this.currOrgBuyerConsignWeight = currOrgBuyerConsignWeight;
    }

    public BigDecimal getOtherOrgBuyerTempWeight() {
        return otherOrgBuyerTempWeight;
    }

    public void setOtherOrgBuyerTempWeight(BigDecimal otherOrgBuyerTempWeight) {
        this.otherOrgBuyerTempWeight = otherOrgBuyerTempWeight;
    }

    public BigDecimal getOtherOrgBuyerConsignWeight() {
        return otherOrgBuyerConsignWeight;
    }

    public void setOtherOrgBuyerConsignWeight(BigDecimal otherOrgBuyerConsignWeight) {
        this.otherOrgBuyerConsignWeight = otherOrgBuyerConsignWeight;
    }

    public BigDecimal getCurrOrgSellerTempWeight() {
        return currOrgSellerTempWeight;
    }

    public void setCurrOrgSellerTempWeight(BigDecimal currOrgSellerTempWeight) {
        this.currOrgSellerTempWeight = currOrgSellerTempWeight;
    }

    public BigDecimal getCurrOrgSellerConsignWeight() {
        return currOrgSellerConsignWeight;
    }

    public void setCurrOrgSellerConsignWeight(BigDecimal currOrgSellerConsignWeight) {
        this.currOrgSellerConsignWeight = currOrgSellerConsignWeight;
    }

    public BigDecimal getOtherOrgSellerTempWeight() {
        return otherOrgSellerTempWeight;
    }

    public void setOtherOrgSellerTempWeight(BigDecimal otherOrgSellerTempWeight) {
        this.otherOrgSellerTempWeight = otherOrgSellerTempWeight;
    }

    public BigDecimal getOtherOrgSellerConsignWeight() {
        return otherOrgSellerConsignWeight;
    }

    public void setOtherOrgSellerConsignWeight(BigDecimal otherOrgSellerConsignWeight) {
        this.otherOrgSellerConsignWeight = otherOrgSellerConsignWeight;
    }

    public BigDecimal getBuyerRewardAmount() {
        return buyerRewardAmount;
    }

    public void setBuyerRewardAmount(BigDecimal buyerRewardAmount) {
        this.buyerRewardAmount = buyerRewardAmount;
    }

    public BigDecimal getSellerRewardAmount() {
        return sellerRewardAmount;
    }

    public void setSellerRewardAmount(BigDecimal sellerRewardAmount) {
        this.sellerRewardAmount = sellerRewardAmount;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }
}
