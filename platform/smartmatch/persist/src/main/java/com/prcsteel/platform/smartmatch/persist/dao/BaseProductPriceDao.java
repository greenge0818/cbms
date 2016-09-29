package com.prcsteel.platform.smartmatch.persist.dao;

import com.prcsteel.platform.smartmatch.model.model.BaseProductPrice;

public interface BaseProductPriceDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BaseProductPrice record);

    int insertSelective(BaseProductPrice record);

    BaseProductPrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseProductPrice record);

    int updateByPrimaryKey(BaseProductPrice record);
    
    BaseProductPrice selectByProductId(Integer productId);
    
    int deleteByPrductId(Integer productId);
}