package com.prcsteel.platform.acl.persist.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.acl.model.model.AllHoliday;
import com.prcsteel.platform.acl.model.query.HolidayQuery;

public interface AllHolidayDao {
    int deleteByPrimaryKey(String id);

    int insert(AllHoliday record);

    int insertSelective(AllHoliday record);

    AllHoliday selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AllHoliday record);

    int updateByPrimaryKey(AllHoliday record);
    
    List<AllHoliday> selectAll(HolidayQuery holidayQuery);
    
    int selectAllCount(HolidayQuery holidayQuery);

	AllHoliday selectByDate(Date holidayDate);
}