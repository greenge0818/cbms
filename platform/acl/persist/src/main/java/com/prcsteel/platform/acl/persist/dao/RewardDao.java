package com.prcsteel.platform.acl.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.acl.model.dto.RewardDto;
import com.prcsteel.platform.acl.model.model.Reward;
import org.apache.ibatis.annotations.Param;

public interface RewardDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Reward record);

    int insertSelective(Reward record);

    Reward selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Reward record);

    int updateByPrimaryKey(Reward record);
    int cancelReward();
    List<Reward> queryAll(Map<String, Object> paramMap);
    /**
     * 获取
     * @param paramMap
     * @return
     */
    public List<RewardDto> queryRewardDto(Map<String, Object> paramMap);
    /**
     * 解除到期的提成方案
     */
     public void expireOldReward();
     /**
      * 让新的返利提成生效
      */
     public void startNewReward();
     /**
      * 统计已经到期的还未生效的提成条数
      * @return
      */
      public int countNextMonthEffectRecord();

    /**
     *
     * 通过大类 查找reward_role
     * @param name
     * @return
     */
     public  double  findRewardRole(@Param("name") String name);

    /**
     * 通过卖家客户查看是临采   还是品牌店
     *
     */
    public String findConsignType(@Param("name") String name);
    /**
     * 查找佣金标准
     *
     */
    public  List<Reward> getCommissionStandard();


    /**
     * 获取RewardDto
     * @param
     * @return
     */
    public RewardDto queryReward();

    /**
     * 获取买家累积采购量区间
     * @param
     * @return
     */
    public List<RewardDto> queryRewardDtoList();

    public  List<Reward> getDealParameter();
    
    public Reward getOneDealParameter(@Param("userId")Integer userId,@Param("supplierLabel")String supplierLabel);

    int deleSysSetting();
}