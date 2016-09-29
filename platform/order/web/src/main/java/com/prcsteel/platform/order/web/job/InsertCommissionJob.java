package com.prcsteel.platform.order.web.job;

import com.prcsteel.platform.order.service.reward.RewardReportService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by rolyer on 15-7-20.
 */
@Component
public class InsertCommissionJob extends CbmsJob {

    private static final Logger logger = Logger.getLogger(InsertCommissionJob.class);

    @Autowired
    RewardReportService rewardReportService;

    @Override
    public void execute(){
        if(isEnabled()) {
            logger.debug("insert commission job execute start");

            try {
                //未付款
                rewardReportService.insertCommission();
            } catch (Exception e) {
                logger.debug("Quartz InsertCommissionJob Exception", e);
            }

            logger.debug("insert commission job execute end");
        }
    }

    @Override
    public boolean isEnabled() {
       /* if(getEnvs()!=null
                && getEnvs().getEnabledCloseOrderJob()!=null
                && Constant.QUARTZ_JOB_ENABLED.equals(getEnvs().getEnabledCloseOrderJob())) {
            return true;
        }*/

        return true;
    }
}
