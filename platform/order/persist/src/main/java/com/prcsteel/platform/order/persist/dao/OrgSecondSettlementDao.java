package com.prcsteel.platform.order.persist.dao;

import java.util.List;

import com.prcsteel.platform.order.model.dto.OrgSecondSettlementDto;
import com.prcsteel.platform.order.model.dto.OrganizationsDto;
import com.prcsteel.platform.order.model.query.OrgSecondSettlementQuery;

/**
 * @author lixiang
 * @version V1.1
 * @Title: OrgSecondSettlementDao
 * @Package com.prcsteel.platform.order.persist.dao
 * @Description: 服务中心二次结算储备金日报
 * @date 2015/8/26
 */

public interface OrgSecondSettlementDao {

	/**
	 * 查询服务中心下的可用额度和已使用额度
	 * 
	 * @param orgSecondSettlementQuery
	 * @return
	 */
	public List<OrganizationsDto> queryByOrg(
			OrgSecondSettlementQuery orgSecondSettlementQuery);

	/**
	 * 查询服务中心下的可用额度和已使用额度的记录数
	 * 
	 * @param orgSecondSettlementQuery
	 * @return
	 */
	public Integer queryByOrgCount(
			OrgSecondSettlementQuery orgSecondSettlementQuery);

	/**
	 * 统计查询服务中心储备金日报
	 * 
	 * @param orgSecondSettlementQuery
	 * @return
	 */
	public List<OrgSecondSettlementDto> queryByOrgId(
			OrgSecondSettlementQuery orgSecondSettlementQuery);

}
