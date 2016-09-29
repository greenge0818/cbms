package com.prcsteel.platform.order.service.order;

import java.util.List;

import com.prcsteel.platform.order.model.model.PickupPerson;

/**
 * Created by Green.Ge on 2015/7/28.
 */
public interface PickupPersonService {
    
  	List<PickupPerson> selectByBillId(Long billId);
  	
}
