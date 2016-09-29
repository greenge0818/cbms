package com.prcsteel.platform.order.service.report;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.common.dto.BuyerOrderDetailDto;
import com.prcsteel.platform.order.model.dto.BuyerTradeStatisticsDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderWithDetailsDto;
import com.prcsteel.platform.order.model.dto.ContractListDto;
import com.prcsteel.platform.order.model.dto.InvoiceInBordereauxDto;
import com.prcsteel.platform.order.model.dto.NsortBusinessReportDto;
import com.prcsteel.platform.order.model.dto.ReportRebateDto;
import com.prcsteel.platform.order.model.dto.SellerOrderBusinessReportDto;
import com.prcsteel.platform.order.model.dto.SellerTurnoverStatisticsDto;
import com.prcsteel.platform.order.model.dto.TradeStatisticsDetailDto;
import com.prcsteel.platform.order.model.dto.TradeStatisticsWithDetailDto;
import com.prcsteel.platform.order.model.model.ReportRebateRecord;
import com.prcsteel.platform.order.model.query.BuyerRebateQuery;
import com.prcsteel.platform.order.model.query.BuyerTradeStatisticsQuery;
import com.prcsteel.platform.order.model.query.ChecklistDetailQuery;
import com.prcsteel.platform.order.model.query.ConsignOrderDetailQuery;
import com.prcsteel.platform.order.model.query.InvoiceInBordereauxQuery;
import com.prcsteel.platform.order.model.query.TradeStatisticsQuery;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportBusinessService
 * @Package com.prcsteel.platform.order.service.report
 * @Description: 业务报表
 * @date 2015/8/19
 */
public interface ReportBusinessService {


    /**
     * 查询卖家交易信息列表
     *
     * @param dto
     * @return
     */
    List<SellerOrderBusinessReportDto> querySellerTradeListByDto(SellerOrderBusinessReportDto dto);


    /**
     * 统计卖家交易记录数
     *
     * @param dto
     * @return
     */
    int countSellerTradeByDto(SellerOrderBusinessReportDto dto);

    /**
     * 卖家成交统计报表
     *
     * @param param
     * @return
     */
    List<SellerTurnoverStatisticsDto> querySellerTurnoverStatisticsByParams(Map<String, Object> param);

    /**
     * 获得各服务中心下的品类交易信息列表
     *
     * @param dto
     * @return
     */
    Map<String, List<NsortBusinessReportDto>> getNsortTradeListOfOrg(NsortBusinessReportDto dto);

    /**
     * 交易详情报表数据
     *
     * @param query
     * @return
     */
    List<ConsignOrderWithDetailsDto> queryOrderDetailReport(ConsignOrderDetailQuery query);


    /**
     * 交易详情报表数据
     *
     * @param query
     * @return
     */
    List<ConsignOrderWithDetailsDto> queryOrderDetailReportSecond(ConsignOrderDetailQuery query);

    /**
     * 交易详情按条件查询总数
     *
     * @param query
     * @return
     */
    int queryOrdersCount(ConsignOrderDetailQuery query);

    /**
     * 卖家成交统计记录数
     *
     * @param param
     * @return
     */
    int countSellerTurnoverStatisticsByParams(Map<String, Object> param);

    /**
     * 根据客户id 查询客户及归属的交易员的相关信息
     *
     * @param accountId 客户id
     * @return
     */
    BuyerOrderDetailDto queryAccountAndManagerInfoByAccountId(Long accountId);

    /**
     * 查询买家采购明细列表
     *
     * @param dto
     * @return
     */
    List<BuyerOrderDetailDto> queryBuyerOrderDetailByDto(BuyerOrderDetailDto dto);

    /**
     * 统计买家采购明细记录数
     *
     * @param dto
     * @return
     */
    int countBuyerOrderDetailByDto(BuyerOrderDetailDto dto);

    /**
     * 买家交易报表数据
     *
     * @param query
     * @return
     */
    List<BuyerTradeStatisticsDto> queryBuyerTrade(BuyerTradeStatisticsQuery query);

    /**
     * 买家交易报表数据总数
     *
     * @param query
     * @return
     */
    int queryBuyerTradeCount(BuyerTradeStatisticsQuery query);


    /**
     * 获得买家返利列表
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    List<ReportRebateDto> getBuyerRebateList(BuyerRebateQuery queryParam);

    /**
     * 统计买家返利记录数
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    int countBuyerRebate(BuyerRebateQuery queryParam);
    /**
     * 服务中心交易报表数据总数
     * @param query
     * @return
     */
    int queryTradeStatisticsCount(TradeStatisticsQuery query);

    /**
     * 服务中心交易报表数据
     * @param query
     * @return
     */
    List<TradeStatisticsWithDetailDto> queryTradeStatistics(TradeStatisticsQuery query,boolean showDetail);

    /**
     * 服务中心交易员统计详情
     *
     * @param query
     * @return
     */
    public List<TradeStatisticsDetailDto> queryTradeStatisticsDetail(TradeStatisticsQuery query);

    /**
     * 获得买家的所有联系人的返利信息列表
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    List<ReportRebateDto> getAllContactsRebateListOfBuyer(BuyerRebateQuery queryParam);

    /**
     * 获得联系人返利信息列表
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    List<ReportRebateDto> getContactsRebateList(BuyerRebateQuery queryParam);

    /**
     * 查询买家的大类返利信息列表
     * @param queryParam
     * @return
     */
    List<ReportRebateRecord> queryGroupCategoryRebateByBuyerId(BuyerRebateQuery queryParam);

    /**
     * 查询联系人的大类返利信息列表
     * @param queryParam
     * @return
     */
    List<ReportRebateRecord> queryGroupCategoryRebateByContactId(BuyerRebateQuery queryParam);
    
    /**
     * 进项发票统计报表数据
     * @param queryParam
     * @return
     */
	List<InvoiceInBordereauxDto> queryInvoiceInBordereauxData(InvoiceInBordereauxQuery queryParam);

	/**
     * 进项发票统计报表数据总数
     * @param queryParam
     * @return
     */
	int queryInvoiceInBordereauxCount(InvoiceInBordereauxQuery queryParam);


	/**
     * 进项发票统计报表数据合计
     * @param queryParam
     * @return
     */
	InvoiceInBordereauxDto queryInvoiceInBordereauxSum(InvoiceInBordereauxQuery queryParam);
	
	/**
     * 查询合同清单 add by wangxianjun
     * @param query
     * @return
     */
	List<ContractListDto>  selectContractList(ChecklistDetailQuery query);
	
	/**
     * 统计合同清单 add by wangxianjun
     * @param query
     * @return
     */
	int countContractList(ChecklistDetailQuery query);
}
