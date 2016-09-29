package com.prcsteel.platform.order.persist.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.InvoiceComputeDto;
import com.prcsteel.platform.order.model.dto.InvoiceOrderItemsDto;

public interface InvoiceComputeDao {

    List<InvoiceComputeDto> queryComputePoolByOrgId(@Param("orgId") Long orgId);

    List<InvoiceComputeDto> queryPoolOutByOrgId(@Param("orgId") Long orgId,
                                                @Param("ownerId") Long ownerId,
                                                @Param("buyerId") Long buyerId,
                                                @Param("nsortName") String nsortName,
                                                @Param("material") String material,
                                                @Param("spec") String spec);

    int updateActualAmount(@Param("id") Long id,
                           @Param("actualAmount") BigDecimal actualAmount,
                           @Param("lastUpdated") Date lastUpdated,
                           @Param("lastUpdatedBy") String lastUpdatedBy);

    List<InvoiceOrderItemsDto> queryOrderItems(@Param("ownerId") Long ownerId,
                                               @Param("buyerId") Long buyerId,
                                               @Param("nsortName") String nsortName,
                                               @Param("material") String material,
                                               @Param("spec") String spec);

}