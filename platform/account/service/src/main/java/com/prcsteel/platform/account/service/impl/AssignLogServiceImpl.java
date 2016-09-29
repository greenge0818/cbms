package com.prcsteel.platform.account.service.impl;


import com.prcsteel.platform.account.model.model.AssignLog;
import com.prcsteel.platform.account.persist.dao.AssignLogDao;
import com.prcsteel.platform.account.service.AssignLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caochao on 2015/7/14.
 */
@Service("assignLogService")
public class AssignLogServiceImpl implements AssignLogService {
    @Autowired
    private AssignLogDao assignLogDao;
    public List<AssignLog> query(Long accountId, int start, int length) {
        return assignLogDao.query(accountId, start, length);
    }


    public int count(Long accountId) {
        return assignLogDao.count(accountId);
    }
}
