package com.prcsteel.platform.order.persist.dao;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.model.InvoiceOutBalanceMonthly;


public interface InvoiceOutBalanceMonthlyDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceOutBalanceMonthly record);

    int insertSelective(InvoiceOutBalanceMonthly record);

    InvoiceOutBalanceMonthly selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceOutBalanceMonthly record);

    int updateByPrimaryKey(InvoiceOutBalanceMonthly record);

    /**
     * 根据服务中心查询每月销项票余额记录
     * @param orgId
     * @param month
     * @return
     */
    InvoiceOutBalanceMonthly queryByOrgIdAndMonth(@Param("orgId") Long orgId, @Param("month") String month);
}