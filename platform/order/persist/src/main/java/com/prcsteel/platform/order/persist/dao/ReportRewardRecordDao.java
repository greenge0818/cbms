package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.dto.OrganizationRewardRecordDto;
import com.prcsteel.platform.order.model.dto.RewardDetailDto;
import com.prcsteel.platform.order.model.dto.RewardReportDto;
import com.prcsteel.platform.order.model.model.CommissionExcel;
import com.prcsteel.platform.order.model.model.ReportRewardRecord;
import com.prcsteel.platform.order.model.query.ReportRewardQuery;

import java.util.List;
import java.util.Map;

public interface ReportRewardRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(ReportRewardRecord record);

    int insertSelective(ReportRewardRecord record);

    /**
     * 批量插入
     * @param records
     * @return
     */
    int batchInsert(List<ReportRewardRecord> records);

    ReportRewardRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportRewardRecord record);

    int updateByPrimaryKey(ReportRewardRecord record);

    List<RewardReportDto> queryRewardDtoByOrgAndDate(Map<String, Object> paramMap);

    List<RewardReportDto> queryRewardDtoByManager(Map<String, Object> paramMap);

    List<OrganizationRewardRecordDto> queryManager(Map<String, Object> paramMap);

    /**
     * 查询服务中心的所有大类提成详情
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    List<RewardDetailDto> queryOrgRewardDetails(ReportRewardQuery queryParam);

    /**
     * 查询交易员的所有大类提成详情
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    List<RewardDetailDto> queryManagerRewardDetails(ReportRewardQuery queryParam);

    /**
     * 软删除该比订单下买/卖家管理员提成
     * @param orderId
     * @return
     */
    int disableRewardByOrderId(Long orderId);
    /**
     *
     * 服务中心提成统计报表导出功能服务中心提成统计报表导出功能
     * @param queryParam
     * @return
     */
    List<CommissionExcel>  findByParam(ReportRewardQuery queryParam);


}