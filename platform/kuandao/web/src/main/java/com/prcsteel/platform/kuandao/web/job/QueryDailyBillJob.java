package com.prcsteel.platform.kuandao.web.job;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prcsteel.platform.kuandao.service.KuandaoDailyBillService;

public class QueryDailyBillJob extends CbmsJob {

private static final Logger logger = LoggerFactory.getLogger(QueryDailyBillJob.class);
	
	@Resource
	private KuandaoDailyBillService kuandaoDailyBillService;
	
	@Override
	public void execute() {
		logger.debug("QueryMclsRefundJob begin");
		kuandaoDailyBillService.insertDailyBill("system");
		logger.debug("QueryMclsRefundJob end");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
