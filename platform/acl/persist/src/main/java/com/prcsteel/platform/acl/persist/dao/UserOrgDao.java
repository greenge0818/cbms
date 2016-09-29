package com.prcsteel.platform.acl.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.acl.model.dto.UserDto;
import com.prcsteel.platform.acl.model.dto.UserOrgsDto;
import com.prcsteel.platform.acl.model.model.UserOrg;

/**
 * Created by Green.Ge on 2015/10/21.
 */
public interface UserOrgDao {
	int deleteByPrimaryKey(Long id);
	int deleteByUserId(Long userId);
    int insert(UserOrg record);

    int insertSelective(UserOrg record);
    
    UserOrg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserOrg record);

    int updateByPrimaryKey(UserOrg record);
    
    List<UserOrgsDto> getByUserId(Long userId);

    /**
     * 获取所有已设用户
     */
    List<UserDto> getSetUser(Map<String, Object> param);	
    /**
     * 获取所有未设用户
     */
    List<UserDto> getUnsetUser();
    
    Integer getSetUserTotal(Map<String, Object> param);
    
    /**
     * 根据userId查询配置
     */
    List<UserOrg> getConfigByUserId(Long userId);
    
    /**
     * 根据userId查询配置 得到更详细的信息
     * @param userId
     * @return
     */
	List<UserOrgsDto> getConfigOrgsByUserId(Long userId);


    /**
     * 查询当前用户所在的服务中心 add by wangxianjun
     * @param userId
     * @return
     */
    List<UserOrgsDto> getConfigBusinessOrgByUserId(Long userId);
    
}
