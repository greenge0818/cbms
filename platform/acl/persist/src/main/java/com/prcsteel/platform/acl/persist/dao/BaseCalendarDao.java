package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.acl.model.model.BaseCalendar;
import com.prcsteel.platform.acl.model.query.HolidayQuery;

public interface BaseCalendarDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BaseCalendar record);

    int insertSelective(BaseCalendar record);

    BaseCalendar selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseCalendar record);

    int updateByPrimaryKey(BaseCalendar record);
    
    /**
     * 通过当前时间查询是否存在
     * @param workDate
     * @return
     */
    BaseCalendar selectByWorkDate(HolidayQuery holidayQuery);
}