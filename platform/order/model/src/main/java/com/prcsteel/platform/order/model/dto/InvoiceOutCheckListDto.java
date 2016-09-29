package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.InvoiceOutCheckList;

public class InvoiceOutCheckListDto extends InvoiceOutCheckList {

    private String orgName;
    private String statusName;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}