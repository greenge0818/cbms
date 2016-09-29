package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by caochao on 2015/9/2.
 */
public class OrganizationTradeStatisticsDto extends TradeStatisticsDto {
    private Long orgId;                                     //组织ID
    private String orgName;                                 //组织名称
    private BigDecimal creditLimit;                         //备用金额
    private BigDecimal creditLimitUsed;                     //备用金额已使用
    private Integer traderCount;                            //交易员人数

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

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditLimitUsed() {
        return creditLimitUsed;
    }

    public void setCreditLimitUsed(BigDecimal creditLimitUsed) {
        this.creditLimitUsed = creditLimitUsed;
    }

    public Integer getTraderCount() {
        return traderCount;
    }

    public void setTraderCount(Integer traderCount) {
        this.traderCount = traderCount;
    }

}
