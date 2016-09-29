package com.prcsteel.platform.order.web.job;

import javax.annotation.Resource;

import com.prcsteel.platform.common.exception.BusinessException;
import org.apache.log4j.Logger;

import com.prcsteel.platform.order.service.allowance.AllowanceService;
import org.springframework.stereotype.Component;

/**
* @Title: CloseAllowanceJob.java 
* @Package com.prcsteel.cbms.web.job 
* @Description: 自动关闭折让单
* @author lixiang   
* @date 2015年11月26日 下午1:47:49 
* @version V1.0
 */
@Component
public class CloseAllowanceJob extends CbmsJob {

    private static final Logger logger = Logger.getLogger(CloseAllowanceJob.class);

    @Resource
    AllowanceService allowanceService;

    @Override
    public void execute(){
        if(isEnabled()) {
            logger.debug("close allowance job execute start");
            try {
                //未通过
            	allowanceService.automaticClose();
            }catch (BusinessException e1) {
            	 logger.error("Quartz allowance Exception:" + e1.getMsg(), e1);
            } 
            catch (Exception e) {
            	logger.error("Quartz allowance Exception:" + e.getMessage(), e);
            }
            logger.debug("close allowance job execute end");
        }
    }

	@Override
	public boolean isEnabled() {
		return true;
	}

}
