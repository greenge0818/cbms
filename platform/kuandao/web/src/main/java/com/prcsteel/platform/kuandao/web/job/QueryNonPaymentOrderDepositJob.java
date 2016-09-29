package com.prcsteel.platform.kuandao.web.job;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prcsteel.platform.kuandao.service.KuandaoPaymentOrderService;

public class QueryNonPaymentOrderDepositJob extends CbmsJob {

	private static final Logger logger = LoggerFactory.getLogger(QueryNonPaymentOrderDepositJob.class);
	
	@Resource
	private KuandaoPaymentOrderService kuandaoPaymentOrderService;
	
	@Override
	public void execute() {
		logger.debug("QueryNonPaymentOrderDepositJob begin");
		kuandaoPaymentOrderService.processNonPaymentOrderDeposit();
		logger.debug("QueryNonPaymentOrderDepositJob end");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
