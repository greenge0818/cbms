package com.prcsteel.platform.order.service.invoice;

import com.prcsteel.platform.order.model.model.InvoiceOutBalanceMonthly;

/**
 * Created by kongbinheng on 2015/8/2.
 */
public interface InvoiceOutMonthService {

    /**
     * 根据服务中心查询每月销项票余额记录
     * @param orgId
     * @param month
     * @return
     */
    InvoiceOutBalanceMonthly queryByOrgIdAndMonth(Long orgId, String month);
}
