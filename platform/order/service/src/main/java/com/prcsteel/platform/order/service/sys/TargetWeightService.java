package com.prcsteel.platform.order.service.sys;

import java.math.BigDecimal;
import java.util.List;

import com.prcsteel.platform.order.model.dto.TargetWeightForUpdateDto;
import com.prcsteel.platform.order.model.model.BaseOrganizationTargetWeight;

/**
 * @author peanut
 * @version 1.0
 * @description  服务中心目标交易量service
 * @date 2016/4/8 11:21
 */
public interface TargetWeightService {

    /**
     * 根据年份获取交易量
     * @param year
     */
    List<BaseOrganizationTargetWeight> getWeightByYear(String year);

    /**
     * 批量更新交易量
      * @param list
     * @return
     */
    int doBatchUpdateWeight(List<TargetWeightForUpdateDto> list);
    
    BigDecimal  selectByUserId(Integer userId);
   
}
