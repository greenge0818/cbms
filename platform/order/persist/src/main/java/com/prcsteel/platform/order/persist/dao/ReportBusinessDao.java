package com.prcsteel.platform.order.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.model.dto.InvoiceInBordereauxDto;
import com.prcsteel.platform.order.model.dto.TradeStatisticsDetailDto;
import com.prcsteel.platform.order.model.dto.TradeStatisticsWithDetailDto;
import com.prcsteel.platform.order.model.dto.UninvoicedInDto;
import com.prcsteel.platform.order.model.query.InvoiceInBordereauxQuery;
import com.prcsteel.platform.order.model.query.TradeStatisticsQuery;

/**
 * Created by caochao on 2015/8/31.
 */
public interface ReportBusinessDao {
    int queryTradeStatisticsCount(TradeStatisticsQuery query);

    List<TradeStatisticsWithDetailDto> queryTradeStatisticsMainData(TradeStatisticsQuery query);

    List<TradeStatisticsWithDetailDto> queryTradeStatisticsBuyerData(TradeStatisticsQuery query);

    List<TradeStatisticsWithDetailDto> queryTradeStatisticsFrequentData(TradeStatisticsQuery query);

    List<TradeStatisticsWithDetailDto> queryTradeStatisticsConsignData(TradeStatisticsQuery query);

    List<TradeStatisticsWithDetailDto> queryTradeStatisticsTempData(TradeStatisticsQuery query);

    List<TradeStatisticsDetailDto> queryTradeStatisticsDetailMainData(TradeStatisticsQuery query);

    List<TradeStatisticsDetailDto> queryTradeStatisticsDetailBuyerData(TradeStatisticsQuery query);

    List<TradeStatisticsDetailDto> queryTradeStatisticsDetailFrequentData(TradeStatisticsQuery query);

    List<TradeStatisticsDetailDto> queryTradeStatisticsDetailConsignData(TradeStatisticsQuery query);

    List<TradeStatisticsDetailDto> queryTradeStatisticsDetailTempData(TradeStatisticsQuery query);

    List<UninvoicedInDto> queryUninvoicedInList(Map<String, Object> param);

    Integer countUninvoicedInList(Map<String, Object> param);
    
    List<InvoiceInBordereauxDto> queryOrderItems(InvoiceInBordereauxQuery queryParam);

	int queryInvoiceInBordereauxCount(InvoiceInBordereauxQuery queryParam);

	InvoiceInBordereauxDto queryInvoiceInBordereauxSum(InvoiceInBordereauxQuery queryParam);
}
