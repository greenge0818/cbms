package com.prcsteel.platform.kuandao.web.job;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prcsteel.platform.kuandao.service.KuandaoPaymentOrderService;

public class SubmitPaymentOrderJob extends CbmsJob {

	private static final Logger logger = LoggerFactory.getLogger(SubmitPaymentOrderJob.class);
	
	@Resource
	private KuandaoPaymentOrderService kuandaoPaymentOrderService;
	
	@Override
	public void execute() {
		logger.debug("SubmitPaymentOrderJob begin");
		kuandaoPaymentOrderService.processMclsUnmatch("system");
		logger.debug("SubmitPaymentOrderJob end");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
