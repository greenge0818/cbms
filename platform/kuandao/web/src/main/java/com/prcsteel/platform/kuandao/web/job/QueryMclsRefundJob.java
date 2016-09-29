package com.prcsteel.platform.kuandao.web.job;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prcsteel.platform.kuandao.service.KuandaoPaymentOrderService;

public class QueryMclsRefundJob extends CbmsJob {

private static final Logger logger = LoggerFactory.getLogger(QueryMclsRefundJob.class);
	
	@Resource
	private KuandaoPaymentOrderService kuandaoPaymentOrderService;
	
	@Override
	public void execute() {
		logger.debug("QueryMclsRefundJob begin");
		kuandaoPaymentOrderService.processMclsRefund("system");
		logger.debug("QueryMclsRefundJob end");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
