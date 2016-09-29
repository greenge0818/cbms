package com.prcsteel.platform.order.web.job;

import com.prcsteel.platform.order.service.QuartzJobService;
import com.prcsteel.platform.order.service.receipt.ReceiptService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tuxianming
 */
@Component
public class ReceiptDataToThisSysJob extends CbmsJob {

    private static final Logger logger = Logger.getLogger(ReceiptDataToThisSysJob.class);

    @Autowired
    private ReceiptService receiptService;

    private String jobName = "ReceiptDataToThisSys";
    @Resource
    private QuartzJobService quartzJobService;

    /**
     * 将爱信诺的已经打印的发票数据同步到本地
     */
    @Override
    public void execute() {
        if (isEnabled()){
            logger.debug("Sync receipt data job to local system execute start");

            receiptService.executeSynInvoiceOutBack();

            quartzJobService.finish(jobName);

            logger.debug("Sync receipt data job to local system execute end");
        }
    }

    @Override
    public boolean isEnabled() {
        return quartzJobService.starting(jobName, "");
    }
}
