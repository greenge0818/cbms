package com.prcsteel.platform.order.service;

import java.util.List;

import com.prcsteel.platform.acl.model.model.PrintLog;

/**
 * Created by rolyer on 15-7-31.
 */
public interface PrintLogService {

    public int insert(PrintLog record);

    public List<PrintLog> query(String billCode, String billType, Long userId);

    public List<PrintLog> query(String billCode, String billType);
}
