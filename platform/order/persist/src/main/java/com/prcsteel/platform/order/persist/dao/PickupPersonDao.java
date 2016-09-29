package com.prcsteel.platform.order.persist.dao;

import java.util.List;

import com.prcsteel.platform.order.model.model.PickupPerson;

public interface PickupPersonDao {
    int deleteByPrimaryKey(Long id);

    int insert(PickupPerson record);

    int insertSelective(PickupPerson record);

    PickupPerson selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PickupPerson record);

    int updateByPrimaryKey(PickupPerson record);

    List<PickupPerson> selectByBillId(Long billid);
    
    int deleteByBillId(Long billId);
}