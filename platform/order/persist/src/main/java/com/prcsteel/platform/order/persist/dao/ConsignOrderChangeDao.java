package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.changecontract.dto.ChangeOrderDto;
import com.prcsteel.platform.order.model.changecontract.dto.ChangeOrderListDto;
import com.prcsteel.platform.order.model.changecontract.dto.QueryChangeOrderDto;
import com.prcsteel.platform.order.model.model.ConsignOrderChange;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConsignOrderChangeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsignOrderChange record);

    int insertSelective(ConsignOrderChange record);

    ConsignOrderChange selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsignOrderChange record);

    int updateByPrimaryKey(ConsignOrderChange record);

    //查询可变更交易单
    List<ChangeOrderListDto> selectByConditions(ChangeOrderDto query);

    int countByConditions(ChangeOrderDto query);

    /**
     * 根据订单Id查询变更记录
     * @param orderId 订单Id
     * @return
     */
    List<ConsignOrderChange> selectByOrderId(Long orderId);

    /**
     * 根据订单Id查询变更记录
     * @param query 查询参数
     * @return
     */
    ConsignOrderChange selectByConsignOrderChange(ConsignOrderChange query);
   
    //查询变更的交易单
    List<ChangeOrderListDto> selectChangeOrderByConditions(ChangeOrderDto query);

    int countChangeOrderByConditions(ChangeOrderDto query);

    //通过订单id查询最新一笔变更合同号
    String queryLastContract(@Param("id") Long id);

    int querySuccessCountByOrderId(@Param("id") Long id);

    List<ConsignOrderChange> selectByQueryDto(QueryChangeOrderDto query);

}