package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.DeliveryPrintTimes;
import org.apache.ibatis.annotations.Param;

public interface DeliveryPrintTimesDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeliveryPrintTimes record);

    int insertSelective(DeliveryPrintTimes record);

    DeliveryPrintTimes selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeliveryPrintTimes record);

    int updateByPrimaryKey(DeliveryPrintTimes record);

    int countByDeliveryIdAndType(@Param("deliveryId") Long deliveryId, @Param("deliveryType") String deliveryType);

    int addPrintTimes(@Param("deliveryId") Long deliveryId, @Param("deliveryType") String deliveryType);
}