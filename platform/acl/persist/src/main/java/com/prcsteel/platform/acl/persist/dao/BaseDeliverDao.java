package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.acl.model.model.BaseDeliver;
import com.prcsteel.platform.acl.model.query.BaseDeliverQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseDeliverDao {
    int deleteByPrimaryKey(Long id);

    int insert(BaseDeliver record);

    int insertSelective(BaseDeliver record);

    BaseDeliver selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BaseDeliver record);

    int updateByPrimaryKey(BaseDeliver record);

    List<BaseDeliver> findByPrimary(BaseDeliverQuery record);

    BaseDeliver selectByPrima(@Param("name") String name);

}