package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.Allowance;

public class AllowanceForUpdateDto extends Allowance{
    private String oldStatus;

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }
}
