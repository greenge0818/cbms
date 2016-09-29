package com.prcsteel.platform.order.service.report.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.common.dto.BuyerOrderDetailDto;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.model.dto.BuyerTradeStatisticsDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderItemsInfoDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderWithDetailsDto;
import com.prcsteel.platform.order.model.dto.ContractListDto;
import com.prcsteel.platform.order.model.dto.InvoiceInBordereauxDto;
import com.prcsteel.platform.order.model.dto.NsortBusinessReportDto;
import com.prcsteel.platform.order.model.dto.ReportRebateDto;
import com.prcsteel.platform.order.model.dto.SellerOrderBusinessReportDto;
import com.prcsteel.platform.order.model.dto.SellerTurnoverStatisticsDto;
import com.prcsteel.platform.order.model.dto.TradeStatisticsDetailDto;
import com.prcsteel.platform.order.model.dto.TradeStatisticsWithDetailDto;
import com.prcsteel.platform.order.model.enums.ConsignOrderPayStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatusForReport;
import com.prcsteel.platform.order.model.enums.OrderItemStatus;
import com.prcsteel.platform.order.model.model.ReportRebateRecord;
import com.prcsteel.platform.order.model.query.BuyerRebateQuery;
import com.prcsteel.platform.order.model.query.BuyerTradeStatisticsQuery;
import com.prcsteel.platform.order.model.query.ChecklistDetailQuery;
import com.prcsteel.platform.order.model.query.ConsignOrderDetailQuery;
import com.prcsteel.platform.order.model.query.InvoiceInBordereauxQuery;
import com.prcsteel.platform.order.model.query.TradeStatisticsQuery;
import com.prcsteel.platform.order.persist.dao.ConsignOrderDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsDao;
import com.prcsteel.platform.order.persist.dao.ReportBusinessDao;
import com.prcsteel.platform.order.persist.dao.ReportRebateRecordDao;
import com.prcsteel.platform.order.service.report.ReportBusinessService;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportBusinessServiceImpl
 * @Package com.prcsteel.platform.order.service.impl.report
 * @Description: 业务报表
 * @date 2015/8/19
 */
@Service("reportBusinessService")
public class ReportBusinessServiceImpl implements ReportBusinessService {

    private static final Logger logger = LoggerFactory.getLogger(ReportBusinessServiceImpl.class);

    @Autowired
    ConsignOrderDao consignOrderDao;

    @Autowired
    ConsignOrderItemsDao consignOrderItemsDao;

    @Resource
    AccountDao accountDao;

    @Resource
    ReportRebateRecordDao rebateRecordDao;

    @Autowired
    ReportBusinessDao reportBusinessDao;

    /**
     * 查询卖家交易信息列表
     *
     * @param dto
     * @return
     */
    @Override
    public List<SellerOrderBusinessReportDto> querySellerTradeListByDto(SellerOrderBusinessReportDto dto) {

        //查询卖家某时间段交易列表（当月信息：所选时间段）
        List<SellerOrderBusinessReportDto> resultList = consignOrderDao.querySellerTradeListByDto(dto);

        //满足条件的所有卖家当月交易重量总和（所选时间段、不分是否代运营）
        BigDecimal sumAllSellerWeight = consignOrderDao.sumAllSellerWeightByDto(dto);

        //查询卖家累计交易总重量（截止所选结束时间）
        dto.setStrStartTime(null);//设置开始时间条件为null，则不作为查询条件
        List<SellerOrderBusinessReportDto> grandTotalList = consignOrderDao.querySellerTradeListByDto(dto);

        //设置所有卖家当月交易重量总和、卖家累计交易总重量
        //TODO 重构
        SellerOrderBusinessReportDto temp;
        if (resultList != null) {
            for (Iterator iterator = resultList.iterator(); iterator.hasNext(); ) {
                temp = (SellerOrderBusinessReportDto) iterator.next();
                temp.setMonthAllSellerWeight(sumAllSellerWeight);

                for (SellerOrderBusinessReportDto grandTotalDto : grandTotalList) {
                    if (StringUtils.equals(temp.getSellerName(), grandTotalDto.getSellerName()) &&
                            StringUtils.equals(temp.getConsignType(), grandTotalDto.getConsignType())) {
                        temp.setSumWeight(grandTotalDto.getSumWeight());
                        break;
                    }
                }

            }
        }

        return resultList;
    }

    /**
     * 统计卖家交易记录数
     *
     * @param dto
     * @return
     */
    @Override
    public int countSellerTradeByDto(SellerOrderBusinessReportDto dto) {
        return consignOrderDao.countSellerTradeByDto(dto);
    }

    /**
     * 获得各服务中心下的品类交易信息列表
     *
     * @param dto
     * @return
     */
    @Override
    public Map<String, List<NsortBusinessReportDto>> getNsortTradeListOfOrg(NsortBusinessReportDto dto) {
        //存放各服务中心的品类数据集合：key:服务中心名称 value：品类数据集合
        Map<String, List<NsortBusinessReportDto>> resultMap = new LinkedHashMap<>();
        List<NsortBusinessReportDto> queryList = consignOrderDao.queryNsortTradeListByDto(dto);

        //按服务中心分组，获得服务中心相关品类数据列表
        if (queryList != null && queryList.size() > 0) {
            resultMap = queryList.stream().peek(e -> {
                //计算代运营交易笔数占比
                e.setConsignOrderPercent(Tools.calculatePercentage(e.getTotalConsignOrder(), e.getTotalOrder()));
                //计算平均每笔交易重量
                e.setAvgWeight(calAvgWeight(e.getTotalWeight(), e.getTotalOrder()));
            }).collect(Collectors.groupingBy(NsortBusinessReportDto::getOrgName));//按服务中心分组
        }
        return resultMap;
    }


    /*
     * 计算平均数四舍五入保留4位小数
     * @param totalWeight
     * @param totalOrder
     * @return
     */
    private BigDecimal calAvgWeight(BigDecimal totalWeight, Integer totalOrder) {
        BigDecimal avgWeight;
        int scale = 4;
        if (totalWeight == null || totalOrder == null || totalOrder == 0) {
            avgWeight = BigDecimal.ZERO;
        } else {
            avgWeight = totalWeight.divide(new BigDecimal(totalOrder), scale, RoundingMode.HALF_UP);
        }
        return avgWeight;
    }


    /**
     * 卖家成交统计报表
     *
     * @param param
     * @return
     */
    @Override
    public List<SellerTurnoverStatisticsDto> querySellerTurnoverStatisticsByParams(Map<String, Object> param) {
        List<SellerTurnoverStatisticsDto> list = consignOrderDao.querySellerTurnoverStatisticsByParams(param);
//        List<String> names = new ArrayList<>();
//        for (SellerTurnoverStatisticsDto dto : list) {
//            names.add(dto.getSellerName());
//        }
//        if (!names.isEmpty()) {
//            String timeFrom = param.get("timeFrom") != null ? param.get("timeFrom").toString().toString() : null;
//            String timeTo = param.get("timeTo") != null ? param.get("timeTo").toString().toString() : null;
//            Map<String, Integer> map = getPublishCountByAccountNames(names, timeFrom, timeTo);
//            for (SellerTurnoverStatisticsDto dto : list) {
//                dto.setPublishCount(map.get(dto.getSellerName()));
//            }
//        }
        return list;
    }

    /**
     * 卖家成交统计记录数
     *
     * @param param
     * @return
     */
    @Override
    public int countSellerTurnoverStatisticsByParams(Map<String, Object> param) {
        return consignOrderDao.countSellerTurnoverStatisticsByParams(param);
    }

//    /*http://market.prcsteel.com/Home/GetPublishCount?
//    companyname=%E6%B9%96%E5%8D%97%E9%92%A2%E8%81%94%E7%A7%91%E8%B4%B8%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8,%E9%95%BF%E6%B2%99%E5%BE%AE%E6%98%93%E5%95%86%E5%8A%A1%E4%BB%A3%E7%90%86%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8&
//    startdate=2013-01-01&
//    enddate=2015-08-01*/
//    /**
//     * 获取公司挂牌次数
//     *
//     * @param accountNames   公司名List
//     * @param startdate      开始统计时间
//     * @param enddate        结束统计时间
//     * @return
//     */
//    private String url = "http://market.prcsteel.com/Home/GetPublishCount";
//
//    private Map<String, Integer> getPublishCountByAccountNames(List<String> accountNames, String startdate, String enddate) {
//        Map<String, Integer> map = new HashMap<>();
//        StringBuilder sb = new StringBuilder();
//        for (String s : accountNames) {
//            sb.append(s + ",");
//        }
//        sb.deleteCharAt(sb.lastIndexOf(","));
//        HashMap<String, String> postPars = new HashMap<String, String>();
//        postPars.put("companyname", sb.toString());
//        if (startdate != null) {
//            postPars.put("startdate", startdate);
//        }
//        if (enddate != null) {
//            postPars.put("enddate", enddate);
//        }
//        String requestUrl = url;
//        String resultXML = HttpInvokerUtils.readContentFromPost(requestUrl, postPars);
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            JsonNode jsonNode = mapper.readTree(resultXML);
//            JsonNode dataNode = jsonNode.path("data");  /*"data":[{"CompanyName":"长沙微易商务代理有限公司","PublishCount":35},{"CompanyName":"湖南钢联科贸有限公司","PublishCount":16}]*/
//            for (JsonNode node : dataNode) {
//                map.put(node.path("CompanyName").asText(), node.path("PublishCount").asInt());
//            }
//        } catch (Exception e) {
//        }
//        return map;
//    }

    @Override
    public List<ConsignOrderWithDetailsDto> queryOrderDetailReport(ConsignOrderDetailQuery query) {
        List<ConsignOrderWithDetailsDto> consignOrderWithDetailsDtoAllList = consignOrderDao.queryOrders(query);

        List<ConsignOrderItemsInfoDto> consignOrderItemsInfoDtoListList = consignOrderItemsDao.queryOrderdetails(query);
        List<ConsignOrderItemsInfoDto> itemsList;
        List<ConsignOrderWithDetailsDto> consignOrderWithDetailsDtoList = new ArrayList<ConsignOrderWithDetailsDto>();
        for (ConsignOrderWithDetailsDto order : consignOrderWithDetailsDtoAllList) {
            itemsList = new ArrayList<>();
            for (ConsignOrderItemsInfoDto orderItems : consignOrderItemsInfoDtoListList) {
                //双敲匹配才显示实提
                if(OrderItemStatus.DEAL.ordinal()!=orderItems.getStatus().intValue())
                {
                    orderItems.setActualPickWeightServer(BigDecimal.ZERO);
                }
                if (orderItems.getOrderId().equals(order.getId())) {
                    itemsList.add(orderItems);
                }
            }
            String payStatusName = "";//待出库状态显示付款状态，其它不显示付款状态
            if (ConsignOrderStatusForReport.RELATED.getCode().equals(order.getStatus())) {
                payStatusName = ConsignOrderPayStatus.getNameByCode(order.getPayStatus());
            }
            order.setPayStatus(payStatusName);
            String statusName = ConsignOrderStatusForReport.getNameByCode(order.getStatus());
            if (StringUtils.isNotEmpty(statusName)) {
                order.setStatus(statusName);
            }
            //modify by wangxianjun 当订单下有订单明细时，才展示
            if(itemsList.size() > 0) {
                consignOrderItemsInfoDtoListList.removeAll(itemsList);
                order.setConsignOrderItems(itemsList);
                consignOrderWithDetailsDtoList.add(order);
            }
        }

        return consignOrderWithDetailsDtoList;
    }
    @Override
    public List<ConsignOrderWithDetailsDto> queryOrderDetailReportSecond(ConsignOrderDetailQuery query) {
        List<ConsignOrderWithDetailsDto> consignOrderWithDetailsDtoAllList = consignOrderDao.queryOrders(query);
        List<ConsignOrderItemsInfoDto> consignOrderItemsInfoDtoListList = consignOrderItemsDao.queryOrderdetailsSecond(query);
        List<ConsignOrderItemsInfoDto> itemsList;
        for (ConsignOrderWithDetailsDto order : consignOrderWithDetailsDtoAllList) {
            itemsList = new ArrayList<>();
            for (ConsignOrderItemsInfoDto orderItems : consignOrderItemsInfoDtoListList) {
                //双敲匹配才显示实提
                if(OrderItemStatus.DEAL.ordinal()!=orderItems.getStatus().intValue())
                {
                    orderItems.setActualPickWeightServer(BigDecimal.ZERO);
                }
                if(order.getId().equals(orderItems.getOrderId())){
                    itemsList.add(orderItems);
                }
            }
            String payStatusName = "";//待出库状态显示付款状态，其它不显示付款状态
            if (ConsignOrderStatusForReport.RELATED.getCode().equals(order.getStatus())) {
                payStatusName = ConsignOrderPayStatus.getNameByCode(order.getPayStatus());
            }
            order.setPayStatus(payStatusName);
            String statusName = ConsignOrderStatusForReport.getNameByCode(order.getStatus());
            if (StringUtils.isNotEmpty(statusName)) {
                order.setStatus(statusName);
            }
            consignOrderItemsInfoDtoListList.removeAll(itemsList);
            order.setConsignOrderItems(itemsList);
        }

        return consignOrderWithDetailsDtoAllList;
    }
    @Override
    public int queryOrdersCount(ConsignOrderDetailQuery query) {
        return consignOrderDao.queryOrdersCount(query);
    }

    /**
     * 根据客户id 查询客户及归属的交易员的相关信息
     *
     * @param accountId 客户id
     * @return
     */
    @Override
    public BuyerOrderDetailDto queryAccountAndManagerInfoByAccountId(Long accountId) {
        return accountDao.queryAccountAndManagerInfoByAccountId(accountId);
    }

    /**
     * 查询买家采购明细列表
     *
     * @param dto
     * @return
     */
    @Override
    public List<BuyerOrderDetailDto> queryBuyerOrderDetailByDto(BuyerOrderDetailDto dto) {
        return consignOrderDao.queryBuyerOrderDetailByDto(dto);
    }

    /**
     * 统计买家采购明细记录数
     *
     * @param dto
     * @return
     */
    @Override
    public int countBuyerOrderDetailByDto(BuyerOrderDetailDto dto) {
        return consignOrderDao.countBuyerOrderDetailByDto(dto);
    }

    /**
     * 买家交易报表数据
     *
     * @param query
     * @return
     */
    @Override
    public List<BuyerTradeStatisticsDto> queryBuyerTrade(BuyerTradeStatisticsQuery query) {
        List<BuyerTradeStatisticsDto> list = consignOrderDao.queryBuyerTrade(query);
        for (BuyerTradeStatisticsDto item : list) {
            int day = Tools.daysDiff(query.getBeginTime(), query.getEndTime());
            day++;//时间间隔为日期差+1
            if (item.getContactOrderCount() == null || item.getContactOrderCount().intValue() <= 0)
                item.setFrequencyOfContact(0.0);
            else {
                item.setFrequencyOfContact(day / item.getContactOrderCount().doubleValue());
            }
            if (item.getTotalOrderCount() == null || item.getTotalOrderCount().intValue() <= 0)
                item.setFrequencyOfCompany(0.0);
            else {
                item.setFrequencyOfCompany(day / item.getTotalOrderCount().doubleValue());
            }
            //是否首次采购
            if(item.getNum() > 0 ){
                item.setIsFirst("否");
            }else{
                item.setIsFirst("是");
            }
        }
        return list;
    }

    /**
     * 买家交易报表数据总数
     *
     * @param query
     * @return
     */
    public int queryBuyerTradeCount(BuyerTradeStatisticsQuery query) {
        return consignOrderDao.queryBuyerTradeCount(query);
    }

    /**
     * 获得买家返利信息列表
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    @Override
    public List<ReportRebateDto> getBuyerRebateList(BuyerRebateQuery queryParam) {
        List<ReportRebateDto> rebateList = rebateRecordDao.queryBuyerRebate(queryParam);

        //提现金额
        if (rebateList != null && rebateList.size() > 0) {
            List<ReportRebateDto> withdrawList = getAccountWithdrawByAccountIds(queryParam, rebateList);
            setAccountWithdrawAmount(rebateList, withdrawList);
        }
        return rebateList;
    }

    /**
     * 统计买家返利记录数
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    @Override
    public int countBuyerRebate(BuyerRebateQuery queryParam) {
        return rebateRecordDao.countBuyerRebate(queryParam);
    }

    /*
     * 服务中心交易报表数据总数
     *
     * @param query
     * @return
     */
    public int queryTradeStatisticsCount(TradeStatisticsQuery query) {
        return reportBusinessDao.queryTradeStatisticsCount(query);
    }

    /**
     * 服务中心交易报表数据
     *
     * @param query
     * @return
     */
    public List<TradeStatisticsWithDetailDto> queryTradeStatistics(TradeStatisticsQuery query, boolean showDetail) {
        List<TradeStatisticsWithDetailDto> mainList = getTradeStatistics(query);

        if (showDetail) {
            List<TradeStatisticsDetailDto> detailMainList = getTradeStatisticDetail(query);

            List<TradeStatisticsDetailDto> itemsList;
            for (TradeStatisticsWithDetailDto item : mainList) {
                itemsList = new ArrayList<>();
                for (TradeStatisticsDetailDto subitem : detailMainList) {
                    if (item.getOrgId().equals(subitem.getOrgId())) {
                        itemsList.add(subitem);
                    }
                }
                detailMainList.removeAll(itemsList);
                item.setTradeStatisticsDetailList(itemsList);
            }
        }
        return mainList;
    }

    private List<TradeStatisticsWithDetailDto> getTradeStatistics(TradeStatisticsQuery query) {
        List<TradeStatisticsWithDetailDto> mainList = reportBusinessDao.queryTradeStatisticsMainData(query);

        List<TradeStatisticsWithDetailDto> buyerDataList = reportBusinessDao.queryTradeStatisticsBuyerData(query);
        List<TradeStatisticsWithDetailDto> frequentDataList = reportBusinessDao.queryTradeStatisticsFrequentData(query);
        List<TradeStatisticsWithDetailDto> consignDataList = reportBusinessDao.queryTradeStatisticsConsignData(query);
        List<TradeStatisticsWithDetailDto> tempDataList = reportBusinessDao.queryTradeStatisticsTempData(query);

        for (TradeStatisticsWithDetailDto item : mainList) {
            if (buyerDataList != null && buyerDataList.size() > 0) {
                Optional<TradeStatisticsWithDetailDto> result = buyerDataList.stream().filter(a -> a.getOrgId().equals(item.getOrgId())).findFirst();
                if (result.isPresent()) {
                    TradeStatisticsWithDetailDto buyerData = result.get();
                    if (buyerData != null) {
                        item.setOrderCount(buyerData.getOrderCount());
                        item.setConsignOrderCount(buyerData.getConsignOrderCount());
                        //item.setOrderTotalAmount(buyerData.getOrderTotalAmount());
                        //item.setOrderTotalWeight(buyerData.getOrderTotalWeight());
                        item.setBuyerCount(buyerData.getBuyerCount());
                    }
                }
            }

            if (frequentDataList != null && frequentDataList.size() > 0) {
                Optional<TradeStatisticsWithDetailDto> result = frequentDataList.stream().filter(a -> a.getOrgId().equals(item.getOrgId())).findFirst();
                if (result.isPresent()) {
                    TradeStatisticsWithDetailDto frequentData = result.get();
                    if (frequentData != null) {
                        item.setFrequentTradeCurMonthCount(frequentData.getFrequentTradeCurMonthCount());
                        item.setFrequentTradePrevMonthCount(frequentData.getFrequentTradePrevMonthCount());
                    }
                }
            }

            if (consignDataList != null && consignDataList.size() > 0) {
                Optional<TradeStatisticsWithDetailDto> result = consignDataList.stream().filter(a -> a.getOrgId().equals(item.getOrgId())).findFirst();
                if (result.isPresent()) {
                    TradeStatisticsWithDetailDto consignData = result.get();
                    if (consignData != null) {
                        item.setTradeSellerCount(consignData.getTradeSellerCount());
                        item.setSellerTradeCount(consignData.getSellerTradeCount());
                        item.setSellerTradeTotalWeight(consignData.getSellerTradeTotalWeight());
                        item.setOrderTotalAmount(item.getOrderTotalAmount().add(consignData.getSellerTradeTotalAmount()));
                        item.setOrderTotalWeight(item.getOrderTotalWeight().add(consignData.getSellerTradeTotalWeight()));
                    }
                }
            }

            if (tempDataList != null && tempDataList.size() > 0) {
                Optional<TradeStatisticsWithDetailDto> result = tempDataList.stream().filter(a -> a.getOrgId().equals(item.getOrgId())).findFirst();
                if (result.isPresent()) {
                    TradeStatisticsWithDetailDto tempData = result.get();
                    if (tempData != null) {
                        item.setTempTradeSellerCount(tempData.getTempTradeSellerCount());
                        item.setTempSellerTradeCount(tempData.getTempSellerTradeCount());
                        item.setTempSellerTradeTotalWeight(tempData.getTempSellerTradeTotalWeight());
                        item.setOrderTotalAmount(item.getOrderTotalAmount().add(tempData.getTempSellerTradeTotalAmount()));
                        item.setOrderTotalWeight(item.getOrderTotalWeight().add(tempData.getTempSellerTradeTotalWeight()));
                    }
                }
            }
        }

        return mainList;
    }

    private List<TradeStatisticsDetailDto> getTradeStatisticDetail(TradeStatisticsQuery query) {
        List<TradeStatisticsDetailDto> detailMainList = reportBusinessDao.queryTradeStatisticsDetailMainData(query);
        List<TradeStatisticsDetailDto> detailBuyerList = reportBusinessDao.queryTradeStatisticsDetailBuyerData(query);
        List<TradeStatisticsDetailDto> detailFrequentList = reportBusinessDao.queryTradeStatisticsDetailFrequentData(query);
        List<TradeStatisticsDetailDto> detailConsignList = reportBusinessDao.queryTradeStatisticsDetailConsignData(query);
        List<TradeStatisticsDetailDto> detailTempList = reportBusinessDao.queryTradeStatisticsDetailTempData(query);
        for (TradeStatisticsDetailDto item : detailMainList) {
            if (detailBuyerList != null && detailBuyerList.size() > 0) {
                Optional<TradeStatisticsDetailDto> result = detailBuyerList.stream().filter(a -> a.getUserId().equals(item.getUserId())).findFirst();
                if (result.isPresent()) {
                    TradeStatisticsDetailDto buyerData = result.get();
                    if (buyerData != null) {
                        item.setOrderCount(buyerData.getOrderCount());
                        item.setConsignOrderCount(buyerData.getConsignOrderCount());
                        //item.setOrderTotalAmount(buyerData.getOrderTotalAmount());
                        //item.setOrderTotalWeight(buyerData.getOrderTotalWeight());
                        item.setBuyerCount(buyerData.getBuyerCount());
                    }
                }
            }
            if (detailBuyerList != null && detailFrequentList.size() > 0) {
                Optional<TradeStatisticsDetailDto> result = detailFrequentList.stream().filter(a -> a.getUserId().equals(item.getUserId())).findFirst();
                if (result.isPresent()) {
                    TradeStatisticsDetailDto frequentData = result.get();
                    if (frequentData != null) {
                        item.setFrequentTradeCurMonthCount(frequentData.getFrequentTradeCurMonthCount());
                        item.setFrequentTradePrevMonthCount(frequentData.getFrequentTradePrevMonthCount());
                    }
                }
            }

            if (detailConsignList != null && detailConsignList.size() > 0) {
                Optional<TradeStatisticsDetailDto> result = detailConsignList.stream().filter(a -> a.getUserId().equals(item.getUserId())).findFirst();
                if (result.isPresent()) {
                    TradeStatisticsDetailDto consignData = result.get();
                    if (consignData != null) {
                        item.setTradeSellerCount(consignData.getTradeSellerCount());
                        item.setSellerTradeCount(consignData.getSellerTradeCount());
                        item.setSellerTradeTotalWeight(consignData.getSellerTradeTotalWeight());
                        item.setOrderTotalAmount(item.getOrderTotalAmount().add(consignData.getSellerTradeTotalAmount()));
                        item.setOrderTotalWeight(item.getOrderTotalWeight().add(consignData.getSellerTradeTotalWeight()));
                    }
                }
            }

            if (detailTempList != null && detailTempList.size() > 0) {
                Optional<TradeStatisticsDetailDto> result = detailTempList.stream().filter(a -> a.getUserId().equals(item.getUserId())).findFirst();
                if (result.isPresent()) {
                    TradeStatisticsDetailDto tempData = result.get();
                    if (tempData != null) {
                        item.setTempTradeSellerCount(tempData.getTempTradeSellerCount());
                        item.setTempSellerTradeCount(tempData.getTempSellerTradeCount());
                        item.setTempSellerTradeTotalWeight(tempData.getTempSellerTradeTotalWeight());
                        item.setOrderTotalAmount(item.getOrderTotalAmount().add(tempData.getTempSellerTradeTotalAmount()));
                        item.setOrderTotalWeight(item.getOrderTotalWeight().add(tempData.getTempSellerTradeTotalWeight()));
                    }
                }
            }
        }
        return detailMainList;
    }

    /**
     * 服务中心交易员统计详情
     *
     * @param query
     * @return
     */
    public List<TradeStatisticsDetailDto> queryTradeStatisticsDetail(TradeStatisticsQuery query) {
        List<TradeStatisticsDetailDto> tradeStatisticsDetailDtoList = getTradeStatisticDetail(query);
        return tradeStatisticsDetailDtoList;
    }

    /**
     * 获得买家的所有联系人的返利信息列表
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    @Override
    public List<ReportRebateDto> getAllContactsRebateListOfBuyer(BuyerRebateQuery queryParam) {
        List<ReportRebateDto> rebateList = rebateRecordDao.queryAllContactsRebateByBuyerId(queryParam);

        //提现金额
        if (rebateList != null && rebateList.size() > 0) {
            List<ReportRebateDto> withdrawList = getContactWithdrawByContactIds(queryParam, rebateList);
            setContactWithdrawAmount(rebateList, withdrawList);
        }
        return rebateList;
    }

    /**
     * 获得联系人返利信息列表
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    @Override
    public List<ReportRebateDto> getContactsRebateList(BuyerRebateQuery queryParam) {
        List<ReportRebateDto> rebateList = rebateRecordDao.queryContactsRebate(queryParam);
        //提现金额
        if (rebateList != null && rebateList.size() > 0) {
            List<ReportRebateDto> withdrawList = getContactWithdrawByContactIds(queryParam, rebateList);
            setContactWithdrawAmount(rebateList, withdrawList);
        }
        return rebateList;
    }

    /**
     * 查询买家的大类返利信息列表
     *
     * @param queryParam
     * @return
     */
    @Override
    public List<ReportRebateRecord> queryGroupCategoryRebateByBuyerId(BuyerRebateQuery queryParam) {
        return rebateRecordDao.queryGroupCategoryRebateByBuyerId(queryParam);
    }

    /**
     * 查询联系人的大类返利信息列表
     *
     * @param queryParam
     * @return
     */
    @Override
    public List<ReportRebateRecord> queryGroupCategoryRebateByContactId(BuyerRebateQuery queryParam) {
        return rebateRecordDao.queryGroupCategoryRebateByContactId(queryParam);
    }

    /*
     * 设置联系人提现金额
     * @param rebateList 待设置的返利列表
     * @param withdrawList 提现列表
     */
    private void setContactWithdrawAmount(List<ReportRebateDto> rebateList, List<ReportRebateDto> withdrawList) {
        //提现金额
        if (withdrawList != null && withdrawList.size() > 0) {
            Optional<ReportRebateDto> withDraw;
            for (ReportRebateDto rebate : rebateList) {
                withDraw = withdrawList.stream().filter(e -> rebate.getContactId().equals(e.getContactId())).findFirst();
                if (withDraw.isPresent()) {
                    rebate.setWithdrawAmount(withDraw.get().getWithdrawAmount());
                }
            }
        }
    }

    /*
    * 设置买家提现金额
    * @param rebateList 待设置的返利列表
    * @param withdrawList 提现列表
    */
    private void setAccountWithdrawAmount(List<ReportRebateDto> rebateList, List<ReportRebateDto> withdrawList) {
        //提现金额
        if (withdrawList != null && withdrawList.size() > 0) {
            Optional<ReportRebateDto> withDraw;
            for (ReportRebateDto rebate : rebateList) {
                withDraw = withdrawList.stream().filter(e -> rebate.getAccountId().equals(e.getAccountId())).findFirst();
                if (withDraw.isPresent()) {
                    rebate.setWithdrawAmount(withDraw.get().getWithdrawAmount());
                }
            }
        }
    }


    /*
     * 获得买家提现金额
     * @param queryParam 查询参数
     * @return
     */
    private List<ReportRebateDto> getAccountWithdrawByAccountIds(BuyerRebateQuery queryParam, List<ReportRebateDto> rebateList) {
        List<Long> accountIds = getAccountIds(rebateList);
        queryParam.setBuyerIdList(accountIds);
        return rebateRecordDao.queryAccountWithdrawByAccountIds(queryParam);
    }

    /*
     * 获得联系人提现金额
     * @param queryParam 查询参数
     * @return
     */
    private List<ReportRebateDto> getContactWithdrawByContactIds(BuyerRebateQuery queryParam, List<ReportRebateDto> rebateList) {
        List<Long> contactIds = getContactIds(rebateList);
        queryParam.setContactIdList(contactIds);
        return rebateRecordDao.queryContactWithdrawByContactIds(queryParam);
    }

    /*
     * 返回买家id列表
     * @param rebateList
     * @return
     */
    private List<Long> getAccountIds(List<ReportRebateDto> rebateList) {
        List<Long> accountIds = new ArrayList<>();
        if (rebateList != null && rebateList.size() > 0) {
            accountIds = rebateList.stream().map(ReportRebateDto::getAccountId).collect(Collectors.toList());
        }
        return accountIds;
    }

    /*
    * 返回联系人id列表
    * @param rebateList
    * @return
    */
    private List<Long> getContactIds(List<ReportRebateDto> rebateList) {
        List<Long> contactIds = new ArrayList<>();
        if (rebateList != null && rebateList.size() > 0) {
            contactIds = rebateList.stream().map(ReportRebateDto::getContactId).collect(Collectors.toList());
        }
        return contactIds;
    }
    
    /**
     * 进项发票统计报表数据
     * @param queryParam
     * @return
     */
	@Override
	public List<InvoiceInBordereauxDto> queryInvoiceInBordereauxData(InvoiceInBordereauxQuery queryParam) {
		return reportBusinessDao.queryOrderItems(queryParam);
	}
	/**
     * 进项发票统计报表数据总量
     * @param queryParam
     * @return
     */
	@Override
	public int queryInvoiceInBordereauxCount(InvoiceInBordereauxQuery queryParam) {
		return reportBusinessDao.queryInvoiceInBordereauxCount(queryParam);
	}

	/**
     * 进项发票统计报表数据合计
     * @param queryParam
     * @return
     */
	@Override
	public InvoiceInBordereauxDto queryInvoiceInBordereauxSum(InvoiceInBordereauxQuery queryParam) {
		return reportBusinessDao.queryInvoiceInBordereauxSum(queryParam);
	}
	
	/**
     * 查询合同清单 add by wangxianjun
     * @param query
     * @return
     */
	@Override
	public List<ContractListDto>  selectContractList(ChecklistDetailQuery query){
		return consignOrderDao.selectContractList(query);
	}
	
	/**
     * 统计合同清单 add by wangxianjun
     * @param query
     * @return
     */
	@Override
	public int countContractList(ChecklistDetailQuery query){
		return consignOrderDao.countContractList(query);
	}
}
