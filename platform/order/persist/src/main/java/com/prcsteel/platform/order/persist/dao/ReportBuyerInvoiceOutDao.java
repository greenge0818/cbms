package com.prcsteel.platform.order.persist.dao;

import java.util.List;

import com.prcsteel.platform.order.model.dto.ReportBuyerInvoiceOutDto;
import com.prcsteel.platform.order.model.model.ReportBuyerInvoiceOut;
import com.prcsteel.platform.order.model.query.ReportBuyerInvoiceOutQuery;

public interface ReportBuyerInvoiceOutDao {
    int deleteByPrimaryKey(Long id);

    int insert(ReportBuyerInvoiceOut record);

    int insertSelective(ReportBuyerInvoiceOut record);

    ReportBuyerInvoiceOut selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportBuyerInvoiceOut record);

    int updateByPrimaryKey(ReportBuyerInvoiceOut record);
    
    ReportBuyerInvoiceOut selectByBuyer(Long buyerId);

	List<ReportBuyerInvoiceOutDto> selectByParams(ReportBuyerInvoiceOutQuery query);

	Integer totalByParams(ReportBuyerInvoiceOutQuery query);

	List<ReportBuyerInvoiceOutDto> selectAllByParams(ReportBuyerInvoiceOutQuery query);

	Integer totalAllByParams(ReportBuyerInvoiceOutQuery query);
}