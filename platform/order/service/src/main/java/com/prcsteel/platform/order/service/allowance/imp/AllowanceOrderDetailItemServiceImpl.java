package com.prcsteel.platform.order.service.allowance.imp;

import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.order.persist.dao.InvoiceInAllowanceItemDao;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.dto.AllowanceOrderItemsDto;
import com.prcsteel.platform.order.model.dto.RebateDetailDto;
import com.prcsteel.platform.order.model.query.AllowanceDetailItemQuery;
import com.prcsteel.platform.order.persist.dao.AllowanceDao;
import com.prcsteel.platform.order.persist.dao.AllowanceOrderDetailItemDao;
import com.prcsteel.platform.order.service.allowance.AllowanceOrderDetailItemService;

@Service("allowanceOrderDetailItemService")
public class AllowanceOrderDetailItemServiceImpl implements
		AllowanceOrderDetailItemService {
	@Resource
	private AllowanceOrderDetailItemDao allowanceOrderDetailItemDao;
	
	@Resource
	private AllowanceDao allowanceDao;
	@Resource
	private InvoiceInAllowanceItemDao inAllowanceItemDao;
	
	@Override
	public List<AllowanceOrderItemsDto> queryNoInvoiceDetails(AllowanceDetailItemQuery detailItemQuery) {
		List<AllowanceOrderItemsDto> list = allowanceOrderDetailItemDao.queryDetails(detailItemQuery);
		// 已经使用的折让单详情id集合
		List<Long> detailItemIds = inAllowanceItemDao.selectByAllowanceDetailItemQuery(detailItemQuery);
		// 排除已经使用
		if(null != detailItemIds && detailItemIds.size() > 0) {
			list.removeIf(a -> detailItemIds.contains(a.getDetailItemId()));
		}
		return list;
	}

	@Override
	public List<AllowanceOrderItemsDto> queryDetails(AllowanceDetailItemQuery detailItemQuery) {
		return allowanceOrderDetailItemDao.queryDetails(detailItemQuery);
	}
}
