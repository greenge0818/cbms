package com.prcsteel.platform.smartmatch.persist.dao;

import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderDto;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderItemsDto;
import com.prcsteel.platform.smartmatch.model.model.InquiryOrder;
import com.prcsteel.platform.smartmatch.model.query.InquiryOrderQuery;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InquiryOrderDao {
    int deleteByPrimaryKey(Long id);

    int insert(InquiryOrder record);

    int insertSelective(InquiryOrder record);

    InquiryOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InquiryOrder record);

    int updateByPrimaryKey(InquiryOrder record);

    List<InquiryOrderItemsDto> selectByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId, @Param("blockInquiryOrderSellerIds") String blockInquiryOrderSellerIds,
                                                       @Param("quotationOrderId") String quotationOrderId);

    int getSellerNum(Long purchaseOrderId);

    List<InquiryOrderDto> getInquiryOrderList(InquiryOrderQuery inquiryOrderQuery);
    
    List<InquiryOrderDto> getInquiryOrders(Long purchaseOrderId);
}