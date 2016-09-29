package com.prcsteel.platform.order.service.invoice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.model.InvoiceOutBalanceMonthly;
import com.prcsteel.platform.order.persist.dao.InvoiceOutBalanceMonthlyDao;
import com.prcsteel.platform.order.service.invoice.InvoiceOutMonthService;

/**
 * Created by kongbinheng on 2015/8/2.
 */
@Service("invoiceOutMonthService")
public class InvoiceOutMonthServiceImpl implements InvoiceOutMonthService {

    @Autowired
    InvoiceOutBalanceMonthlyDao InvoiceOutMonthDao;

    /**
     * 根据服务中心查询每月销项票余额记录
     * @param orgId
     * @param month
     * @return
     */
    @Override
    public InvoiceOutBalanceMonthly queryByOrgIdAndMonth(Long orgId, String month) {
        return InvoiceOutMonthDao.queryByOrgIdAndMonth(orgId, month);
    }
}
