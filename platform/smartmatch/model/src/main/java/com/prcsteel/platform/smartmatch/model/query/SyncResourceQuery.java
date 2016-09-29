package com.prcsteel.platform.smartmatch.model.query;

import java.util.Date;
import java.util.List;

/**
 * Created by caochao on 2016/9/20.
 */
public class SyncResourceQuery {
    private List<Long> basepriceIds;
    private String operator;

    public List<Long> getBasepriceIds() {
        return basepriceIds;
    }

    public void setBasepriceIds(List<Long> basepriceIds) {
        this.basepriceIds = basepriceIds;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
