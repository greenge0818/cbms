package com.prcsteel.platform.smartmatch.model.dto;

/**
 * Created by caochao on 2016/8/24.
 */
public class BasePriceCustSubscribeInfo {
    private Integer subscribed; //是否已订阅
    private Long accountId;
    private Long departmentId;
    private String accountName;

    public Integer getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Integer subscribed) {
        this.subscribed = subscribed;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
