package com.prcsteel.platform.order.web.job;

import com.prcsteel.platform.order.service.order.CertificateService;
import org.apache.log4j.Logger;

import javax.annotation.Resource;

/**
 * @author peanut
 * @version 1.0
 * @description 需补齐买、卖家凭证的已开票订单job
 * @date 2016/4/12 10:18
 */
public class CertificateInvoiceJob  extends CbmsJob {
    private static final Logger logger = Logger.getLogger(CertificateInvoiceJob.class);
    @Resource
    private CertificateService certificateService;
    /**
     * 执行任务
     */
    @Override
    public void execute() {
        if (isEnabled()) {
            logger.debug("Certificate Invoice Job execute start");
            certificateService.executeCertificateInvoiceJob();
            logger.debug("Certificate Invoice Job execute end");
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
