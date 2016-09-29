package com.prcsteel.platform.order.model.dto;

import java.util.List;

/**
 * Created by caochao on 2015/9/2.
 */
public class TradeStatisticsWithDetailDto extends OrganizationTradeStatisticsDto {
    private List<TradeStatisticsDetailDto> tradeStatisticsDetailList;

    public List<TradeStatisticsDetailDto> getTradeStatisticsDetailList() {
        return tradeStatisticsDetailList;
    }

    public void setTradeStatisticsDetailList(List<TradeStatisticsDetailDto> tradeStatisticsDetailList) {
        this.tradeStatisticsDetailList = tradeStatisticsDetailList;
    }
}
