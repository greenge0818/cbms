package com.prcsteel.platform.smartmatch.model.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存监控信息
 * Created by Rolyer on 2015/11/28.
 */
public class ReportResourceInventoryDto {
    /**
     * 品种UUID
     */
    private String uuid;
    /**
     * 品种
     */
    private String name;
    /**
     * 库存资源卖家条数总计
     */
    private int totalAccount;
    /**
     * 库存资源条数总计
     */
    private int totalResource;
    /**
     * 总重量
     */
    private BigDecimal totalWeight;
    /**
     * 品规覆盖率
     */
    private BigDecimal totalSpecCoverageRate;
    /**
     * 成交覆盖率
     */
    private BigDecimal totalTransactionCoverageRate;
    /**
     * 价格偏差条数
     */
    private int totalDeviation;
    /**
     * 价格偏率
     */
    private BigDecimal totalDeviationRate;
    /**
     * 品种
     */
    private List<CategoryGroupDto>  categoryGroup;

    /**
     * 构造函数
     * @param totalAccount
     * @param totalResource
     * @param totalWeight
     * @param totalSpecCoverageRate
     * @param totalTransactionCoverageRate
     * @param totalDeviation
     * @param totalDeviationRate
     * @param categoryGroup
     */
    public ReportResourceInventoryDto(int totalAccount, int totalResource, BigDecimal totalWeight, BigDecimal totalSpecCoverageRate, BigDecimal totalTransactionCoverageRate, int totalDeviation, BigDecimal totalDeviationRate, List<CategoryGroupDto> categoryGroup) {
        this.totalAccount = totalAccount;
        this.totalResource = totalResource;
        this.totalWeight = totalWeight;
        this.totalSpecCoverageRate = totalSpecCoverageRate;
        this.totalTransactionCoverageRate = totalTransactionCoverageRate;
        this.totalDeviation = totalDeviation;
        this.totalDeviationRate = totalDeviationRate;
        this.categoryGroup = categoryGroup;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getTotalAccount() {
		return totalAccount;
	}

	public void setTotalAccount(int totalAccount) {
		this.totalAccount = totalAccount;
	}

	public int getTotalResource() {
        return totalResource;
    }

    public void setTotalResource(int totalResource) {
        this.totalResource = totalResource;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public BigDecimal getTotalSpecCoverageRate() {
        return totalSpecCoverageRate;
    }

    public void setTotalSpecCoverageRate(BigDecimal totalSpecCoverageRate) {
        this.totalSpecCoverageRate = totalSpecCoverageRate;
    }

    public BigDecimal getTotalTransactionCoverageRate() {
        return totalTransactionCoverageRate;
    }

    public void setTotalTransactionCoverageRate(BigDecimal totalTransactionCoverageRate) {
        this.totalTransactionCoverageRate = totalTransactionCoverageRate;
    }

    public int getTotalDeviation() {
        return totalDeviation;
    }

    public void setTotalDeviation(int totalDeviation) {
        this.totalDeviation = totalDeviation;
    }

    public BigDecimal getTotalDeviationRate() {
        return totalDeviationRate;
    }

    public void setTotalDeviationRate(BigDecimal totalDeviationRate) {
        this.totalDeviationRate = totalDeviationRate;
    }

    public List<CategoryGroupDto> getCategoryGroup() {
        return categoryGroup;
    }

    public void setCategoryGroup(List<CategoryGroupDto> categoryGroup) {
        this.categoryGroup = categoryGroup;
    }
}
