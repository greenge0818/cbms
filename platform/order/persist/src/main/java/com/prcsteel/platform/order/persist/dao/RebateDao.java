package com.prcsteel.platform.order.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.model.dto.RebateDto;
import com.prcsteel.platform.acl.model.model.Rebate;

public interface RebateDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Rebate record);

    int insertSelective(Rebate record);

    Rebate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Rebate record);

    int updateByPrimaryKey(Rebate record);
    
    int cancelRebateEffect();
    
    List<Rebate> queryAll(Rebate rebate);
    /**
     * 获取返利
     * @param paramMap
     * @return
     */
    public List<RebateDto> queryRebateDto(Map<String, Object> paramMap);
   /**
    * 解除到期的返利方案
    */
    public void expireOldRebate();
    /**
     * 让新的返利方案生效
     */
    public void startNewRebate();
    /**
     * 统计已经到期的还未生效的提成条数
     * @return
     */
     public int countNextMonthEffectRecord();
}