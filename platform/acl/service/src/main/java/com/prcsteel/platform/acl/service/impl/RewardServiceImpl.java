package com.prcsteel.platform.acl.service.impl;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.acl.model.model.SysSetting;import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.acl.model.enums.RewardType;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.acl.model.dto.RewardDto;
import com.prcsteel.platform.common.enums.OperateStatus;
import com.prcsteel.platform.acl.model.enums.RewardStatus;
import com.prcsteel.platform.acl.model.model.Reward;
import com.prcsteel.platform.acl.persist.dao.RewardDao;
import com.prcsteel.platform.acl.service.RewardService;

import javax.annotation.Resource;

/**
 * Created by chenchen on 2015/8/3.
 */
@Service("rewardService")
public class RewardServiceImpl implements RewardService {
	private static final Logger logger = Logger.getLogger(RewardServiceImpl.class);
	@Resource
	RewardDao rewardDao;
	

	Calendar calendar;

	public Date calcDate() {
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	@Override
	public Integer addReward(Reward reward) {
		Integer flag = OperateStatus.FAIL.ordinal();
		reward.setCreated(new Date());
		reward.setLastUpdated(new Date());
		reward.setModificationNumber(0);
		reward.setRewardStatus(RewardStatus.EFFECT_NEXT_MONTH.getName());
		/*
		 * 设置下个月1号生效
		 */
		reward.setEffectiveTime(calcDate());
		if (rewardDao.insert(reward) > 0) {
			flag = OperateStatus.SUCCESS.ordinal();
		} else {
			flag = OperateStatus.DUPLICATE.ordinal();
		}
		return flag;
	}

	@Override
	public int cancleReward() {
		return rewardDao.cancelReward();
	}

	@Override
	public List<Reward> getAllReward() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("rewardStatus", RewardStatus.EFFECT.getName());
		paramMap.put("rewardType", RewardType.ORDER.getCode());
		return rewardDao.queryAll(paramMap);
	}
	@Override
	public List<Reward> getAllRadio() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("rewardStatus", RewardStatus.EFFECT.getName());
		paramMap.put("rewardType", RewardType.RADIO.getCode());
		return rewardDao.queryAll(paramMap);
	}
	@Override
	public List<Reward> getCommissionStandard() {
		return  rewardDao.getCommissionStandard();
	}






	@Override
	public List<RewardDto> getAllRewardDto() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("rewardStatus", RewardStatus.EFFECT.getName());
		return this.rewardDao.queryRewardDto(paramMap);
	}

	@Override
	public int refleshReward(List<Reward> rewardList, Map<String, String> reward_rate,Reward rewardComm, String user) {
		
		int flag=this.cancleReward();
		rewardComm.setRewardType(RewardType.NORM.getCode());
		rewardComm.setCreatedBy(user);
		rewardComm.setLastUpdatedBy(user);
		if(!addReward(rewardComm).equals(OperateStatus.SUCCESS.ordinal())){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "设置佣金标准失败");
		}
		
		for (Reward reward : rewardList) {
			if(!addReward(reward).equals(OperateStatus.SUCCESS.ordinal())){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "设置新成交量提成失败");
			}
		}
		for(String key : reward_rate.keySet()){
			Reward reward = new Reward();
			reward.setRewardType(RewardType.ORDER.getCode());
			reward.setCategoryUuid(key);
			reward.setRewardRole(BigDecimal.valueOf(Double.valueOf(reward_rate.get(key))));
			reward.setCreatedBy(user);
			reward.setLastUpdatedBy(user);
			if(!addReward(reward).equals(OperateStatus.SUCCESS.ordinal())){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "设置新提成比例失败");
			}
		}
		return flag;
	}

	@Override
	public void startNewReward() {
		try {
			this.rewardDao.expireOldReward();
			this.rewardDao.startNewReward();
		} catch (Exception e) {
			logger.error("start new reward error");
			
		}
	}

	@Override
	public int countNextMonthEffectRecord() {
		
		return rewardDao.countNextMonthEffectRecord();
	}
	@Override
	public double  findRewardRole(String name){
		return rewardDao.findRewardRole(name);
	}

	/**
	 * 通过卖家客户查看是临采   还是品牌店
	 *
	 */
	@Override
	public String findConsignType(String name){
		return rewardDao.findConsignType(name);
	}

	@Override
	public List<Reward> getDealParameter(){
		return rewardDao.getDealParameter();

	}
	@Override
	public int perfectSysSetting(List<Reward> rewardList, String user){
		int flag=this.deleSysSetting();
		for(Reward reward:rewardList) {
			if (!addSysSetting(reward, user).equals(com.prcsteel.platform.common.enums.OperateStatus.SUCCESS.ordinal())) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "设置超期时间设置失败！");
			}
		}
		return flag;
	}

	@Override
	public Integer deleSysSetting() {
		Integer flag = com.prcsteel.platform.common.enums.OperateStatus.FAIL.ordinal();
		if (rewardDao.deleSysSetting() > 0) {
			flag = com.prcsteel.platform.common.enums.OperateStatus.SUCCESS.ordinal();
		} else {
			flag = com.prcsteel.platform.common.enums.OperateStatus.DUPLICATE.ordinal();
		}
		return flag;
	}

	@Override
	public Integer addSysSetting(Reward reward,String user) {
		Integer flag = OperateStatus.FAIL.ordinal();
		reward.setCreated(new Date());
		reward.setCreatedBy(user);
		reward.setLastUpdated(new Date());
		reward.setLastUpdatedBy(user);
		reward.setModificationNumber(0);
		reward.setRewardStatus("DEAL_PARAMETER");
		if (rewardDao.insertSelective(reward) > 0) {
			flag = OperateStatus.SUCCESS.ordinal();
		} else {
			flag = OperateStatus.DUPLICATE.ordinal();
		}
		return flag;
	}

	@Override
	public Reward getDealParameter(Integer userId, String supplierLabel) {
		// TODO Auto-generated method stub
		return rewardDao.getOneDealParameter(userId,supplierLabel);
	}


}
