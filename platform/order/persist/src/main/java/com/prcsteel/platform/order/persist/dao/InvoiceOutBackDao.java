package com.prcsteel.platform.order.persist.dao;

import java.util.List;

import com.prcsteel.platform.order.model.model.InvoiceOutBack;
import com.prcsteel.platform.order.model.query.InvoiceOutBackQuery;

public interface InvoiceOutBackDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceOutBack record);

    int insertSelective(InvoiceOutBack record);

    InvoiceOutBack selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceOutBack record);

    int updateByPrimaryKey(InvoiceOutBack record);

	List<InvoiceOutBack> selectByParams(InvoiceOutBackQuery query);
}