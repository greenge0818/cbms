package com.prcsteel.platform.order.service.invoice;

import com.prcsteel.platform.order.model.model.InvoiceInAllowance;

import java.util.List;

/**
 * Created by lichaowei on 2015/11/30.
 */
public interface InvoiceInAllowanceService {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceInAllowance record);

    int insertSelective(InvoiceInAllowance record);

    InvoiceInAllowance selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceInAllowance record);

    int updateByPrimaryKey(InvoiceInAllowance record);

    /**
     * 根据进项票Id查询
     *
     * @param invoiceInId 进项票Id
     * @return 集合
     */
    InvoiceInAllowance selectByInvoiceInId(Long invoiceInId);

    /**
     * 根据进项票Id删除折让关系
     *
     * @param invoiceInId   进项票Id
     * @param lastUpdatedBy 最后修改人
     * @return 集合
     */
    void deleteByInvoiceInId(Long invoiceInId, String lastUpdatedBy);
}
