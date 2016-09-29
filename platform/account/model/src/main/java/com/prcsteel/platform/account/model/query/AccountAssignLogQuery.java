package com.prcsteel.platform.account.model.query;


import com.prcsteel.platform.common.query.PagedQuery;

public class AccountAssignLogQuery extends PagedQuery{
    private String accountName;
    private Long orgId;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
