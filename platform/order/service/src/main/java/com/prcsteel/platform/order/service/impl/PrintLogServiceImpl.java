package com.prcsteel.platform.order.service.impl;

import java.util.List;

import com.prcsteel.platform.order.service.PrintLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.acl.model.model.PrintLog;
import com.prcsteel.platform.acl.persist.dao.PrintLogDao;

/**
 * Created by rolyer on 15-7-31.
 */
@Service("printLogService")
public class PrintLogServiceImpl implements PrintLogService {

    @Autowired
    private PrintLogDao printLogDao;

    public int insert(PrintLog printLog) {

        printLog.setLastUpdatedBy(printLog.getCreatedBy());

        return printLogDao.insert(printLog);
    }

    public List<PrintLog> query(String billCode, String billType, Long userId) {
        return printLogDao.query(billCode, billType, userId);
    }

    public List<PrintLog> query(String billCode, String billType) {
        return printLogDao.query(billCode, billType, null);
    }
}
