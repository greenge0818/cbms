package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.InvoiceInAllowance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InvoiceInAllowanceDao {
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
     * 根据进项票Id删除
     *
     * @param invoiceInId   进项票Id
     * @param lastUpdatedBy 最后修改人
     * @return 集合
     */
    Integer deleteByInvoiceInId(@Param("invoiceInId") Long invoiceInId, @Param("lastUpdatedBy") String lastUpdatedBy);
}