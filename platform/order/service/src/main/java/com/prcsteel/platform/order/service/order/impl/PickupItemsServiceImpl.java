package com.prcsteel.platform.order.service.order.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.model.PickupItems;
import com.prcsteel.platform.order.persist.dao.PickupItemsDao;
import com.prcsteel.platform.order.service.order.PickupItemService;

/**
 * Created by Green.Ge on 2015/7/27.
 */

@Service("pickupItemService")
public class PickupItemsServiceImpl implements PickupItemService {
    
    @Autowired
    PickupItemsDao pickupItemsDao;

	@Override
	public List<PickupItems> selectByBillId(Long billId) {
		return pickupItemsDao.selectByBillId(billId);
	}

	@Override
	public List<HashMap<String, Object>> selectByBillIdForEdit(Long billId) {
		return pickupItemsDao.selectByBillIdForEdit(billId);
	}

	@Override
	public BigDecimal selectPickedWeightByOrderItemId(Long orderItemId) {
		
		return pickupItemsDao.selectPickedWeightByOrderItemId(orderItemId);
	}

	@Override
	public int selectPickedQtyByOrderItemId(Long orderItemId) {
		return pickupItemsDao.selectPickedQtyByOrderItemId(orderItemId);
	}
   
}