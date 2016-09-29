package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.WithDraw;

public interface WithDrawDao {
    int deleteByPrimaryKey(Integer id);

    int insert(WithDraw record);

    int insertSelective(WithDraw record);

    WithDraw selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WithDraw record);

    Integer queryMaxSyncId();
}