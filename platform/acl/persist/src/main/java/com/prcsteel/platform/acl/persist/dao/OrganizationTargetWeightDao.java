package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.acl.model.dto.TargetWeightForUpdateDto;
import com.prcsteel.platform.acl.model.model.OrganizationTargetWeight;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author peanut
 * @version 1.0
 * @description 服务中心目标交易量表Dao
 * @date 2016/4/8 10:22
 */

 // Created by zhoucai@prcsteel.com 移植风控上线代码 on 2016/5/6.

@Repository
public interface OrganizationTargetWeightDao{
    int deleteByPrimaryKey(Long id);

    int insert(OrganizationTargetWeight record);

    int insertSelective(OrganizationTargetWeight record);

    OrganizationTargetWeight selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrganizationTargetWeight record);

    int updateByPrimaryKey(OrganizationTargetWeight record);

    /**
     * 根据年份查询在用服务中心目标交易量
     * @param years   年份
     * @return
     */
    List<OrganizationTargetWeight> getWeightByYears(@Param("years") String years);

    /**
     * 批量更新交易量
     * @param list
     * @return
     */
    int doBatchUpdateWeight(List<TargetWeightForUpdateDto> list);
    
    OrganizationTargetWeight selectByUserId(@Param("years")String years,@Param("userId")Integer userId);
}
