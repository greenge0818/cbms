package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: SellerOrderBusinessReportDto
 * @Package com.prcsteel.cbms.persist.dto
 * @Description: 代运营卖家交易报表
 * @date 2015/8/19
 */
public class SellerOrderBusinessReportDto {
    private String sellerName;
    private String consignType;
    private Integer monthOrderCount;
    private BigDecimal monthWeight;
    private BigDecimal monthAllSellerWeight;
    private BigDecimal monthAmount;
    private BigDecimal sumWeight;


    private Long orgId;
    private String strStartTime;
    private String strEndTime;
    private Long userId;
    private Integer start;
    private Integer length;
    private List<Long> userIdList;

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getConsignType() {
        return consignType;
    }

    public void setConsignType(String consignType) {
        this.consignType = consignType;
    }

    public Integer getMonthOrderCount() {
        return monthOrderCount;
    }

    public void setMonthOrderCount(Integer monthOrderCount) {
        this.monthOrderCount = monthOrderCount;
    }

    public BigDecimal getMonthWeight() {
        return monthWeight;
    }

    public void setMonthWeight(BigDecimal monthWeight) {
        this.monthWeight = monthWeight;
    }

    public BigDecimal getMonthAllSellerWeight() {
        return monthAllSellerWeight;
    }

    public void setMonthAllSellerWeight(BigDecimal monthAllSellerWeight) {
        this.monthAllSellerWeight = monthAllSellerWeight;
    }

    public BigDecimal getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(BigDecimal monthAmount) {
        this.monthAmount = monthAmount;
    }

    public BigDecimal getSumWeight() {
        return sumWeight;
    }

    public void setSumWeight(BigDecimal sumWeight) {
        this.sumWeight = sumWeight;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getStrStartTime() {
        return strStartTime;
    }

    public void setStrStartTime(String strStartTime) {
        this.strStartTime = strStartTime;
    }

    public String getStrEndTime() {
        return strEndTime;
    }

    public void setStrEndTime(String strEndTime) {
        this.strEndTime = strEndTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }
}


