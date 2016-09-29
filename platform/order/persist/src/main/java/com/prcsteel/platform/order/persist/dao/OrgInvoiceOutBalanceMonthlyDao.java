package com.prcsteel.platform.order.persist.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.model.OrgInvoiceOutBalanceMonthly;

public interface OrgInvoiceOutBalanceMonthlyDao {

    public List<OrgInvoiceOutBalanceMonthly> queryByMonthAndOrgId(@Param("month") String month, @Param("orgId") Long orgId);

    public int updateInvoiceOutBalance(@Param("invoiceOutBalance") BigDecimal invoiceOutBalance, @Param("month") String month, @Param("orgId") Long orgId);
}