package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.BaseDict;

public interface BaseDictDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BaseDict record);

    int insertSelective(BaseDict record);

    BaseDict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseDict record);

    int updateByPrimaryKey(BaseDict record);
    
    BaseDict selectByKey(String key);
}