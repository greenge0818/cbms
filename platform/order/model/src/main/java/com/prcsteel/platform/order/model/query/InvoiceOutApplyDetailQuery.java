package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * Created by rolyer on 15-9-14.
 */
public class InvoiceOutApplyDetailQuery extends PagedQuery {
    private Long orgId;
    private String status;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
