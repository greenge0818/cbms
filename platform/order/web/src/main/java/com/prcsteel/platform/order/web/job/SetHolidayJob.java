package com.prcsteel.platform.order.web.job;

import javax.annotation.Resource;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.service.order.HolidaySettingService;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;

/**
* @Title: SetHolidayJob.java 
* @Package com.prcsteel.cbms.web.job 
* @Description: 每日凌晨自动设置工作日与非工作日
* @author lixiang   
* @date 2016年09月19日 下午1:47:49 
* @version V1.0
 */
@Component
public class SetHolidayJob extends CbmsJob {

    private static final Logger logger = Logger.getLogger(SetHolidayJob.class);

    @Resource
    HolidaySettingService holidaySettingService;

    @Override
    public void execute(){
        if(isEnabled()) {
            logger.debug("set holiday job execute start");
            try {
            	holidaySettingService.setHoliday();
            }catch (BusinessException e1) {
            	 logger.error("Quartz allowance Exception:" + e1.getMsg(), e1);
            } 
            catch (Exception e) {
            	logger.error("Quartz allowance Exception:" + e.getMessage(), e);
            }
            logger.debug("set holiday job execute end");
        }
    }

	@Override
	public boolean isEnabled() {
		return true;
	}

}
