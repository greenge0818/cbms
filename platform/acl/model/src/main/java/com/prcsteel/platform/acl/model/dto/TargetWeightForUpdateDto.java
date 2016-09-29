package com.prcsteel.platform.acl.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author peanut
 * @version 1.0
 * @description  服务中心目标交易量更新dto
 * @date 2016/4/11 14:59
 */
// Created by zhoucai@prcsteel.com 移植风控上线代码 on 2016/5/6.
public class TargetWeightForUpdateDto implements Serializable {

    /***服务中心Id****/
    private Long orgId;
    /***服务中心名称****/
    private String orgName;
    /***年份***/
    private String years;
    /***月份***/
    private String month;
    /***重量***/
    private BigDecimal weight;

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

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
