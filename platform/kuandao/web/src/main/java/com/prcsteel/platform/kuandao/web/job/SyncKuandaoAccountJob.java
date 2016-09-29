package com.prcsteel.platform.kuandao.web.job;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prcsteel.platform.kuandao.service.KuandaoAccountService;

public class SyncKuandaoAccountJob extends CbmsJob {

	private static final Logger logger = LoggerFactory.getLogger(SyncKuandaoAccountJob.class);
	
	@Resource
	private KuandaoAccountService kuandaoAccountService;
	
	@Override
	public void execute() {
		logger.debug("SyncKuandaoAccountJob begin");
		kuandaoAccountService.synchronizeAccountByJob();
		logger.debug("SyncKuandaoAccountJob end");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
