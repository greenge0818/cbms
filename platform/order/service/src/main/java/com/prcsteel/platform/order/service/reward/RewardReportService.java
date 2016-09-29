package com.prcsteel.platform.order.service.reward;

import java.util.Date;
import java.util.List;

import com.prcsteel.platform.order.model.dto.OrganizationRewardRecordDto;
import com.prcsteel.platform.order.model.dto.ReportNewUserRewardDto;
import com.prcsteel.platform.order.model.dto.ReportRebateRecordDto;
import com.prcsteel.platform.order.model.dto.RewardNewAcccountWithDetailsDto;
import com.prcsteel.platform.order.model.model.CommissionExcel;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ReportCollect;
import com.prcsteel.platform.order.model.model.ReportRebateRecord;
import com.prcsteel.platform.order.model.query.ReportRebateQuery;
import com.prcsteel.platform.order.model.query.ReportRewardQuery;

/**
 * Created by chenchen  on 2015/8/19.
 */
public interface RewardReportService {
	public List<OrganizationRewardRecordDto> getRewardReportByPage(Date start,Date end,long org_id);
	
	public ReportNewUserRewardDto getReportNewUserRewardDto(Date start,Date end,long org_id);
	
	public List<OrganizationRewardRecordDto> getRewardReportByOrg(Date start,Date end,long org_id,String managerName);
	
	public ReportNewUserRewardDto getReportNewUserRewardDtoByManager(Date start,Date end,long org_id);
	
	/**
	 * 买家返利报表公司详情
	 * @param reportRebateQuery
	 * @return
	 */
	public List<ReportRebateRecordDto> getRebateReportByPage(ReportRebateQuery reportRebateQuery);
	
	/**
	 * 买家返利报表公司详情总记录数
	 * @param reportRebateQuery
	 * @return
	 */
	public int totalRebateReport(ReportRebateQuery reportRebateQuery);
	
	/**
	 * 通过客户ID查客户名
	 * @param reportRebateQuery
	 * @return
	 */
	public String getAccountName(ReportRebateQuery reportRebateQuery);

	/**
	 * 查询用户返利详情数据
	 * @param query
	 * @return
	 */
	public  List<ReportRebateRecordDto> queryUserRebateDetail(ReportRebateQuery query);

	/**
	 * 查询用户返利详情数据总数
	 * @param query
	 * @return
	 */
	public int queryUserRebateDetailCount(ReportRebateQuery query) ;


	/**
	 * 获得服务中心的提成列表
	 *
	 * @param queryParam
	 * @return
	 * @author dengxiyan
	 */
	List<RewardNewAcccountWithDetailsDto> getOrgRewardList(ReportRewardQuery queryParam );

	/**
	 * 获得交易员的提成列表
	 *
	 * @param queryParam
	 * @return
	 * @author dengxiyan
	 */

	List<RewardNewAcccountWithDetailsDto> getManagerRewardList(ReportRewardQuery queryParam);
	/**
	 * 根据单号更新买家联系人信息
	 * @param order
	 * @author wangxianjun
	 * @return
	 */
	int updateByCodeSelective(ConsignOrder order);

	/**
	 *
	 * 服务中心提成统计报表导出功能服务中心提成统计报表导出功能
	 *
	 * 明细 加汇总
	 * @param queryParam
	 * @return
	 */
	List<CommissionExcel>  findByParam(ReportRewardQuery queryParam);

	List<ReportCollect>  findSunParam(ReportRewardQuery queryParam);
	List<ReportCollect>  findSellerSunParam(ReportRewardQuery queryParam);

	/**
	 *
	 * 服务中心提成历史数据统计明细报表导出功能
	 * @param queryParam
	 * @return
	 */
	List<CommissionExcel>  selectCommissionListByMonth(ReportRewardQuery queryParam);

	/**
	 *
	 * 服务中心提成历史数据统计汇总报表导出功能
	 * @param queryParam
	 * @return
	 */
	List<ReportCollect>  queryAllByMonth(ReportRewardQuery queryParam);
	/**
	 *
	 * 服务中心提成历史数据插入表中
	 * @param
	 * @return
	 */
	public boolean insertCommission();

}
