package com.prcsteel.platform.order.web.job;

import com.prcsteel.platform.order.web.support.AppEnvironment;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.common.constants.Constant;
import org.springframework.stereotype.Component;

/**
 * Created by rolyer on 15-7-20.
 */
@Component
public class CloseOrderJob extends CbmsJob {

    private static final Logger logger = Logger.getLogger(CloseOrderJob.class);

    @Autowired
    ConsignOrderService consignOrderService;

    @Override
    public void execute(){
        if(isEnabled()) {
            logger.debug("close order job execute start");

            try {
                //未付款
                consignOrderService.selectOrderByPayStatus();
            } catch (Exception e) {
                logger.debug("Quartz CloseOrderJob Exception", e);
            }

            logger.debug("close order job execute end");
        }
    }

    @Override
    public boolean isEnabled() {
        if(getEnvs()!=null
                && AppEnvironment.getEnvs()!=null
                && Constant.QUARTZ_JOB_ENABLED.equals(AppEnvironment.getEnvs().get("closeorderjob"))) {
            return true;
        }

        return false;
    }
}
