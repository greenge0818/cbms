package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.dto.ReportAccountFundDetailDto;
import com.prcsteel.platform.order.model.dto.ReportAccountFundDto;
import com.prcsteel.platform.order.model.model.ReportAccountFinancial;
import com.prcsteel.platform.order.model.query.ReportAccountFundQuery;

import java.math.BigDecimal;
import java.util.List;

public interface ReportAccountFinancialDao {
    int deleteByPrimaryKey(Long id);

    int insert(ReportAccountFinancial record);

    int insertSelective(ReportAccountFinancial record);

    ReportAccountFinancial selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportAccountFinancial record);

    int updateByPrimaryKey(ReportAccountFinancial record);

    List<ReportAccountFundDetailDto> queryAccountFinancialDetailByParam(ReportAccountFundQuery query);

    int queryAccountFinancialDetailTotalByParam(ReportAccountFundQuery query);

    BigDecimal queryAccountInitialBalance(ReportAccountFundQuery query);

    BigDecimal queryAccountEndingBalance(ReportAccountFundQuery query);

    ReportAccountFundDetailDto sumAccountFinancial(ReportAccountFundQuery query);

    List<ReportAccountFundDto> queryAccountFinancialByParam(ReportAccountFundQuery query);

    List<ReportAccountFundDto> queryAccountEndingBalanceList(ReportAccountFundQuery query);

    int queryAccountFinancialTotalByParam(ReportAccountFundQuery query);

}