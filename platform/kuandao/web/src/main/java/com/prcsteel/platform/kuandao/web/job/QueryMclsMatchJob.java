package com.prcsteel.platform.kuandao.web.job;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prcsteel.platform.kuandao.service.KuandaoPaymentOrderService;

public class QueryMclsMatchJob extends CbmsJob {

	private static final Logger logger = LoggerFactory.getLogger(QueryMclsMatchJob.class);
	
	@Resource
	private KuandaoPaymentOrderService kuandaoPaymentOrderService;
	
	@Override
	public void execute() {
		logger.debug("QueryMclsMatchJob begin");
		kuandaoPaymentOrderService.processMclsMatch("system");
		logger.debug("QueryMclsMatchJob end");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
