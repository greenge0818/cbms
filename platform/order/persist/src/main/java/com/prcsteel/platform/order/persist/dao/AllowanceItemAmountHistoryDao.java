package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.AllowanceItemAmountHistory;

public interface AllowanceItemAmountHistoryDao {
    int deleteByPrimaryKey(Long id);

    int insert(AllowanceItemAmountHistory record);

    int insertSelective(AllowanceItemAmountHistory record);

    AllowanceItemAmountHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AllowanceItemAmountHistory record);

    int updateByPrimaryKey(AllowanceItemAmountHistory record);
}