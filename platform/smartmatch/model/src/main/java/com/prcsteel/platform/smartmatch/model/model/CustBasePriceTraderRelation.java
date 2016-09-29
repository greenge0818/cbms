package com.prcsteel.platform.smartmatch.model.model;

import java.util.Date;

public class CustBasePriceTraderRelation {
    private Long subscriberId;

    private Long traderId;

    private Date created;

    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public Long getTraderId() {
        return traderId;
    }

    public void setTraderId(Long traderId) {
        this.traderId = traderId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}