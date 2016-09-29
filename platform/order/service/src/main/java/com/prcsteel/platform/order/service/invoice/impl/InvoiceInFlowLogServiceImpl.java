package com.prcsteel.platform.order.service.invoice.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.order.model.dto.InvoiceInFlowDto;
import com.prcsteel.platform.order.persist.dao.InvoiceInFlowLogDao;
import com.prcsteel.platform.order.service.invoice.InvoiceInFlowLogService;

/**
 * Created by lx on 2016/02/19.
 */
@Service("invoiceInFlowLogService")
@Transactional
public class InvoiceInFlowLogServiceImpl implements InvoiceInFlowLogService {
	
	@Resource
	InvoiceInFlowLogDao invoiceInFlowLogDao;
	
	@Override
	public List<InvoiceInFlowDto> queryInvoiceInlogById(Long invoiceId) {
		return invoiceInFlowLogDao.queryInvoiceInlogById(invoiceId);
	}

}
