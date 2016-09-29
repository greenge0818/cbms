package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.dto.ConsignOrderStatusDto;
import com.prcsteel.platform.order.model.model.OrderAuditTrail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderStatusDao {

    int insertOrderAuditTrail(OrderAuditTrail orderAuditTrail);

    int updateOrderStatus(Map<String, Object> paramMap);

    int updateOrderStatusForSecondarySettlement(Map<String, Object> param);

    List<ConsignOrderStatusDto> getAuditDetailById(Long orderId);

    OrderAuditTrail getLastStatus(@Param("orderId") Long orderId, @Param("statusType") String statusType);

    /**
     * 批量插入
     *
     * @param records 数据集合
     * @return 成功数量
     */
    int batchInsert(List<OrderAuditTrail> records);
    /**
     *二结时更新订单二结时间
     * @param  param
     * @return
     */
    int updateOrderSecondaryTime(Map<String, Object> param);

    int updateOrderStatusForSecondaryAccomplish(Map<String, Object> param);

    /**
     * 按订单id查询
     * @param  orderId
     * @return
     */
    String queryCloseReasonByOrderId(@Param("orderId") Long orderId);
}