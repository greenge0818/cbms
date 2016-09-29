package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.dto.ReportOrgDayDto;
import com.prcsteel.platform.order.model.model.ReportOrgDay;
import com.prcsteel.platform.order.model.query.ReportOrgDayQuery;

import java.util.List;

public interface ReportOrgDayDao {
    int deleteByPrimaryKey(Long id);

    int insert(ReportOrgDay record);

    int insertSelective(ReportOrgDay record);

    ReportOrgDay selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportOrgDay record);

    int updateByPrimaryKey(ReportOrgDay record);

    /**
     * 查询服务中心关联 订单销售额（合同关联金额）、重量之和、笔数之和、代运营交易笔数之和
     * @param query
     * @return
     */
    List<ReportOrgDay> queryOrgReletedOrderData(ReportOrgDayQuery query);

    /**
     * 查询服务中心关联订单采购金额 （初次付款）
     * @param query
     * @return
     */
    List<ReportOrgDay> queryOrgReletedOrderPurchaseAmount(ReportOrgDayQuery query);

    /**
     * 查询服务中心二结订单销售结算额、采购结算额、重量之和、笔数之和、代运营交易笔数之和（二次结算的实提重量 实提金额）
     * @param query
     * @return
     */
    List<ReportOrgDay> queryOrgSecondOrderData(ReportOrgDayQuery query);

    /**
     * 查询截止到当天各服务中心所有已签约的卖家数
     * @return
     */
    List<ReportOrgDay> queryOrgConsignSellerTotal();


    /**
     * 查询服务中心日报在某个时间段内有数据的所有日期集合
     * @param query
     * @return
     */
    List<String> queryAllCalculateDateForPage(ReportOrgDayQuery query);

    List<ReportOrgDayDto>  queryByParam(ReportOrgDayQuery query);

    List<ReportOrgDayDto>  queryTotalGroupByOrgByParam(ReportOrgDayQuery query);

    /**
     * 查询服务中心月报在某个时间段内有数据的所有日期集合
     * @param query
     * @return
     */
    List<String> queryMonthReportAllCalculateDateForPage(ReportOrgDayQuery query);

    List<ReportOrgDayDto> queryMonthByParam(ReportOrgDayQuery query);

    List<ReportOrgDayDto> queryMonthTotalGroupByOrgByParam(ReportOrgDayQuery query);




}