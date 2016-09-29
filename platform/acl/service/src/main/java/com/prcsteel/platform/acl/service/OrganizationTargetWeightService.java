package com.prcsteel.platform.acl.service;

import java.math.BigDecimal;
import java.util.List;

import com.prcsteel.platform.acl.model.dto.TargetWeightForUpdateDto;
import com.prcsteel.platform.acl.model.model.OrganizationTargetWeight;

/**
 * @author peanut
 * @version 1.0
 * @description  服务中心目标交易量service
 * @date 2016/4/8 11:21
 */
// Created by zhoucai@prcsteel.com 移植风控上线代码 on 2016/5/6.
public interface OrganizationTargetWeightService {

    /**
     * 根据年份获取交易量
     * @param year
     */
    List<OrganizationTargetWeight> getWeightByYear(String year);

    /**
     * 批量更新交易量
      * @param list
     * @return
     */
    int doBatchUpdateWeight(List<TargetWeightForUpdateDto> list);
    
    BigDecimal  selectByUserId(Integer userId);
   
}
