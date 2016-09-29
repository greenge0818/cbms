package com.prcsteel.platform.acl.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.acl.service.UserOrgService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.acl.model.dto.UserDto;
import com.prcsteel.platform.acl.model.dto.UserOrgsDto;
import com.prcsteel.platform.acl.model.model.UserOrg;
import com.prcsteel.platform.acl.persist.dao.UserOrgDao;

/**
 * Created by Green.Ge on 2015/10/21.
 */

@Service("UserOrgService")
public class UserOrgServiceImpl implements UserOrgService {
	@Resource
	UserOrgDao userOrgDao;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public List<UserOrgsDto> getByUserId(Long userId) {
		return userOrgDao.getByUserId(userId);
	}

	@Override
	public List<UserDto> getSetUser(Map<String, Object> param) {
		return userOrgDao.getSetUser(param);
	}

	@Override
	public List<UserDto> getUnsetUser() {
		return userOrgDao.getUnsetUser();
	}

	@Override
	public Integer getSetUserTotal(Map<String, Object> param) {
		return userOrgDao.getSetUserTotal(param);
	}

	@Override
	public List<UserOrg> getConfigByUserId(Long userId) {
		return userOrgDao.getConfigByUserId(userId);
	}

	@Override
	public ResultDto saveProcess(List<UserOrg> list) {
		ResultDto result = new ResultDto();
		if(list==null||list.isEmpty()){
			result.setSuccess(false);
			result.setMessage("没有配置需要保存");
			return result;
		}
		Long userId = list.get(0).getUserId();
		try{
		userOrgDao.deleteByUserId(userId);
		String loginId =(String)SecurityUtils.getSubject().getPrincipal();
		for(UserOrg uo:list){
			uo.setCreatedBy(loginId);
			uo.setLastUpdatedBy(loginId);
			userOrgDao.insert(uo);
		}
		}catch(Exception e){
			logger.error("error when saving user org info",e.getMessage());
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN,"保存时发生错误");
		}
		result.setSuccess(true);
		return result;
	}

	@Override
	public ResultDto deleteByUserId(Long userId) {
		ResultDto result = new ResultDto();
		try{
			int i = userOrgDao.deleteByUserId(userId);
			if(i>0){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
				result.setMessage("未删除任何数据");
			}
		}catch(Exception e){
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "删除用户配置发生异常");
		}
		
		return result;
	}
	
	@Override
	public List<UserOrgsDto> getConfigOrgsByUserId(Long userId) {
		// TODO Auto-generated method stub
		return userOrgDao.getConfigOrgsByUserId(userId);
	}
	@Override
	public List<UserOrgsDto> getConfigBusinessOrgByUserId(Long userId) {
		// TODO Auto-generated method stub
		return userOrgDao.getConfigBusinessOrgByUserId(userId);
	}
    
    
}