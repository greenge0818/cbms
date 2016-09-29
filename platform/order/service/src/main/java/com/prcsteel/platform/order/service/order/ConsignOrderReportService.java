package com.prcsteel.platform.order.service.order;

import com.prcsteel.platform.order.model.dto.ConsignOrderDetailReportDto;
import com.prcsteel.platform.order.model.query.ConsignOrderDetailQuery;

/**
 * 代运营单报表
 * @author zhoukun
 *
 */
public interface ConsignOrderReportService {

	ConsignOrderDetailReportDto queryOrderDetailReport(ConsignOrderDetailQuery query);
	
}
