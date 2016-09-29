package com.prcsteel.platform.order.service.report.impl;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.order.model.datatable.Column;
import com.prcsteel.platform.order.model.dto.ReportOrgDayDto;
import com.prcsteel.platform.order.model.dto.ReportPrecipitationFundsDto;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.enums.ReportDayRowName;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.order.model.model.ReportOrgDay;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.order.model.query.ReportOrgDayQuery;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.order.persist.dao.ReportOrgDayDao;
import com.prcsteel.platform.order.service.report.ReportOrgDayService;
import com.prcsteel.platform.order.service.report.ReportPrecipitationFundsService;
import com.prcsteel.platform.acl.service.SysSettingService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportOrgDayServiceImpl
 * @Package com.prcsteel.platform.order.service.report.impl
 * @Description: 服务中心日报服务
 * @date 2015/12/9
 */
@Service("reportOrgDayService")
public class ReportOrgDayServiceImpl implements ReportOrgDayService {

    @Resource
    OrganizationDao organizationDao;

    @Resource
    ReportOrgDayDao reportOrgDayDao;

    @Resource
    ReportPrecipitationFundsService reportPrecipitationFundsService;


    @Resource
    SysSettingService sysSettingService;

    @Value("${quartz.job.spdb.systemId}")
    private String systemId;

    private static final Logger logger = Logger.getLogger(ReportOrgDayServiceImpl.class);

    private static final String ORG_PRDFIX = "org_";
    private static final String COL_NAME_CALCULATE_DATE = "calculateDate";
    private static final String COL_NAME_TRADEDATA = "tradeData";
    private static final String COL_NAME_FUNDS = "funds";
    private static final String TYPE_DAY = "day";
    private static final String TYPE_MONTH = "month";
    private static final String TOTAL_ROW_DATE = "合计";


    /**
     * 通过订单添加所有服务中心每天的数据统计记录
     * 插入平台沉淀资金(当前执行时间最新的)
     */
    @Transactional
    public void addOrgDayByOrderAndPrecipitatinFunds(Date startDate, Date endDate,SysSetting sysSetting) {

        //插入服务中心日报数据
        addOrgDayCommonByOrder(startDate, endDate);

        //插入平台资金
        reportPrecipitationFundsService.add();

        //更新配置
        int count = sysSettingService.updateBySettingTypeSelective(sysSetting);
        if (count == 0) throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新日报配置信息失败。");
    }

    /**
     * 通过订单添加所有服务中心每天的数据统计记录
     */
    @Transactional
    public void addOrgDayByOrder(Date startDate, Date endDate,SysSetting sysSetting) {
        addOrgDayCommonByOrder(startDate, endDate);

        //执行成功 更新配置表上次执行时间为date
        sysSetting.setLastUpdated(new Date());
        sysSetting.setLastUpdatedBy(systemId);
        sysSetting.setModificationNumber(sysSetting.getModificationNumber() + 1);
        sysSetting.setDefaultValue(Tools.dateToStr(endDate, "yyyy-MM-dd HH:mm:ss"));//执行成功时间
        int count = sysSettingService.updateBySettingTypeSelective(sysSetting);
        if (count == 0) throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新日报配置信息失败。");
    }


    /**
     * 通过订单添加所有服务中心每天的数据统计记录
     */
    private void addOrgDayCommonByOrder(Date startDate, Date endDate){

        //每个服务中心构造一条日报记录
        List<ReportOrgDay> orgDayList = buildReportOrgDayList(endDate);

        //查询当前日期服务中心关联订单数据
        ReportOrgDayQuery query = new ReportOrgDayQuery(startDate, endDate, ConsignOrderStatus.getReletedOrderStatus());
        List<ReportOrgDay> orgReletedOrderData = reportOrgDayDao.queryOrgReletedOrderData(query);
        List<ReportOrgDay> reletedOrderPurchaseAmount = reportOrgDayDao.queryOrgReletedOrderPurchaseAmount(query);

        //查询当前日期服务中心二结订单数据
        query.setOrderStatusList(ConsignOrderStatus.getSecondOrderStatus());
        List<ReportOrgDay> secondOrderData = reportOrgDayDao.queryOrgSecondOrderData(query);

        //查询截止到当天各服务中心所有已签约的卖家数
        List<ReportOrgDay> orgConsignSellerTotal = reportOrgDayDao.queryOrgConsignSellerTotal();

        orgDayList.forEach(a -> {
            Optional<ReportOrgDay> optional;

            //关联订单数据设置（查询时间）
            if (orgReletedOrderData.size() > 0) {
                optional = orgReletedOrderData.stream().filter(b -> a.getOrgId().equals(b.getOrgId())).findFirst();
                if (optional.isPresent()) {
                    a.setReletedTotalSaleAmount(optional.get().getReletedTotalSaleAmount());
                    a.setReletedTotalWeight(optional.get().getReletedTotalWeight());
                    a.setReletedTotalOrder(optional.get().getReletedTotalOrder());
                    a.setReletedTotalConsignOrder(optional.get().getReletedTotalConsignOrder());
                }
            }

            //关联订单采购金额设置（查询时间）
            if (reletedOrderPurchaseAmount.size() > 0) {
                optional = reletedOrderPurchaseAmount.stream().filter(b -> a.getOrgId().equals(b.getOrgId())).findFirst();
                if (optional.isPresent()) {
                    a.setReletedTotalPurchaseAmount(optional.get().getReletedTotalPurchaseAmount());
                }
            }

            //二结订单数据设置（查询时间）
            if (secondOrderData.size() > 0) {
                optional = secondOrderData.stream().filter(b -> a.getOrgId().equals(b.getOrgId())).findFirst();
                if (optional.isPresent()) {
                    a.setSecondTotalSaleAmount(optional.get().getSecondTotalSaleAmount());
                    a.setSecondTotalPurchaseAmount(optional.get().getSecondTotalPurchaseAmount());
                    a.setSecondTotalWeight(optional.get().getSecondTotalWeight());
                    a.setSecondTotalOrder(optional.get().getSecondTotalOrder());
                    a.setSecondTotalConsignOrder(optional.get().getSecondTotalConsignOrder());
                }
            }

            //已签约卖家数(系统时间截止日为止所签约的所有代运营卖家数)
            if (orgConsignSellerTotal.size() > 0) {
                optional = orgConsignSellerTotal.stream().filter(b -> a.getOrgId().equals(b.getOrgId())).findFirst();
                if (optional.isPresent()) {
                    a.setTotalConsignSellerAccount(optional.get().getTotalConsignSellerAccount());
                }
            }

            a.setRemark("查询时间：" + Tools.dateToStr(query.getStartDate()) + " 至 " + Tools.dateToStr(query.getEndDate()));
            int count = reportOrgDayDao.insertSelective(a);//插入数据库
            if (count == 0) throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入日报数据失败。");
        });
    }

    /**
     * 每个服务中心构造一条日报记录
     *
     * @return
     */
    public List<ReportOrgDay> buildReportOrgDayList(Date endDate) {
        List<ReportOrgDay> orgDayList = new ArrayList<>();
        List<Organization> orgList = queryAllBusinessOrg();
        orgList.forEach(a ->
                        orgDayList.add(new ReportOrgDay(a.getId(), a.getName(), endDate, new Date(), systemId, new Date(), systemId, 0l, ""))
        );
        return orgDayList;
    }


    /**
     * 查询所有业务服务中心：如长沙、杭州 去掉服务中心
     *
     * @return
     */
    @Override
    public List<Organization> queryAllBusinessOrg() {
        List<Organization> orgList = organizationDao.queryAllBusinessOrg();
        orgList.forEach(a ->
                a.setName(a.getName().replace("服务中心", "")));
        return orgList;
    }

    /**
     * 获得服务中心日报列头
     *
     * @return
     */
    @Override
    public List<Column> getOrgDayReportColumns() {
        List<Column> columns = new ArrayList<>();

        //日期、交易数据
        Column dateCol = new Column(COL_NAME_CALCULATE_DATE,Column.TEXT_RIGHT,getColIndex(columns.size()),COL_NAME_CALCULATE_DATE);
        columns.add(dateCol);
        Column tradeDataCol = new Column(COL_NAME_TRADEDATA,Column.TEXT_CENTER,getColIndex(columns.size()),COL_NAME_TRADEDATA);
        columns.add(tradeDataCol);

        List<Column> orgCols =  getOrgColumns(columns.size());
        //所有服务中心
        columns.addAll(orgCols);

        //当日合计、沉淀资金
        Column dayTotalCol = new Column(Constant.REPORT_DAY_COL_NAME_DAYTOTAL, Column.TEXT_RIGHT,getColIndex(columns.size()),Constant.REPORT_DAY_COL_NAME_DAYTOTAL);
        columns.add(dayTotalCol);
//        Column fundsCol = new Column(COL_NAME_FUNDS, Column.TEXT_RIGHT,getColIndex(columns.size()),COL_NAME_FUNDS);
//        columns.add(fundsCol);

        return columns;
    }

    private String getColIndex(int size){
        return "var" + (size + 1);
    }

    private List<Column> getOrgColumns(int startSize) {
        List<Column> columns = new ArrayList<>();
        //服务中心
        List<Organization> orgList = queryAllBusinessOrg();
        if (orgList != null) {
            for (Organization a : orgList) {
                columns.add(new Column(ORG_PRDFIX + a.getId(), Column.TEXT_RIGHT,getColIndex(columns.size() + startSize),ORG_PRDFIX));

            }
        }
        return columns;
    }

    @Override
    public List<String> queryAllCalculateDateForPage(ReportOrgDayQuery query) {
        return reportOrgDayDao.queryAllCalculateDateForPage(query);
    }

    @Override
    public List<Map<String, Object>> getOrgDayList(ReportOrgDayQuery query) {
        List<ReportOrgDayDto> orgDayList = reportOrgDayDao.queryByParam(query);
        //对所有的日期根据列转成行数据
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (orgDayList != null && orgDayList.size() > 0) {

            //添加搜索时间范围内的合计行数据（所有页）
            orgDayList.addAll(reportOrgDayDao.queryTotalGroupByOrgByParam(query));

            //服务中心按日期行转列
            List<Map<String, Object>> colsed = toColsByDate(orgDayList, TYPE_DAY);

            //对所有的日期根据列转成行数据
            resultList = toRows(colsed);

            //平台沉淀资金
            List<ReportPrecipitationFundsDto> fundsList = getPrecipitationFundsList(query);

            //当日合计 沉淀资金
            resultList.forEach(a -> {
                //沉淀资金
//                Optional<ReportPrecipitationFundsDto> funOp = fundsList.stream().filter(
//                        b -> (a.get(COL_NAME_CALCULATE_DATE).toString()).equals(b.getCalculateStr())).findFirst();
//                if (funOp.isPresent()) {
//                    a.put(COL_NAME_FUNDS, formatBigDecimal(funOp.get().getPrecipitationFunds(),Constant.MONEY_PRECISION));
//                }

                //计算当日合计
                BigDecimal orgSum = new BigDecimal(a.entrySet().stream().filter(b -> b.getKey().startsWith(ORG_PRDFIX) && b.getValue() != null).mapToDouble(
                        c -> (new BigDecimal(c.getValue().toString())).doubleValue()).sum());
                a.put(Constant.REPORT_DAY_COL_NAME_DAYTOTAL, orgSum);
            });


            //添加当前页合计行
           // resultList.addAll(getTotalRowMap(resultList));
        }
        return resultList;
    }

    private List<ReportPrecipitationFundsDto> getPrecipitationFundsList(ReportOrgDayQuery query){
        //日期范围内 每天的平台沉淀资金
        List<ReportPrecipitationFundsDto> fundsList = reportPrecipitationFundsService.queryByParam(query);

        //合计行的沉淀资金为当前最新数据
        BigDecimal funds = reportPrecipitationFundsService.queryCurrentPrecipitationFunds();
        ReportPrecipitationFundsDto dto = new ReportPrecipitationFundsDto();
        dto.setCalculateStr(TOTAL_ROW_DATE);
        dto.setPrecipitationFunds(funds);
        fundsList.add(dto);

        return fundsList;
    }

    /*
     * 根据日期将所有服务中心行转成列
     * @param orgDayList
     * @return
     */
    private List<Map<String, Object>> toColsByDate(List<ReportOrgDayDto> orgDayList, String type) {
        List<Map<String, Object>> resultList = new ArrayList<>();//日期 服务中心 采购 销售。。。。
        if (orgDayList != null && orgDayList.size() > 0) {
            Map<String, Map<String, Object>> dateMap = new LinkedHashMap<>();
            orgDayList.forEach(a -> {
                String date = a.getCalculateStr();
                Map allOrgsMap;
                if (!dateMap.containsKey(date)) {
                    allOrgsMap = new LinkedHashMap<>();
                    allOrgsMap.put(COL_NAME_CALCULATE_DATE, a.getCalculateStr());
                    dateMap.put(a.getCalculateStr(), allOrgsMap);
                } else {
                    allOrgsMap = dateMap.get(date);
                }

                //添加服务中心业务数据:日报
                if (TYPE_DAY.equals(type)) {
                    allOrgsMap.put(generateKey(ReportDayRowName.SaleAmount.getCode(), a.getOrgId()), formatBigDecimal(a.getReletedTotalSaleAmount(),Constant.MONEY_PRECISION));
                    allOrgsMap.put(generateKey(ReportDayRowName.PurchaseAmount.getCode(), a.getOrgId()), formatBigDecimal(a.getReletedTotalPurchaseAmount(), Constant.MONEY_PRECISION));
                    allOrgsMap.put(generateKey(ReportDayRowName.Weight.getCode(), a.getOrgId()), a.getReletedTotalWeight());
                    allOrgsMap.put(generateKey(ReportDayRowName.Orders.getCode(), a.getOrgId()), a.getReletedTotalOrder());
                    allOrgsMap.put(generateKey(ReportDayRowName.ConsignOrders.getCode(), a.getOrgId()), a.getReletedTotalConsignOrder());
                    allOrgsMap.put(generateKey(ReportDayRowName.Accounts.getCode(), a.getOrgId()), a.getTotalConsignSellerAccount());
                }

                //添加服务中心业务数据：月报
                if (TYPE_MONTH.equals(type)) {
                    allOrgsMap.put(generateKey(ReportDayRowName.SaleAmount.getCode(), a.getOrgId()), formatBigDecimal(a.getSecondTotalSaleAmount(), Constant.MONEY_PRECISION));
                    allOrgsMap.put(generateKey(ReportDayRowName.PurchaseAmount.getCode(), a.getOrgId()), formatBigDecimal(a.getSecondTotalPurchaseAmount(), Constant.MONEY_PRECISION));
                    allOrgsMap.put(generateKey(ReportDayRowName.Weight.getCode(), a.getOrgId()), a.getSecondTotalWeight());
                    allOrgsMap.put(generateKey(ReportDayRowName.Orders.getCode(), a.getOrgId()), a.getSecondTotalOrder());
                    allOrgsMap.put(generateKey(ReportDayRowName.ConsignOrders.getCode(), a.getOrgId()), a.getSecondTotalConsignOrder());
                    allOrgsMap.put(generateKey(ReportDayRowName.Accounts.getCode(), a.getOrgId()), a.getTotalConsignSellerAccount());
                }
            });
            resultList.addAll(dateMap.values());
        }
        return resultList;
    }

    /**
     * 当前页合计行
     * @param resultList 根据结果集汇总
     * @return
     */
    private  List<Map<String, Object>> getTotalRowMap(List<Map<String, Object>> resultList){
        List<Map<String, Object>> totalRows = new ArrayList<>();

        List<ReportDayRowName> rowNames = Arrays.asList(ReportDayRowName.values());
        //合计行的沉淀资金为当前最新数据
        BigDecimal funds = reportPrecipitationFundsService.queryCurrentPrecipitationFunds();

        List<Organization> orgList = queryAllBusinessOrg();

        rowNames.forEach(rowName -> {
            Map<String, Object> totalMap = new LinkedHashMap<>();
            totalMap.put(COL_NAME_CALCULATE_DATE,TOTAL_ROW_DATE);//日期列
            totalMap.put(COL_NAME_TRADEDATA, rowName.getName());//交易数据列
            totalMap.put(COL_NAME_FUNDS,funds);//沉淀资金

            //当日合计列
            totalMap.put(Constant.REPORT_DAY_COL_NAME_DAYTOTAL, new BigDecimal(resultList.stream().filter(a -> rowName.getName().equals(a.get(COL_NAME_TRADEDATA).toString()) && a.get(Constant.REPORT_DAY_COL_NAME_DAYTOTAL) != null).mapToDouble(
                            a -> new BigDecimal(a.get(Constant.REPORT_DAY_COL_NAME_DAYTOTAL).toString()).doubleValue()).sum())
            );

            //所有服务中心计算当前行
            orgList.forEach(org ->
                    totalMap.put(ORG_PRDFIX + org.getId(), new BigDecimal(resultList.stream().filter(
                                    a -> rowName.getName().equals(a.get(COL_NAME_TRADEDATA).toString()) && a.get(ORG_PRDFIX + org.getId()) != null).mapToDouble(
                            v -> new BigDecimal(v.get(ORG_PRDFIX + org.getId()).toString()).doubleValue()).sum())
                    )
            );

            totalRows.add(totalMap);
        });
        return totalRows;
    }

    /*
     * 根据枚举生成行
     * @param paramList
     * @return
     */
    private List<Map<String, Object>> toRows(List<Map<String, Object>> paramList) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (paramList != null && paramList.size() > 0) {
            List<ReportDayRowName> rowNames = Arrays.asList(ReportDayRowName.values());

            paramList.forEach(b -> {
                //设置所有服务中心的rowName行的列
                rowNames.forEach(rowEum -> {
                    Map<String, Object> rowMap = new LinkedHashMap<>();//数据行
                    rowMap.put(COL_NAME_CALCULATE_DATE, b.get(COL_NAME_CALCULATE_DATE));//日期列
                    rowMap.put(COL_NAME_TRADEDATA, rowEum.getName());//交易数据列
                    rowMap.put(Constant.REPORT_DAY_ROW_DATA_TYPE,rowEum.getCode());//交易数据类型列 用来区分是什么数据

                    List<String> keys = b.keySet().stream().filter(c -> c.startsWith(rowEum.getCode())).collect(Collectors.toList());
                    keys.forEach(key -> {
                        String org = StringUtils.substringAfter(key, rowEum.getCode() + "_");//org_${org_id}
                        rowMap.put(org, b.get(key));
                    });
                    resultList.add(rowMap);
                });
            });
        }
        return resultList;
    }

    private String generateKey(String code, Long value) {
        return code + "_" + ORG_PRDFIX + value;
    }

    /**
     * @param b
     * @return
     */
    public static BigDecimal formatBigDecimal(BigDecimal b,int len) {
        if (b == null) return null;
        return b.setScale(len, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获得服务中心月报列头
     *
     * @return
     */
    @Override
    public List<Column> getOrgMonthReportColumns() {
        List<Column> columns = new ArrayList<>();

        //日期、交易数据
        Column dateCol = new Column(COL_NAME_CALCULATE_DATE,Column.TEXT_RIGHT,getColIndex(columns.size()),COL_NAME_CALCULATE_DATE);
        columns.add(dateCol);
        Column tradeDataCol = new Column(COL_NAME_TRADEDATA,Column.TEXT_CENTER,getColIndex(columns.size()),COL_NAME_TRADEDATA);
        columns.add(tradeDataCol);

        List<Column> orgCols =  getOrgColumns(columns.size());
        //所有服务中心
        columns.addAll(orgCols);

        //当日合计、沉淀资金
        Column dayTotalCol = new Column(Constant.REPORT_DAY_COL_NAME_DAYTOTAL, Column.TEXT_RIGHT,getColIndex(columns.size()),Constant.REPORT_DAY_COL_NAME_DAYTOTAL);
        columns.add(dayTotalCol);
//        Column fundsCol = new Column(COL_NAME_FUNDS, Column.TEXT_RIGHT,getColIndex(columns.size()),COL_NAME_FUNDS);
//        columns.add(fundsCol);

        return columns;
    }

    /**
     * 查询服务中心月报在某个时间段内有数据的所有日期集合
     *
     * @param query
     * @return
     */
    @Override
    public List<String> queryMonthReportAllCalculateDateForPage(ReportOrgDayQuery query) {
        return reportOrgDayDao.queryMonthReportAllCalculateDateForPage(query);
    }

    @Override
    public List<Map<String, Object>> getOrgMonthList(ReportOrgDayQuery query) {
        List<ReportOrgDayDto> orgMonthList = reportOrgDayDao.queryMonthByParam(query);
        //对所有的日期根据列转成行数据
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (orgMonthList != null && orgMonthList.size() > 0) {

            //添加搜索时间范围内的合计行数据（所有页）
            orgMonthList.addAll(reportOrgDayDao.queryMonthTotalGroupByOrgByParam(query));

            //服务中心按日期行转列
            List<Map<String, Object>> colsed = toColsByDate(orgMonthList, TYPE_MONTH);

            //对所有的日期根据列转成行数据
            resultList = toRows(colsed);

            //平台沉淀资金
            //List<ReportPrecipitationFundsDto> fundsList = getPrecipitationFundsList(query);

            //当日合计 沉淀资金
            resultList.forEach(a -> {
                //沉淀资金
//                Optional<ReportPrecipitationFundsDto> funOp = fundsList.stream().filter(
//                        b -> (a.get(COL_NAME_CALCULATE_DATE).toString()).equals(b.getCalculateStr())).findFirst();
//                if (funOp.isPresent()) {
//                    a.put(COL_NAME_FUNDS, formatBigDecimal(funOp.get().getPrecipitationFunds(), Constant.MONEY_PRECISION));
//                }

                //计算当日合计
                BigDecimal orgSum = new BigDecimal(a.entrySet().stream().filter(b -> b.getKey().startsWith(ORG_PRDFIX) && b.getValue() != null).mapToDouble(
                        c -> (new BigDecimal(c.getValue().toString())).doubleValue()).sum());
                a.put(Constant.REPORT_DAY_COL_NAME_DAYTOTAL, orgSum);
            });


            //添加当前页合计行
            // resultList.addAll(getTotalRowMap(resultList));
        }
        return resultList;
    }
}

