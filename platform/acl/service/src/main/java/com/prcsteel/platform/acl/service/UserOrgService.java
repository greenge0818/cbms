package com.prcsteel.platform.acl.service;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.acl.model.dto.UserDto;
import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.model.dto.UserOrgsDto;
import com.prcsteel.platform.acl.model.model.UserOrg;

/**
 * Created by Green.Ge on 2015/10/21.
 */
public interface UserOrgService {

    List<UserOrgsDto> getByUserId(Long userId);

    /**
     * 获取所有已设用户
     */
    List<UserDto> getSetUser(Map<String, Object> param);	
    /**
     * 获取所有未设用户
     */
    List<UserDto> getUnsetUser();
    
    Integer getSetUserTotal(Map<String,Object> param);
    
    /**
     * 根据userId查询配置
     */
    List<UserOrg> getConfigByUserId(Long userId);
    
    ResultDto saveProcess(List<UserOrg> list);
    
    ResultDto deleteByUserId(Long userId);

	List<UserOrgsDto> getConfigOrgsByUserId(Long userId);
    List<UserOrgsDto> getConfigBusinessOrgByUserId(Long userId);
}
