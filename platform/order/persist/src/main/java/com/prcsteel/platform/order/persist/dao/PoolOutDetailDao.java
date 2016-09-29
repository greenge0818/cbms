package com.prcsteel.platform.order.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.model.dto.PoolInAndOutModifier;
import com.prcsteel.platform.order.model.dto.PoolOutDetailDto;
import com.prcsteel.platform.order.model.dto.PoolOutDetailModifier;
import com.prcsteel.platform.order.model.model.PoolOutDetail;

public interface PoolOutDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(PoolOutDetail record);

    int insertSelective(PoolOutDetail record);

    PoolOutDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PoolOutDetail record);

    int updateByPrimaryKey(PoolOutDetail record);

    List<PoolOutDetail> queryByBuyerAndDetails(Map<String, Object> paramMap);

    int modifyPoolOutDetail(PoolInAndOutModifier modifier);
    
    List<PoolOutDetail> selectByPoolOutId (PoolOutDetailDto poolOutDetailDto);
    
    int updatePoolOutDetail (PoolOutDetailModifier poolOutDetailModifier);
}