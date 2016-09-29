package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.InvoiceOutMain;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: InvoiceOutMainDto
 * @Package com.prcsteel.cbms.persist.model
 * @Description:
 * @date 2015/8/4
 */
public class InvoiceOutMainDto extends InvoiceOutMain {
    private Long orgId;
    private String orgName;
    private String createdStartTime;
    private String createdEndTime;
    private String createdStr;
    private String ids;

    public String getCreatedEndTime() {
        return createdEndTime;
    }

    public void setCreatedEndTime(String createdEndTime) {
        this.createdEndTime = createdEndTime;
    }

    public String getCreatedStartTime() {
        return createdStartTime;
    }

    public void setCreatedStartTime(String createdStartTime) {
        this.createdStartTime = createdStartTime;
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

    public String getCreatedStr() {
        return createdStr;
    }

    public void setCreatedStr(String createdStr) {
        this.createdStr = createdStr;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
