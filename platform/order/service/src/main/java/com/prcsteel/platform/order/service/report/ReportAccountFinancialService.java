package com.prcsteel.platform.order.service.report;


import com.prcsteel.platform.account.model.dto.AccountTransLogDtoForReport;
import com.prcsteel.platform.order.model.dto.ReportAccountFundDto;
import com.prcsteel.platform.order.model.query.ReportAccountFundQuery;

import java.util.List;
import java.util.Map;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportAccountFinancialService
 * @Package com.prcsteel.platform.order.service.report
 * @Description: 往来单位财务报表服务
 * @date 2015/12/21
 */
public interface ReportAccountFinancialService {

    /**
     * 流水操作踩点
     * @param transLogList
     */
    void accountTransLogOperation(List<AccountTransLogDtoForReport> transLogList);

    Map<String,Object> getAccountFinancialDetail(ReportAccountFundQuery query);

    int queryAccountFinancialDetailTotalByParam(ReportAccountFundQuery query);

    int getTotal(ReportAccountFundQuery query);

    List<ReportAccountFundDto> getAccountFinancialList(ReportAccountFundQuery query);
}
