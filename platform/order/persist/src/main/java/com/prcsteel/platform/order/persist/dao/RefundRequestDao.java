package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.RefundRequest;

public interface RefundRequestDao {
    int deleteByPrimaryKey(Long id);

    int insert(RefundRequest record);

    int insertSelective(RefundRequest record);

    RefundRequest selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RefundRequest record);

    int updateByPrimaryKey(RefundRequest record);

    RefundRequest selectByBankSerialNumber(String bankSerialNumber);
}