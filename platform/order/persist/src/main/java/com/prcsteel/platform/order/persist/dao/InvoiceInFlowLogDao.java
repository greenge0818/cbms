package com.prcsteel.platform.order.persist.dao;

import java.util.List;

import com.prcsteel.platform.order.model.dto.InvoiceInFlowDto;
import com.prcsteel.platform.order.model.model.InvoiceInFlowLog;

public interface InvoiceInFlowLogDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceInFlowLog record);

    int insertSelective(InvoiceInFlowLog record);

    InvoiceInFlowLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceInFlowLog record);

    int updateByPrimaryKey(InvoiceInFlowLog record);
    
    List<InvoiceInFlowDto> queryInvoiceInlogById(Long invoiceId);
}