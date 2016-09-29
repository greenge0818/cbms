package com.prcsteel.platform.order.web.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.prcsteel.platform.smartmatch.service.CopyResourceService;
import org.springframework.stereotype.Component;

/** 
 * 复制资源数据至资源历史表job
 * 
 * @author peanut  <p> 2016年2月17日 下午2:51:50</p>   
 */
@Component
public class CopyResourceDataToHistoryJob extends CbmsJob {
	    private static final Logger logger = Logger.getLogger(CopyResourceDataToHistoryJob.class);

	    @Resource
	    private CopyResourceService copyResourceService;

	    @Override
	    public void execute() {
	        if (isEnabled()) {
	            logger.debug("Copy Resource Job execute start");
	            copyResourceService.doCopyResource();
	            logger.debug("Copy Resource Job execute end");
	        }
	    }

	    @Override
	    public boolean isEnabled() {
	        return true;
	    }
}
