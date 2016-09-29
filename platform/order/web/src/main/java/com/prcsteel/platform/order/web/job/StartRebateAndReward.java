package com.prcsteel.platform.order.web.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.prcsteel.platform.order.service.rebate.RebateService;
import com.prcsteel.platform.acl.service.RewardService;
import org.springframework.stereotype.Component;

/**
 * Created by chenchen on 15-8-4.
 */
@Component
public class StartRebateAndReward extends CbmsJob {

    private static final Logger logger = Logger.getLogger(StartRebateAndReward.class);

    @Autowired
    RewardService rewardService;
    @Autowired
    RebateService rebateSevice;

    @Override
    public void execute() {
        if (isEnabled()) {
            try {
                /**
                 * 如果有到期并且尚未生效的提成，返现方案，执行更新操作
                 */
                if (rewardService.countNextMonthEffectRecord() > 0) {
                    rewardService.startNewReward();
                }
                if (rebateSevice.cancleEffectRebate() > 0) {
                    rebateSevice.startNewRebate();
                }
                System.out.println("testtesttest");
                logger.debug("start new rebate and reward　execute");
            } catch (Exception e) {
                logger.debug("start new rebate and reward　error", e);
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
