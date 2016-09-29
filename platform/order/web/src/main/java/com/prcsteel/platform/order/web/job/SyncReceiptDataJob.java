package com.prcsteel.platform.order.web.job;

import com.prcsteel.platform.order.service.QuartzJobService;
import com.prcsteel.platform.order.service.receipt.ReceiptService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Rolyer on 2015/9/29.
 */
@Component
public class SyncReceiptDataJob extends CbmsJob {

    private static final Logger logger = Logger.getLogger(SyncReceiptDataJob.class);

    @Autowired
    private ReceiptService receiptService;

    private String jobName = "SyncReceiptData";
    @Resource
    private QuartzJobService quartzJobService;

    @Override
    public void execute() {
        if (isEnabled()){
            logger.debug("Sync receipt data job execute start");

            receiptService.syncReceiptData();

            quartzJobService.finish(jobName);

            logger.debug("Sync receipt data job execute end");
        }
    }

    @Override
    public boolean isEnabled() {
        return quartzJobService.starting(jobName, "");
    }
}
