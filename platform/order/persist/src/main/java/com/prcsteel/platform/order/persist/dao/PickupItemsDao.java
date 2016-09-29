package com.prcsteel.platform.order.persist.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.prcsteel.platform.order.model.model.PickupItems;

public interface PickupItemsDao {
    int deleteByPrimaryKey(Long id);
    
    int insert(PickupItems record);

    int insertSelective(PickupItems record);

    PickupItems selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PickupItems record);

    int updateByPrimaryKey(PickupItems record);

    List<PickupItems> selectByBillId(Long billId);
    
    //查待开单表体
    List<HashMap<String, Object>> selectBillDetail(Long orderId);
    //查询已提件数
    int selectPickedQtyByOrderItemId(Long orderItemId);
    //查询已提件重量
    BigDecimal selectPickedWeightByOrderItemId(Long orderItemId);
    //根据单据ID删除明细
    int deleteByBillId(Long billId);
    //查单据明细
    List<HashMap<String,Object>> selectByBillIdForEdit(Long billId);
    //查询提货单打印列表
    List<HashMap<String,Object>> getPrintListById(Long pickupId);
    
    /**
     * 通过订单id查已提货数量和可提货数量
     * @author lixiang
     * @param orderId
     * @return
     */
    List<PickupItems> getQuantityByOrderId(Long orderId);
}