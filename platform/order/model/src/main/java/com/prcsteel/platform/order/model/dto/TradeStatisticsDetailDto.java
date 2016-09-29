package com.prcsteel.platform.order.model.dto;

/**
 * Created by caochao on 2015/9/2.
 */
public class TradeStatisticsDetailDto extends TradeStatisticsDto {
    private Long userId;
    private String userName;
    private Long orgId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
