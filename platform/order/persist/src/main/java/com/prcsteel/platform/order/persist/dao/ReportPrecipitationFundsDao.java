package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.dto.ReportPrecipitationFundsDto;
import com.prcsteel.platform.order.model.model.ReportPrecipitationFunds;
import com.prcsteel.platform.order.model.query.ReportOrgDayQuery;

import java.util.List;

public interface ReportPrecipitationFundsDao {
    int deleteByPrimaryKey(Long id);

    int insert(ReportPrecipitationFunds record);

    int insertSelective(ReportPrecipitationFunds record);

    ReportPrecipitationFunds selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportPrecipitationFunds record);

    int updateByPrimaryKey(ReportPrecipitationFunds record);

    List<ReportPrecipitationFundsDto> queryByParam(ReportOrgDayQuery query);
}