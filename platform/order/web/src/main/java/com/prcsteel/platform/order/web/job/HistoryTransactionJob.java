package com.prcsteel.platform.order.web.job;


import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.prcsteel.platform.smartmatch.service.HistoryTransactionService;
import org.springframework.stereotype.Component;

/** 
 * 将订单完成的资源数据导入至资源表，状态为历史成交资源
 * @author peanut <p>2016年2月24日 上午10:19:15</p>  
 */
@Component
public class HistoryTransactionJob extends CbmsJob{
	private static final Logger logger = Logger.getLogger(CopyResourceDataToHistoryJob.class);
	
	@Resource
	private HistoryTransactionService historyTransactionService;
	/**
	 * 执行任务
	 */
	@Override
	public void execute() {
		if (isEnabled()) {
            logger.debug("History Transaction Job execute start");
            historyTransactionService.doHistoryTransactionJob();
            logger.debug("History Transaction Job execute end");
        }
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
