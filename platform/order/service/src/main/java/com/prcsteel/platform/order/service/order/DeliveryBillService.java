package com.prcsteel.platform.order.service.order;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.prcsteel.platform.order.model.dto.DeliveryItemDto;
import com.prcsteel.platform.order.model.model.DeliveryBill;
import com.prcsteel.platform.order.model.model.PickupPerson;
import com.prcsteel.platform.acl.model.model.User;


/**
 * Created by Green.Ge on 2015/7/28.
 */
public interface DeliveryBillService {
	//查提货单是否已经存在已打印的放货单，如果存在则不允许修改提货单。
	int getPrintedDeliveryBillCountByPickupId(Long pickupId);

//	//根据提货单id获取放货单打印列表
//	List<HashMap<String,Object>> getBillPrintInfoByPickupId(Long pickupId);
	//查单据明细
	List<DeliveryItemDto> selectByBillIdForEdit(Long billId);

	DeliveryBill selectById(Long id);

	List<PickupPerson> selectPickupPersonByPickupBillId(Long id);

	boolean save(DeliveryBill db, List<PickupPerson> persons, MultipartFile file, User user);

	//根据提货单id获取放货单详细信息，包括表体
	List<HashMap<String,Object>> getBillByPickupId(Long pickupId);
	//根据订单id获取放货单详细信息，包括表体
	List<HashMap<String,Object>> getBillByOrderId(Long orderId, String deliveryType);
	
	//打印放货单
	void printDelivery(Long id,String deliveryType,User user);
}
