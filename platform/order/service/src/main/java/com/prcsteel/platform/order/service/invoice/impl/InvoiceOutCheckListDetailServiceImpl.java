package com.prcsteel.platform.order.service.invoice.impl;

import com.prcsteel.platform.order.model.dto.ChecklistDetailDto;
import com.prcsteel.platform.order.model.query.ChecklistDetailQuery;
import com.prcsteel.platform.order.persist.dao.InvoiceOutCheckListDetailDao;
import com.prcsteel.platform.order.service.invoice.InvoiceOutCheckListDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by rolyer on 15-9-16.
 */
@Service("invoiceOutCheckListDetailService")
public class InvoiceOutCheckListDetailServiceImpl implements InvoiceOutCheckListDetailService {
    @Resource
    private InvoiceOutCheckListDetailDao invoiceOutCheckListDetailDao;

    @Override
    public List<ChecklistDetailDto> queryByCondition(ChecklistDetailQuery query) {
        return invoiceOutCheckListDetailDao.queryByCondition(query);
    }

    @Override
    public Integer countByCondition(ChecklistDetailQuery query) {
		return invoiceOutCheckListDetailDao.countByCondition(query);
    }
    
    public ChecklistDetailDto queryByConditionTotal(ChecklistDetailQuery query) {
		return invoiceOutCheckListDetailDao.queryByConditionTotal(query);
    }
}
