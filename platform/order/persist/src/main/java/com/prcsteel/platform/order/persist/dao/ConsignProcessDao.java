package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.ConsignProcess;

public interface ConsignProcessDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsignProcess record);

    int insertSelective(ConsignProcess record);

    ConsignProcess selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsignProcess record);

    int updateByPrimaryKey(ConsignProcess record);

    ConsignProcess getOfficeStaff(Long userId);
}