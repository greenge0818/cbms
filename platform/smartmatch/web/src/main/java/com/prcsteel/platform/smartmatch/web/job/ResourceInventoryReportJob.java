package com.prcsteel.platform.smartmatch.web.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.prcsteel.platform.smartmatch.service.ReportResourceInventoryService;

/**
 * 库存监控定时任务
 * 每天17：30执行
 * Created by Rolyer on 2015/12/17.
 */
@Component
public class ResourceInventoryReportJob extends CbmsJob {
    private static final Logger logger = Logger.getLogger(ResourceInventoryReportJob.class);

    @Resource
    private ReportResourceInventoryService reportResourceInventoryService;

    @Override
    public void execute() {
        if (isEnabled()) {
            logger.debug("Resource inventory report job execute start");
            reportResourceInventoryService.dailyStatistics();
            logger.debug("Resource inventory report job execute end");
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
