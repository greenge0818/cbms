package com.prcsteel.platform.order.service.order;

/**
 *
 * Created by lixiang on 2016/09/19.
 */
public interface HolidaySettingService {
    
	/**
	 * 根据获取当前系统时间，匹配当前日期是否是工作日
	 * @author lixiang
	 */
	void setHoliday();
	
	/**
	 * 根据当前日期，查询是工作日或非工作日
	 * @param workDate 当前日期
	 * @return
	 */
	int workOrHoliday(String workDate);
}
