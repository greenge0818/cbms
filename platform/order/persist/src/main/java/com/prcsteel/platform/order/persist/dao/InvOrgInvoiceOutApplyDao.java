package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.InvOrgInvoiceOutApply;

public interface InvOrgInvoiceOutApplyDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvOrgInvoiceOutApply record);

    int insertSelective(InvOrgInvoiceOutApply record);

    InvOrgInvoiceOutApply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvOrgInvoiceOutApply record);

    int updateByPrimaryKey(InvOrgInvoiceOutApply record);
}