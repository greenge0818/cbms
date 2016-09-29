package com.prcsteel.platform.order.model.changecontract.dto;

import java.util.List;

/**
 * 修改合同变更Dto
 * Created by lichaowei on 2016/8/25.
 */
public class UpdateChangeOrderDto {

    private Integer changeOrderId;  // 合同变更Id
    private String alterStatus;     // 需要修改的状态
    private String oldStatus;       // 原状态
    private String lastUpdatedBy;   // 更新人
    private List<Integer> itemChangeIds; // 合同变更详情Id集合

    public Integer getChangeOrderId() {
        return changeOrderId;
    }

    public void setChangeOrderId(Integer changeOrderId) {
        this.changeOrderId = changeOrderId;
    }

    public String getAlterStatus() {
        return alterStatus;
    }

    public void setAlterStatus(String alterStatus) {
        this.alterStatus = alterStatus;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public List<Integer> getItemChangeIds() {
        return itemChangeIds;
    }

    public void setItemChangeIds(List<Integer> itemChangeIds) {
        this.itemChangeIds = itemChangeIds;
    }
}
