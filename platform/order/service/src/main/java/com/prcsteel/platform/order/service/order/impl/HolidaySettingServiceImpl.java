package com.prcsteel.platform.order.service.order.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.acl.model.model.AllHoliday;
import com.prcsteel.platform.acl.model.model.BaseCalendar;
import com.prcsteel.platform.acl.model.query.HolidayQuery;
import com.prcsteel.platform.acl.persist.dao.AllHolidayDao;
import com.prcsteel.platform.acl.persist.dao.BaseCalendarDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.service.order.HolidaySettingService;


@Service("holidaySettingService")
public class HolidaySettingServiceImpl implements HolidaySettingService {
	
	private final static Logger logger = Logger.getLogger(HolidaySettingServiceImpl.class);
	
	private final static String TITLE = "补班";
	
	@Value("${startTime}")
	private String startTime;  //上班起始时间
	
	@Value("${endTimeSummer}")
	private String endTimeSummer;  //夏季下班时间
	
	@Value("${endTimeWinter}")
	private String endTimeWinter;  //冬季下班时间
	
	private final static String WORK = "work";
	
	private final static String HOLIDAY = "holiday";
	
	private final static String SYSTEM = "系统";
	
	@Resource
	private BaseCalendarDao baseCalendarDao;
	
	@Resource
    private AllHolidayDao allHolidayDao;
	
	@Override
	public void setHoliday() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;//获取当前月
		String dateBeijing =df.format(new Date());//获取当前北京时间
		String nowTimes[] = dateBeijing.split(" ");
		HolidayQuery holidayQuery = new HolidayQuery();
		try {
			Date dateNow = f.parse(nowTimes[0]);
			AllHoliday allHoliday = allHolidayDao.selectByDate(dateNow);
			holidayQuery.setWorkDate(nowTimes[0]);
			BaseCalendar baseCalendar = baseCalendarDao.selectByWorkDate(holidayQuery);
			if (baseCalendar == null) {
				baseCalendar = new BaseCalendar();
				baseCalendar.setWorkDate(nowTimes[0]);
				if (allHoliday == null || TITLE.equals(allHoliday.getTitle())) {
					baseCalendar.setStartTime(df.parse(nowTimes[0] +" " + startTime));
					if (month == 10 || month == 11 || month == 12 || month == 1
							|| month == 2 || month == 3 || month == 4) {
						baseCalendar.setEndTime(df.parse(nowTimes[0] +" " + endTimeSummer));
					} else {
						baseCalendar.setEndTime(df.parse(nowTimes[0] +" " + endTimeWinter));
					}
					baseCalendar.setWorkStatus(WORK);
				} else {
					baseCalendar.setWorkStatus(HOLIDAY);
				}
				baseCalendar.setCreated(new Date());
				baseCalendar.setCreatedBy(SYSTEM);
				baseCalendar.setLastUpdated(new Date());
				baseCalendar.setLastUpdatedBy(SYSTEM);
				baseCalendar.setModificationNumber(0);
				int num = baseCalendarDao.insertSelective(baseCalendar);
				if (num == 0) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"新增假期失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	@Override
	public int workOrHoliday(String workDate) {
		int number = 0 ;
		String nowTimes[] = workDate.split("_");
		HolidayQuery holidayQuery = new HolidayQuery();
		holidayQuery.setWorkDate(nowTimes[0]);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
		try {
			holidayQuery.setStartTime(df.parse(workDate));
			BaseCalendar baseCalendar = baseCalendarDao.selectByWorkDate(holidayQuery);
			if (baseCalendar != null) {
				if (WORK.equals(baseCalendar.getWorkStatus())) {
					number = 1;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return number;
	}
	
}
