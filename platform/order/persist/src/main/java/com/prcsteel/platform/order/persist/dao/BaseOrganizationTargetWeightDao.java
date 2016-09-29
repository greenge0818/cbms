package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.dto.TargetWeightForUpdateDto;
import com.prcsteel.platform.order.model.model.BaseOrganizationTargetWeight;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author peanut
 * @version 1.0
 * @description 服务中心目标交易量表Dao
 * @date 2016/4/8 10:22
 */
@Repository
public interface BaseOrganizationTargetWeightDao{
    int deleteByPrimaryKey(Long id);

    int insert(BaseOrganizationTargetWeight record);

    int insertSelective(BaseOrganizationTargetWeight record);

    BaseOrganizationTargetWeight selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BaseOrganizationTargetWeight record);

    int updateByPrimaryKey(BaseOrganizationTargetWeight record);

    /**
     * 根据年份查询在用服务中心目标交易量
     * @param years   年份
     * @return
     */
    List<BaseOrganizationTargetWeight> getWeightByYears(@Param("years") String years);

    /**
     * 批量更新交易量
     * @param list
     * @return
     */
    int doBatchUpdateWeight(List<TargetWeightForUpdateDto> list);
    
    BaseOrganizationTargetWeight selectByUserId(@Param("years")String years,@Param("userId")Integer userId);
}
