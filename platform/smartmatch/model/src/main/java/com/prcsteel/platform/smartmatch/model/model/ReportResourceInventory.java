package com.prcsteel.platform.smartmatch.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class ReportResourceInventory {
    private Long id;

    private Long areaId;

    private String categoryGroupUuid;

    private String categoryGroupName;

    private String categoryUuid;

    private String categoryName;

    private Integer totalResource;
    
    private Integer totalAccount;

    private Integer totalSpec;

    private Integer totalStockSpec;

    private BigDecimal specCoverageRate;

    private Integer totalTransaction;

    private BigDecimal transactionCoverageRate;

    private Integer totalDeviation;

    private BigDecimal deviationRate;

    private BigDecimal weight;

    private Date created;

    private String createdBy;

    public ReportResourceInventory() {
    }

    public ReportResourceInventory(Long id, Long areaId, String categoryGroupUuid, String categoryGroupName, String categoryUuid, String categoryName, Integer totalResource, Integer totalSpec, Integer totalStockSpec, BigDecimal specCoverageRate, Integer totalTransaction, BigDecimal transactionCoverageRate, Integer totalDeviation, BigDecimal deviationRate, BigDecimal weight, Date created, String createdBy) {
        this.id = id;
        this.areaId = areaId;
        this.categoryGroupUuid = categoryGroupUuid;
        this.categoryGroupName = categoryGroupName;
        this.categoryUuid = categoryUuid;
        this.categoryName = categoryName;
        this.totalResource = totalResource;
        this.totalSpec = totalSpec;
        this.totalStockSpec = totalStockSpec;
        this.specCoverageRate = specCoverageRate;
        this.totalTransaction = totalTransaction;
        this.transactionCoverageRate = transactionCoverageRate;
        this.totalDeviation = totalDeviation;
        this.deviationRate = deviationRate;
        this.weight = weight;
        this.created = created;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getCategoryGroupUuid() {
        return categoryGroupUuid;
    }

    public void setCategoryGroupUuid(String categoryGroupUuid) {
        this.categoryGroupUuid = categoryGroupUuid;
    }

    public String getCategoryGroupName() {
        return categoryGroupName;
    }

    public void setCategoryGroupName(String categoryGroupName) {
        this.categoryGroupName = categoryGroupName;
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getTotalResource() {
        return totalResource;
    }

    public void setTotalResource(Integer totalResource) {
        this.totalResource = totalResource;
    }
    
    public Integer getTotalAccount() {
		return totalAccount;
	}

	public void setTotalAccount(Integer totalAccount) {
		this.totalAccount = totalAccount;
	}

	public Integer getTotalSpec() {
        return totalSpec;
    }

    public void setTotalSpec(Integer totalSpec) {
        this.totalSpec = totalSpec;
    }

    public Integer getTotalStockSpec() {
        return totalStockSpec;
    }

    public void setTotalStockSpec(Integer totalStockSpec) {
        this.totalStockSpec = totalStockSpec;
    }

    public BigDecimal getSpecCoverageRate() {
        return specCoverageRate;
    }

    public void setSpecCoverageRate(BigDecimal specCoverageRate) {
        this.specCoverageRate = specCoverageRate;
    }

    public Integer getTotalTransaction() {
        return totalTransaction;
    }

    public void setTotalTransaction(Integer totalTransaction) {
        this.totalTransaction = totalTransaction;
    }

    public BigDecimal getTransactionCoverageRate() {
        return transactionCoverageRate;
    }

    public void setTransactionCoverageRate(BigDecimal transactionCoverageRate) {
        this.transactionCoverageRate = transactionCoverageRate;
    }

    public Integer getTotalDeviation() {
        return totalDeviation;
    }

    public void setTotalDeviation(Integer totalDeviation) {
        this.totalDeviation = totalDeviation;
    }

    public BigDecimal getDeviationRate() {
        return deviationRate;
    }

    public void setDeviationRate(BigDecimal deviationRate) {
        this.deviationRate = deviationRate;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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
}