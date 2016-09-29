package com.prcsteel.platform.order.persist.dao;

import java.util.HashMap;
import java.util.List;

import com.prcsteel.platform.order.model.model.DeliveryItems;

public interface DeliveryItemsDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeliveryItems record);

    int insertSelective(DeliveryItems record);

    DeliveryItems selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeliveryItems record);

    int updateByPrimaryKey(DeliveryItems record);
    
    int deleteByPickupId(Long pickupId);
    
//    List<DeliveryItems> getListByDeliveryId(Long deliveryId);

    List<HashMap<String,Object>> getPrintListByDeliveryId(Long deliveryId);

    List<DeliveryItems> selectByBillId(Long billId);

    List<HashMap<String,Object>> getListByDeliveryId(Long deliveryId);

}