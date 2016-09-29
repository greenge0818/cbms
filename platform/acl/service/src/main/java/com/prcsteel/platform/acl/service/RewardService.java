package com.prcsteel.platform.acl.service;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.acl.model.dto.RewardDto;
import com.prcsteel.platform.acl.model.model.Reward;

/**
 * Created by chenchen Mao on 2015/8/3.
 */
public interface RewardService {
 /**
  * 添加新提成
  * @param reward
  * @return
  */
 public Integer addReward(Reward reward);
 /**
  * 将所有未生效的提成改为失效
  * @return
  */
 public int cancleReward();
 /**
  * 获取所有正在生效的提成方案
  * @return
  */
 public List<Reward> getAllReward();
 /**
  * 获取所有卖家提成系数、区间
  * @return
  */
 public List<Reward>getAllRadio();

 /**
  * 获取所有正在生效的提成方案
  * @return
  */
 public List<RewardDto> getAllRewardDto();

 /**
  * 获取佣金标准
  * @return
  */
 public List<Reward> getCommissionStandard();


 /**
  * 重置新的提成方案
  * @param
  * @return
  */
 public  int refleshReward(List<Reward> rewardList, Map<String, String> reward_rate,Reward rewardComm, String user);

 /**
  * 启用新的提成方案
  * @return
  */
 public void startNewReward();

 /**
  * 统计已经到期的还未生效的提成条数
  * @return
  */
 public int countNextMonthEffectRecord();

   public double findRewardRole(String name);

 /**
  * 通过卖家客户查看是临采   还是品牌店
  *
  */
 public String findConsignType(String name);

 /**
  * 获取 交易参数
  * @return
  */

 public List<Reward> getDealParameter();

 /**
  * 获取 单笔交易参数
  * @return
  */
 
 public Reward getDealParameter(Integer userId,String supplierLabel);

 /**
  * 风控  交易参数设置：
  * @param rewardList
  * @param user
  * @return
  */
 int perfectSysSetting(List<Reward> rewardList ,String user);
 /**
  * 删除 交易参数
  */
 Integer deleSysSetting();

 Integer addSysSetting(Reward reward,String user);


}
