package com.prcsteel.platform.order.service.order;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.model.dto.ConsignOrderStatusDto;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.acl.model.dto.UserDto;
import com.prcsteel.platform.order.model.model.ConsignOrderProcess;

/**
 * Created by caochao on 2015/7/30.
 */
public interface ConsignOrderProcessService {

    List<ConsignOrderStatusDto> getOrderProcessByUserId(Long userId);

    /**
     * 查询所有内勤的ID
     * @return      ID List
     */
    List<Long> getOfficeStaffIds();
    
    /**
     * 获取所有已设业务员
     */
    List<UserDto> getAllBusinessMen(Map<String, Object> param);	
    /**
     * 获取所有未设业务员
     */
    List<UserDto> getUnsetBusinessMen();
    
    Integer getAllBusinessMenTotal(Map<String,Object> param);
    
    /**
     * 根据userId查询配置
     */
    List<ConsignOrderProcess> getProcessByUserId(Long userId);
    
    List<ConsignOrderProcess> getUnsetProcessByUserId(Long userId);
    
    ResultDto saveProcess(List<ConsignOrderProcess> list);
    
    ResultDto deleteByUserId(Long userId);
}
