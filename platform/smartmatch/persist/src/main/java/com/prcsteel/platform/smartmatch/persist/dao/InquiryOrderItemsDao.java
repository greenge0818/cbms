package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderSatisfactionDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchResultItemsDto;
import com.prcsteel.platform.smartmatch.model.model.InquiryOrderItems;

public interface InquiryOrderItemsDao {
    int deleteByPrimaryKey(Long id);

    int insert(InquiryOrderItems record);

    int insertSelective(InquiryOrderItems record);

    InquiryOrderItems selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InquiryOrderItems record);

    int updateByPrimaryKey(InquiryOrderItems record);
    
    int deleteByInquiryOrderId(Long inquiryOrderId);
    
    List<SearchResultItemsDto> getInquiryOrderItems(Long inquiryOrderSellerId);
    
    int getPurchaseOrderItemsNum(Long purchaseOrderId);
    /**
     * 获取订单item满足率
     * @param purchaseOrderId
     * @return
     */
    List <InquiryOrderSatisfactionDto> getPurchaseOrderItemsSatisfaction(Long purchaseOrderId);
}