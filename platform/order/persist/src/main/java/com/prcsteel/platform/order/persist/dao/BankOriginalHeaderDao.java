package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.BankOriginalHeader;

public interface BankOriginalHeaderDao {

    public int insert(BankOriginalHeader bankOriginalHeader);

    public int insertSelective(BankOriginalHeader bankOriginalHeader);

    public BankOriginalHeader selectByPrimaryKey(Long id);

    public int updateByPrimaryKeySelective(BankOriginalHeader bankOriginalHeader);

    public int updateByPrimaryKey(BankOriginalHeader bankOriginalHeader);
}