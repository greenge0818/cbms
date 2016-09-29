package com.prcsteel.platform.order.web.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.prcsteel.platform.order.service.invoice.InvoiceOutCheckListService;

/**
 * 销项清单二结欠款状态更新
 * 这个状态：主要于用留存销项票的二结欠款
 * @author tuxianming
 * @date 20160627
 */
public class UpdateDebtSecondSettlementJob extends CbmsJob{
	
	private static final Logger logger = Logger.getLogger(LocalTransactionDataJob.class);
	
	@Resource
    private InvoiceOutCheckListService invoiceOutCheckListService;

	@Override
	public void execute() {
		logger.info("UpdateDebtSecondSettlementJob job start");
		try {
			invoiceOutCheckListService.updateDebtSecondSettlement();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
