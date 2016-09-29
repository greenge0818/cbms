package com.prcsteel.platform.order.service.report;

import java.util.List;

import com.prcsteel.platform.order.model.dto.OrgListDto;
import com.prcsteel.platform.order.model.query.OrgSecondSettlementQuery;

/**
 * @author lixiang
 * @version V1.1
 * @Title: OrgSecondSettlementService
 * @Package com.prcsteel.platform.order.service.report
 * @Description: 服务中心二次结算储备金日报
 * @date 2015/8/26
 */
public interface OrgSecondSettlementService {

	/**
	 * 查询服务中心下的可用额度和已使用额度，统计查询服务中心储备金日报
	 * 
	 * @param orgSecondSettlementQuery
	 * @return
	 */
	public List<OrgListDto> queryByOrg(
			OrgSecondSettlementQuery orgSecondSettlementQuery);

	/**
	 * 查询服务中心下的可用额度和已使用额度的记录数
	 * 
	 * @param orgSecondSettlementQuery
	 * @return
	 */
	public Integer queryByOrgCount(
			OrgSecondSettlementQuery orgSecondSettlementQuery);
}
