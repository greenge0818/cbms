package com.prcsteel.platform.order.service.invoice.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.order.model.model.InvoiceInProcess;
import com.prcsteel.platform.order.persist.dao.InvoiceInProcessDao;
import com.prcsteel.platform.order.service.invoice.InvoiceInProcessService;

/**
 * Created by lx on 2016/02/19.
 */
@Service("invoiceInProcessService")
@Transactional
public class InvoiceInProcessServiceImpl implements InvoiceInProcessService {
	
	@Resource
	InvoiceInProcessDao invoiceInProcessDao;
	
	@Override
	public List<InvoiceInProcess> queryByOperatorId(Long userId) {
		return invoiceInProcessDao.queryByOperatorId(userId);
	}

}
