package com.prcsteel.platform.smartmatch.service.impl;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.smartmatch.model.dto.QuotationItemsPO;
import com.prcsteel.platform.smartmatch.model.dto.QuotationOrderItemsDto;
import com.prcsteel.platform.smartmatch.model.enums.PurchaseOrderStatus;
import com.prcsteel.platform.smartmatch.model.enums.QuotationOrderStatus;
import com.prcsteel.platform.smartmatch.model.model.PurchaseOrder;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrder;
import com.prcsteel.platform.smartmatch.persist.dao.PurchaseOrderDao;
import com.prcsteel.platform.smartmatch.persist.dao.QuotationOrderDao;
import com.prcsteel.platform.smartmatch.service.QuotationOrderItemsService;
import com.prcsteel.platform.smartmatch.service.QuotationOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by myh on 2015/12/24.
 */
@Service("quotationOrderService")
public class QuotationOrderServiceImpl implements QuotationOrderService {
    @Resource
    QuotationOrderDao quotationOrderDao;
    @Resource
    PurchaseOrderDao purchaseOrderDao;
    @Resource
    QuotationOrderItemsService quotationOrderItemsService;

    public QuotationOrder selectByPrimaryKey(Long id){
        return quotationOrderDao.selectByPrimaryKey(id);
    }

    @Transactional
    @Override
    public void confirm(Long id, User user) {
        QuotationOrder order = quotationOrderDao.selectByPrimaryKey(id);
        order.setStatus(QuotationOrderStatus.CONFIRMED.getCode());  //已确认报价
        order.setLastUpdatedBy(user.getLoginId());
        if(quotationOrderDao.updateByPrimaryKeySelective(order) != 1){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新报价单状态失败");
        }

        PurchaseOrder purchaseOrder = purchaseOrderDao.selectByPrimaryKey(order.getPurchaseOrderId());
        if(!purchaseOrder.getStatus().equals(PurchaseOrderStatus.QUOTED)) {
            purchaseOrder.setStatus(PurchaseOrderStatus.QUOTED.getCode());  //已报价
            purchaseOrder.setLastUpdatedBy(user.getLoginId());
          
            if (purchaseOrderDao.updateByPrimaryKeySelective(purchaseOrder) != 1) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新采购单状态失败");
            }
        }
    }

	@Override
	public List<Map<String,List<QuotationOrderItemsDto>>> selectByPurchaseOrderId(Long purchaseOrderId) {
		List<Map<String,List<QuotationOrderItemsDto>>> quotationOrderList = new ArrayList<>();
		
		List<QuotationOrder> quotationOrders = quotationOrderDao.selectByPurchaseOrderId(purchaseOrderId);
		for (QuotationOrder quotationOrder : quotationOrders){
			Map<String,List<QuotationOrderItemsDto>> quotationMap = new HashMap<>();
			quotationMap.put("quotationItems", quotationOrderItemsService.getDtoByOrderId(quotationOrder.getId()));
			quotationOrderList.add(quotationMap);
		}
		return quotationOrderList;
	}

    /**
     * 根据报价单id集，查询报价单明细
     *
     * @param quotationIdList 报价单id集,例：1,2,3,4
     * @return
     * @author peanut
     * @date 2016/6/20
     */
    @Override
    public List<QuotationItemsPO> selectQuotationItemsByQuotationIds(List<Long> quotationIdList) {

        if(quotationIdList ==null || quotationIdList.isEmpty()) return null;

        return quotationOrderDao.selectQuotationItemsByQuotationIds(quotationIdList);
    }

}
