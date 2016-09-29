package com.prcsteel.platform.smartmatch.service;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderDto;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderSellersDto;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrder;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrderItems;
import com.prcsteel.platform.smartmatch.model.query.InquiryOrderQuery;

import net.sf.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * Created by myh on 2015/11/26.
 */
public interface InquiryOrderService {
    List<InquiryOrderDto> selectByPurchaseOrderId(Long id, String option, String blockInquiryOrderSellerIds, String quotationOrderId);

    Long saveQuotationOrder(QuotationOrder order, List<QuotationOrderItems> items, User user);

    Map<String, Object> getSellerNum(Long purchaseOrderId, User user);

    List<InquiryOrderDto>  getInquiryOrderList(InquiryOrderQuery inquiryOrderQuery);

    void updateInquiryItems(JSONArray inquiryOrderItemsList, User user);
    
    List<InquiryOrderSellersDto> getSellersInquiryRecord(String sellerIds);
    
    String getCityByWarehouseId(Long warehouseId);
}
