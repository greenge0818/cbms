package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.dto.ReportRebateDto;
import com.prcsteel.platform.order.model.dto.ReportRebateRecordDto;
import com.prcsteel.platform.order.model.model.CommissionExcel;
import com.prcsteel.platform.order.model.model.ReportCollect;
import com.prcsteel.platform.order.model.model.ReportRebateRecord;
import com.prcsteel.platform.order.model.query.BuyerRebateQuery;
import com.prcsteel.platform.order.model.query.ReportRebateQuery;
import com.prcsteel.platform.order.model.query.ReportRewardQuery;

import java.util.List;

public interface ReportRebateRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(ReportRebateRecord record);

    int insertSelective(ReportRebateRecord record);

    ReportRebateRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportRebateRecord record);

    int updateByPrimaryKey(ReportRebateRecord record);

    /**
     * 批量插入
     * @param records
     * @return
     */
    int batchInsert(List<ReportRebateRecord> records);

	/**
	 * 买家返利报表公司详情
	 * @param reportRebateQuery
	 * @return
	 */
    public List<ReportRebateRecord> queryComRebateByPage(ReportRebateQuery reportRebateQuery);
    
    /**
	 * 买家返利报表公司详情总记录数
	 * @param reportRebateQuery
	 * @return
	 */
    public int totalComRebate(ReportRebateQuery reportRebateQuery);

    public List<ReportRebateRecordDto> queryUserRebateDetail(ReportRebateQuery query);

    public int queryUserRebateDetailCount(ReportRebateQuery query);

    /**
     * 查询买家返利信息列表
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    List<ReportRebateDto> queryBuyerRebate(BuyerRebateQuery queryParam);

    /**
     * 统计买家返利记录数
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    int countBuyerRebate(BuyerRebateQuery queryParam);

    /**
     * 根据买家查询所有联系人的返利信息列表
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    List<ReportRebateDto> queryAllContactsRebateByBuyerId(BuyerRebateQuery queryParam);

    /**
     * 查询联系人返利信息列表
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    List<ReportRebateDto> queryContactsRebate(BuyerRebateQuery queryParam);

    /**
     * 查询买家的大类返利信息列表
     * @param queryParam
     * @return
     */
    List<ReportRebateRecord> queryGroupCategoryRebateByBuyerId(BuyerRebateQuery queryParam);

    /**
     * 查询联系人的大类返利信息列表
     * @param queryParam
     * @return
     */
    List<ReportRebateRecord> queryGroupCategoryRebateByContactId(BuyerRebateQuery queryParam);

    /**
     * 查询联系人的提现信息
     * @param queryParam
     * @return
     */
    List<ReportRebateDto> queryContactWithdrawByContactIds(BuyerRebateQuery queryParam);

    /**
     * 查询买家的提现信息
     * @param queryParam
     * @return
     */
    List<ReportRebateDto> queryAccountWithdrawByAccountIds(BuyerRebateQuery queryParam);

    /**
     * 回滚买家返利数据
     * @param orderCode
     * @return
     */
    int disableRebateByOrderCode(String orderCode);

    /**
     * 查询该比订单买家获得的返利
     * @param orderCode
     * @return
     */
    List<ReportRebateRecord> queryByOrderCode(String orderCode);
    /**
     * 根据单号批量更新买家联系人信息
     * @param recordList
     * @author wangxianjun
     * @return
     */
    int batchUpdateReportRebateList(List<ReportRebateRecord> recordList);
    /**
     *
     * 服务中心提成统计报表导出功能服务中心提成统计报表导出功能
     * @param queryParam
     * @return
     */
    List<CommissionExcel>  findByParam(ReportRewardQuery queryParam);

    /**
     *
     * 买家汇总
     * @param queryParam
     * @return
     */
    List<ReportCollect>  findSunParam(ReportRewardQuery queryParam);

    /**
     * 买家汇总
     * @param queryParam
     * @return
     */
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
     * 批量插入服务中心提成历史数据
     * @param commissionList
     * @return
     */
    int batchInsertCommission(List<CommissionExcel> commissionList);

}
