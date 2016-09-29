package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.IcbcBdlHeader;

public interface IcbcBdlHeaderDao {
    int deleteByPrimaryKey(Long id);

    int insert(IcbcBdlHeader record);

    int insertSelective(IcbcBdlHeader record);

    IcbcBdlHeader selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IcbcBdlHeader record);

    int updateByPrimaryKey(IcbcBdlHeader record);
}