package com.prcsteel.platform.order.service.invoice;

import java.util.List;

import com.prcsteel.platform.order.model.model.InvoiceInProcess;

/**
 * Created by lx on 2016/02/19.
 */

public interface InvoiceInProcessService {
	
	List<InvoiceInProcess> queryByOperatorId(Long userId);
}
