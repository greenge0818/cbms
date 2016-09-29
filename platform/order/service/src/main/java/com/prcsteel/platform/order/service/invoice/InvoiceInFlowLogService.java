package com.prcsteel.platform.order.service.invoice;

import java.util.List;

import com.prcsteel.platform.order.model.dto.InvoiceInFlowDto;

/**
 * 
 * @author lx 2016/02/19
 *
 */
public interface InvoiceInFlowLogService {
	
	List<InvoiceInFlowDto> queryInvoiceInlogById(Long invoiceId);
}
