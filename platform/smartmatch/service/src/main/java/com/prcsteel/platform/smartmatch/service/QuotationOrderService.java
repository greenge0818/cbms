package com.prcsteel.platform.smartmatch.service;


import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.smartmatch.model.dto.QuotationItemsPO;
import com.prcsteel.platform.smartmatch.model.dto.QuotationOrderItemsDto;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by Rabbit on 2015/12/24.
 */
public interface QuotationOrderService {
    QuotationOrder selectByPrimaryKey(Long id);

    /**
     * 确认报价
     * @param id
     */
    void confirm(Long id, User user);
    
    /**
     * 根据采购单Id获取报价单
     * @param purchaseOrderId
     */
    List<Map<String, List<QuotationOrderItemsDto>>> selectByPurchaseOrderId(Long purchaseOrderId);

    /**
     * 根据报价单id集，查询报价单明细
     *
     * @param quotationIdList 报价单id集,例：1,2,3,4
     * @return
     * @author peanut
     * @date 2016/6/20
     */
    List<QuotationItemsPO> selectQuotationItemsByQuotationIds(List<Long> quotationIdList);
}
