package com.prcsteel.platform.order.model.dto;

/**
 * Created by Rabbit on 2015/10/19.
 */
public class PoolInAndOutModifier {
    Long id;

    Double changeAmount;

    Double changeWeight;

    Double originalAmount;

    Double originalWeight;

    String lastUpdatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public Double getChangeWeight() {
        return changeWeight;
    }

    public void setChangeWeight(Double changeWeight) {
        this.changeWeight = changeWeight;
    }

    public Double getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(Double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public Double getOriginalWeight() {
        return originalWeight;
    }

    public void setOriginalWeight(Double originalWeight) {
        this.originalWeight = originalWeight;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
