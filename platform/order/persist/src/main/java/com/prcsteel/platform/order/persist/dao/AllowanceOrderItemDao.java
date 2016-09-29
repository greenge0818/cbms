package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.AllowanceOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AllowanceOrderItemDao {
    int deleteByPrimaryKey(Long id);

    int insert(AllowanceOrderItem record);

    int insertSelective(AllowanceOrderItem record);

    AllowanceOrderItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AllowanceOrderItem record);

    int updateByPrimaryKey(AllowanceOrderItem record);

    int updateDeletedByAllowanceId(@Param("allowanceIds") List<Long> allowanceIds, @Param("lastUpdatedBy") String lastUpdatedBy);

    int countSecondClosedOrderByAllwanceId(Long allowanceId);
}