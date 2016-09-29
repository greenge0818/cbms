package com.prcsteel.platform.account.service;

import java.util.List;

import com.prcsteel.platform.account.model.model.AssignLog;

/**
 * Created by caochao on 2015/7/14.
 */
public interface AssignLogService {
    public List<AssignLog> query(Long accountId, int start, int length);
    public int count(Long accountId);
}
