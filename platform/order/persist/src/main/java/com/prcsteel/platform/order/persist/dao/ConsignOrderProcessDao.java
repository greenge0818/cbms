package com.prcsteel.platform.order.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.common.constants.Constant;
import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.ConsignOrderStatusDto;
import com.prcsteel.platform.acl.model.dto.UserDto;
import com.prcsteel.platform.order.model.model.ConsignOrderProcess;
import org.springframework.cache.annotation.Cacheable;


public interface ConsignOrderProcessDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsignOrderProcess record);

    int insertSelective(ConsignOrderProcess record);

    ConsignOrderProcess selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsignOrderProcess record);

    int updateByPrimaryKey(ConsignOrderProcess record);

    @Cacheable(value = Constant.CACHE_NAME, key="'"+ Constant.CACHE_ORDER_PROCESS_BY_USERId +"' + #p0")
    List<ConsignOrderStatusDto> getOrderProcessByUserId(Long userId);
    
    ConsignOrderStatusDto getByUserIdAndStatus(@Param("userId") Long userId,@Param("status") String status);

    /**
     * 查询所有内勤的ID
     * @return      ID List
     */
    @Cacheable(value = Constant.CACHE_NAME, key="'" + Constant.CACHE_All_OFFICE_STAFF + "'")
    List<Long> getOfficeStaffIds();
    /**
     * 获取所有已设业务员
     */
    List<UserDto> getAllBusinessMen(Map<String,Object> param);	
    
    /**
     * 获取所有未设业务员
     */
    List<UserDto> getUnsetBusinessMen();
    
    List<ConsignOrderProcess> getSetProcessByUserId(Long userId);
    
    Integer getAllBusinessMenTotal(Map<String,Object> param);
    
    List<ConsignOrderProcess> getUnsetProcessByUserId(Long userId);
    
    Integer deleteByUserId(Long UserId);
    //获取已关联银票对应操作
    List<ConsignOrderProcess> getRelateProcessByUserId(Long userId);
}