package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: NsortBusinessReportDto
 * @Package com.prcsteel.cbms.persist.dto
 * @Description: 品类交易报表dto
 * @date 2015/8/20
 */
public class NsortBusinessReportDto {
    private String orgName;
    private String nsortName;
    private Integer totalOrder;
    private Integer totalConsignOrder;
    private String consignOrderPercent;
    private BigDecimal totalAmount;
    private BigDecimal totalWeight;
    private BigDecimal avgWeight;
    private Integer totalBuyer;


    private Long orgId;
    private String strStartTime;
    private String strEndTime;
    private List<Long> userIdList;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getNsortName() {
        return nsortName;
    }

    public void setNsortName(String nsortName) {
        this.nsortName = nsortName;
    }

    public Integer getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(Integer totalOrder) {
        this.totalOrder = totalOrder;
    }

    public Integer getTotalConsignOrder() {
        return totalConsignOrder;
    }

    public void setTotalConsignOrder(Integer totalConsignOrder) {
        this.totalConsignOrder = totalConsignOrder;
    }

    public String getConsignOrderPercent() {
        return consignOrderPercent;
    }

    public void setConsignOrderPercent(String consignOrderPercent) {
        this.consignOrderPercent = consignOrderPercent;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public BigDecimal getAvgWeight() {
        return avgWeight;
    }

    public void setAvgWeight(BigDecimal avgWeight) {
        this.avgWeight = avgWeight;
    }

    public Integer getTotalBuyer() {
        return totalBuyer;
    }

    public void setTotalBuyer(Integer totalBuyer) {
        this.totalBuyer = totalBuyer;
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

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }
}
