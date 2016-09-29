package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.changecontract.dto.QueryChangeOrderDto;
import com.prcsteel.platform.order.model.changecontract.dto.UpdateChangeOrderDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderSellerInfoDto;
import com.prcsteel.platform.order.model.model.ConsignOrderItemsChangedrecord;

import java.util.List;

public interface ConsignOrderItemsChangedrecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsignOrderItemsChangedrecord record);

    int insertSelective(ConsignOrderItemsChangedrecord record);

    ConsignOrderItemsChangedrecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsignOrderItemsChangedrecord record);

    int updateByPrimaryKey(ConsignOrderItemsChangedrecord record);
    
    List<ConsignOrderItemsChangedrecord> queryByItemChangeIds(List<Integer> itemChangeIds);

    ConsignOrderItemsChangedrecord queryByItemChangeId(Integer itemChangeId);

    /**
     * 根据条件查询记录
     * @return 集合
     */
    List<ConsignOrderItemsChangedrecord> selectByChangedrecord(QueryChangeOrderDto query);

    /**
     * 通过变更订单ID修改状态
     * @param updateDto 修改对象
     * @return
     */
    int updateStatusByChangeOrderId(UpdateChangeOrderDto updateDto);

    /**
     * 通过变更订单详情ID修改数据
     * @param record 修改对象
     * @return
     */
    int updateByItemChangeId(ConsignOrderItemsChangedrecord record);
}