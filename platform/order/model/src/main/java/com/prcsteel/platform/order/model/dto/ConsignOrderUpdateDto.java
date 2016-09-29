package com.prcsteel.platform.order.model.dto;

import java.util.List;

import com.prcsteel.platform.order.model.model.ConsignOrder;

/**
 * Created by caochao on 2015/10/9.
 * 用于更新订单
 */
public class ConsignOrderUpdateDto extends ConsignOrder {
    private List<String> oldStatusList;

    private String oldPayStatus;

    public List<String> getOldStatusList() {
        return oldStatusList;
    }

    public void setOldStatusList(List<String> oldStatusList) {
        this.oldStatusList = oldStatusList;
    }

    public String getOldPayStatus() {
        return oldPayStatus;
    }

    public void setOldPayStatus(String oldPayStatus) {
        this.oldPayStatus = oldPayStatus;
    }
}
