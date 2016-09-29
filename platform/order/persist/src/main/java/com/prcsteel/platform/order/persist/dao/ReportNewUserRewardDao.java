package com.prcsteel.platform.order.persist.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.order.model.dto.ReportNewUserRewardDto;
import com.prcsteel.platform.order.model.model.ReportNewUserReward;
import com.prcsteel.platform.order.model.query.ReportRewardQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheEvict;

public interface ReportNewUserRewardDao {
    int deleteByPrimaryKey(Long id);

    int insert(ReportNewUserReward record);

    int insertSelective(ReportNewUserReward record);

    ReportNewUserReward selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportNewUserReward record);

    int updateByPrimaryKey(ReportNewUserReward record);
    
    ReportNewUserRewardDto queryReportNewUserReward(Map<String, Object> paramMap);
    
    ReportNewUserRewardDto queryReportNewUserRewardByManager(Map<String, Object> paramMap);

    /**
     * 查询服务中心买家卖家新增用户数提成信息
     * @author dengxiyan
     * @param queryParam
     * @return
     */
    List<ReportNewUserReward> queryNewUserRewardByOrgId(ReportRewardQuery queryParam);

    /**
     * 查询交易员买家卖家新增用户数提成信息
     * @author dengxiyan
     * @param queryParam
     * @return
     */
    List<ReportNewUserReward> queryManagerNewUserReward(ReportRewardQuery queryParam);

    /**
     * 按照客户id查询提成
     * @author rabbit
     * @return
     */
    ReportNewUserReward queryByAccountId(Long accountId);

    /**
     * 使新增用户提成生效
     * @author rabbit
     * @return
     */
    Integer deleteByAccountId(
                   @Param("accountId") Long accountId,
                   @Param("lastUpdatedBy") String lastUpdatedBy,
                   @Param("oldModificationNumber") Integer oldModificationNumber);

	List<ReportNewUserReward> queryByAccountAndOpenDate(ReportRewardQuery query);

}