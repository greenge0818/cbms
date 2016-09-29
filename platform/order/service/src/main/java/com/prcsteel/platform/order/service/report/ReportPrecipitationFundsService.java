
package com.prcsteel.platform.order.service.report;

import com.prcsteel.platform.order.model.dto.ReportPrecipitationFundsDto;
import com.prcsteel.platform.order.model.query.ReportOrgDayQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportPrecipitationFundsService
 * @Package com.prcsteel.platform.order.service.report
 * @Description: 平台沉淀资金
 * @date 2015/12/10
 */
public interface ReportPrecipitationFundsService {

    void add();

    BigDecimal queryCurrentPrecipitationFunds();

    List<ReportPrecipitationFundsDto> queryByParam(ReportOrgDayQuery query);
}

