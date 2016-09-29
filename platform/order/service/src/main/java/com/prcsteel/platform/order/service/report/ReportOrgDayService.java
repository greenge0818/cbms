
package com.prcsteel.platform.order.service.report;

import com.prcsteel.platform.order.model.datatable.Column;
import com.prcsteel.platform.order.model.dto.ReportOrgDayDto;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.order.model.query.ReportOrgDayQuery;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportOrgDayService
 * @Package com.prcsteel.platform.order.service.report
 * @Description: 服务中心日报服务
 * @date 2015/12/9
 */
public interface ReportOrgDayService {

    /**
     * 查询所有业务服务中心：如长沙、杭州 服务中心
     * @return
     */
    List<Organization> queryAllBusinessOrg();

    /**
     * 获得服务中心日报列头
     * @return
     */
    List<Column> getOrgDayReportColumns();

    /**
     * 通过订单添加所有服务中心每天的数据统计记录
     * 插入平台沉淀资金
     */
    void addOrgDayByOrderAndPrecipitatinFunds(Date startDate,Date endDate,SysSetting sysSetting);

    List<String> queryAllCalculateDateForPage(ReportOrgDayQuery query);

    List<Map<String,Object>> getOrgDayList(ReportOrgDayQuery query);

    void addOrgDayByOrder(Date startDate,Date endDate,SysSetting sysSetting);

    /**
     * 获得服务中心月报列头
     * @return
     */
    List<Column> getOrgMonthReportColumns();

    /**
     * 查询服务中心月报在某个时间段内有数据的所有日期集合
     * @param query
     * @return
     */
    List<String> queryMonthReportAllCalculateDateForPage(ReportOrgDayQuery query);

    List<Map<String,Object>> getOrgMonthList(ReportOrgDayQuery query);

}

