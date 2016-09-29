package com.prcsteel.platform.order.persist.dao;

import java.util.List;

import com.prcsteel.platform.order.model.dto.ReportSellerInvoiceInDto;
import com.prcsteel.platform.order.model.model.ReportSellerInvoiceIn;
import com.prcsteel.platform.order.model.query.ReportSellerInvoiceInDataQuery;
import com.prcsteel.platform.order.model.query.ReportSellerInvoiceInQuery;

public interface ReportSellerInvoiceInDao {
    int deleteByPrimaryKey(Long id);

    int insert(ReportSellerInvoiceIn record);

    int insertSelective(ReportSellerInvoiceIn record);

    ReportSellerInvoiceIn selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportSellerInvoiceIn record);

    int updateByPrimaryKey(ReportSellerInvoiceIn record);

	ReportSellerInvoiceIn queryLastReportSellerInvoice(ReportSellerInvoiceInQuery reportSellerInvoiceInQuery);

	Integer queryReportSellerInvoiceInDataCount(ReportSellerInvoiceInDataQuery query);

	List<ReportSellerInvoiceInDto> queryReportSellerInvoiceInData(ReportSellerInvoiceInDataQuery query);

	Integer queryReportSellerInvoiceInDetailDataCount(ReportSellerInvoiceInDataQuery query);

	List<ReportSellerInvoiceIn> queryReportSellerInvoiceInDetailData(ReportSellerInvoiceInDataQuery query);

	ReportSellerInvoiceInDto queryReportSellerInvoiceInRangeAmount(ReportSellerInvoiceInDataQuery query);
}