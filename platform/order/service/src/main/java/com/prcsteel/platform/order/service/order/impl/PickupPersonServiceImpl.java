package com.prcsteel.platform.order.service.order.impl;

import java.util.List;

import com.prcsteel.platform.order.service.order.PickupPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.model.PickupPerson;
import com.prcsteel.platform.order.persist.dao.PickupPersonDao;

/**
 * Created by Green.Ge on 2015/7/27.
 */

@Service("pickupPersonService")
public class PickupPersonServiceImpl implements PickupPersonService {
    
    @Autowired
    PickupPersonDao pickupPersonDao;

	@Override
	public List<PickupPerson> selectByBillId(Long billId) {
		return pickupPersonDao.selectByBillId(billId);
	}
   
}