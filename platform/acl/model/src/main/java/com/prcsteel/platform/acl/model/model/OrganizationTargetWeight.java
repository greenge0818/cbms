package com.prcsteel.platform.acl.model.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author peanut
 * @version 1.0
 * @description 服务中心目标交易量表对象
 * @date 2016/4/8 10:19
 */
// Created by zhoucai@prcsteel.com 移植风控上线代码 on 2016/5/6.
public class OrganizationTargetWeight {
    /**** 主健id ****/
    private Long id;
    /**** 服务中心id ****/
    private Long orgId;
    /**** 服务中心名称 ****/
    private String orgName;
    /**** 目标交易量 一月份****/
    private BigDecimal targetWeightOne;
    /**** 目标交易量 二月份****/
    private BigDecimal targetWeightTwo;
    /**** 目标交易量 三月份****/
    private BigDecimal targetWeightThree;
    /**** 目标交易量 四月份****/
    private BigDecimal targetWeightFour;
    /**** 目标交易量 五月份****/
    private BigDecimal targetWeightFive;
    /**** 目标交易量 六月份****/
    private BigDecimal targetWeightSix;
    /**** 目标交易量 七月份****/
    private BigDecimal targetWeightSeven;
    /**** 目标交易量 八月份****/
    private BigDecimal targetWeightEight;
    /**** 目标交易量 九月份****/
    private BigDecimal targetWeightNine;
    /**** 目标交易量 十月份****/
    private BigDecimal targetWeightTen;
    /**** 目标交易量 十一月份****/
    private BigDecimal targetWeightEleven;
    /**** 目标交易量 十二月份****/
    private BigDecimal targetWeightTwelve;
    /**** 年份****/
    private String years;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public BigDecimal getTargetWeightOne() {
        return targetWeightOne;
    }

    public void setTargetWeightOne(BigDecimal targetWeightOne) {
        this.targetWeightOne = targetWeightOne;
    }

    public BigDecimal getTargetWeightTwo() {
        return targetWeightTwo;
    }

    public void setTargetWeightTwo(BigDecimal targetWeightTwo) {
        this.targetWeightTwo = targetWeightTwo;
    }

    public BigDecimal getTargetWeightThree() {
        return targetWeightThree;
    }

    public void setTargetWeightThree(BigDecimal targetWeightThree) {
        this.targetWeightThree = targetWeightThree;
    }

    public BigDecimal getTargetWeightFour() {
        return targetWeightFour;
    }

    public void setTargetWeightFour(BigDecimal targetWeightFour) {
        this.targetWeightFour = targetWeightFour;
    }

    public BigDecimal getTargetWeightFive() {
        return targetWeightFive;
    }

    public void setTargetWeightFive(BigDecimal targetWeightFive) {
        this.targetWeightFive = targetWeightFive;
    }

    public BigDecimal getTargetWeightSix() {
        return targetWeightSix;
    }

    public void setTargetWeightSix(BigDecimal targetWeightSix) {
        this.targetWeightSix = targetWeightSix;
    }

    public BigDecimal getTargetWeightSeven() {
        return targetWeightSeven;
    }

    public void setTargetWeightSeven(BigDecimal targetWeightSeven) {
        this.targetWeightSeven = targetWeightSeven;
    }

    public BigDecimal getTargetWeightEight() {
        return targetWeightEight;
    }

    public void setTargetWeightEight(BigDecimal targetWeightEight) {
        this.targetWeightEight = targetWeightEight;
    }

    public BigDecimal getTargetWeightNine() {
        return targetWeightNine;
    }

    public void setTargetWeightNine(BigDecimal targetWeightNine) {
        this.targetWeightNine = targetWeightNine;
    }

    public BigDecimal getTargetWeightTen() {
        return targetWeightTen;
    }

    public void setTargetWeightTen(BigDecimal targetWeightTen) {
        this.targetWeightTen = targetWeightTen;
    }

    public BigDecimal getTargetWeightEleven() {
        return targetWeightEleven;
    }

    public void setTargetWeightEleven(BigDecimal targetWeightEleven) {
        this.targetWeightEleven = targetWeightEleven;
    }

    public BigDecimal getTargetWeightTwelve() {
        return targetWeightTwelve;
    }

    public void setTargetWeightTwelve(BigDecimal targetWeightTwelve) {
        this.targetWeightTwelve = targetWeightTwelve;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
