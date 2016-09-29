package com.prcsteel.platform.order.persist.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.prcsteel.platform.order.model.model.DeliveryBill;

public interface DeliveryBillDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeliveryBill record);

    int insertSelective(DeliveryBill record);

    DeliveryBill selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeliveryBill record);

    int updateByPrimaryKey(DeliveryBill record);
    
    String getDeliveryBillSeq(Long pickupId);
    
    int deleteByPickupId(Long pickupId);
    
    int getPrintedDeliveryBillCountByPickupId(Long pickupId);
    
    int disableBillByPickupId(Long pickupId);
    
    HashMap<String,Object> getHead(Long id);
    
    List<DeliveryBill> getListByPickupId(Long pickupId);
    
    List<DeliveryBill> getListByOrderId(Long orderId);
    
    int checkIfAllPrinted(Long orderId);
    
    Date getEarlestDeliveryBillDateByOrderId(Long orderId);

	DeliveryBill queryLastRecord();
}