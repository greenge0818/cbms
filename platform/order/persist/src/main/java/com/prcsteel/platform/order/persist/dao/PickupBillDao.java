package com.prcsteel.platform.order.persist.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.prcsteel.platform.order.model.model.PickupBill;

public interface PickupBillDao {
    int deleteByPrimaryKey(Long id);

    int insert(PickupBill record);

    int insertSelective(PickupBill record);

    PickupBill selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PickupBill record);

    int updateByPrimaryKey(PickupBill record);

    List<PickupBill> selectByOrderId(Long orderId);
    
    String getPickupBillSeq(Long orderId);
    
    int getOrderPickedQty(Long orderId);
    
    int disableBill(Long id);
    
    HashMap<String,Object> getHead(Long pickupId);
    
    int checkIfAllInput(Long orderId);
    
    Date getEarlestPickupBillDateByOrderId(Long orderId);
}