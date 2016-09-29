package com.prcsteel.platform.order.persist.dao;

import java.util.List;

import com.prcsteel.platform.order.model.model.InvoiceInProcess;

public interface InvoiceInProcessDao {
    int deleteByPrimaryKey(Integer id);

    int insert(InvoiceInProcess record);

    int insertSelective(InvoiceInProcess record);

    InvoiceInProcess selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InvoiceInProcess record);

    int updateByPrimaryKey(InvoiceInProcess record);
    
    List<InvoiceInProcess> queryByOperatorId(Long userId);
}