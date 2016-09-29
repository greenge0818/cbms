package com.prcsteel.platform.order.service.order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.prcsteel.platform.order.model.model.PickupItems;

/**
 * Created by Green.Ge on 2015/7/27.
 */
public interface PickupItemService {
    
  	List<PickupItems> selectByBillId(Long billId);
  	
  //查单据明细
    List<HashMap<String,Object>> selectByBillIdForEdit(Long billId);
    
  //查询已提件重量
    BigDecimal selectPickedWeightByOrderItemId(Long orderItemId);
    
  //查询已提件数
    int selectPickedQtyByOrderItemId(Long orderItemId);
}
