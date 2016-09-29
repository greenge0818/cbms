package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.ConsignOrderItemsOperatedrecord;

public interface ConsignOrderItemsOperatedrecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsignOrderItemsOperatedrecord record);

    int insertSelective(ConsignOrderItemsOperatedrecord record);

    ConsignOrderItemsOperatedrecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsignOrderItemsOperatedrecord record);

    int updateByPrimaryKey(ConsignOrderItemsOperatedrecord record);
}