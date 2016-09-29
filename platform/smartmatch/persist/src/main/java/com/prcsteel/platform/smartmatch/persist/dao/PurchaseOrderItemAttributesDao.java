package com.prcsteel.platform.smartmatch.persist.dao;

import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderItemsAttributeDto;
import com.prcsteel.platform.smartmatch.model.model.PurchaseOrderItemAttributes;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PurchaseOrderItemAttributesDao {
    int deleteByPrimaryKey(Long id);

    int insert(PurchaseOrderItemAttributes record);

    int insertSelective(PurchaseOrderItemAttributes record);

    PurchaseOrderItemAttributes selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PurchaseOrderItemAttributes record);

    int updateByPrimaryKey(PurchaseOrderItemAttributes record);

    int batchInsert(List<PurchaseOrderItemAttributes> record);

    int deleteByPurchaseOrderId(Long orderId);

    List<PurchaseOrderItemsAttributeDto> getAttributesByPurchaseOrderItemIdAndCategoryUuid(
            @Param("categoryUuid") String categoryUuid, @Param("purchaseOrderItemId") Long purchaseOrderItemId);
    
    List<PurchaseOrderItemsAttributeDto>  getAllAttributes();
}