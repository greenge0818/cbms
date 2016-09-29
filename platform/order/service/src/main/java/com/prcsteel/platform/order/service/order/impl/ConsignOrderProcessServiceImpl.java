package com.prcsteel.platform.order.service.order.impl;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.common.exception.BusinessException;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.order.model.dto.ConsignOrderStatusDto;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.acl.model.dto.UserDto;
import com.prcsteel.platform.order.model.model.ConsignOrderProcess;
import com.prcsteel.platform.order.persist.dao.ConsignOrderProcessDao;
import com.prcsteel.platform.order.service.order.ConsignOrderProcessService;

/**
 * Created by caochao on 2015/7/30.
 */

@Service("consignOrderProcessService")
public class ConsignOrderProcessServiceImpl implements ConsignOrderProcessService {
    @Autowired
    ConsignOrderProcessDao consignOrderProcessDao;

    @Override
    public List<ConsignOrderStatusDto> getOrderProcessByUserId(Long userId) {
        return consignOrderProcessDao.getOrderProcessByUserId(userId);
    }

    /**
     *
     * @return      ID List
     */
    @Override
    public List<Long> getOfficeStaffIds(){
        return consignOrderProcessDao.getOfficeStaffIds();
    }

	@Override
	public List<UserDto> getAllBusinessMen(Map<String, Object> param) {
		return consignOrderProcessDao.getAllBusinessMen(param);
	}

	@Override
	public Integer getAllBusinessMenTotal(Map<String, Object> param) {
		return consignOrderProcessDao.getAllBusinessMenTotal(param);
	}

	@Override
	public List<ConsignOrderProcess> getProcessByUserId(Long userId) {
		return consignOrderProcessDao.getSetProcessByUserId(userId);
	}

	@Override
	public ResultDto saveProcess(List<ConsignOrderProcess> list) {
		ResultDto result = new ResultDto();
		if(list==null||list.isEmpty()){
			result.setSuccess(false);
			result.setMessage("没有配置需要保存");
		}
		String loginId =(String)SecurityUtils.getSubject().getPrincipal();
		for(ConsignOrderProcess cop:list){
			if(cop.getId()==null){
				cop.setCreatedBy(loginId);
				cop.setLastUpdatedBy(loginId);
				consignOrderProcessDao.insert(cop);
			}else{
				cop.setLastUpdatedBy(loginId);
				consignOrderProcessDao.updateByPrimaryKey(cop);
			}
		}
		result.setSuccess(true);
		return result;
	}

	@Override
	public List<UserDto> getUnsetBusinessMen() {
		return consignOrderProcessDao.getUnsetBusinessMen();
	}

	@Override
	public List<ConsignOrderProcess> getUnsetProcessByUserId(Long userId) {
		return consignOrderProcessDao.getUnsetProcessByUserId(userId);
	}

	@Override
	public ResultDto deleteByUserId(Long userId) {
		ResultDto result = new ResultDto();
		try{
			int i = consignOrderProcessDao.deleteByUserId(userId);
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
    
    
    
}