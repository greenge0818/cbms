package com.prcsteel.platform.order.service.report;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.dto.IncomeSummaryDto;
import com.prcsteel.platform.order.model.dto.InvoiceInWithDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutItemDetailDto;
import com.prcsteel.platform.order.model.dto.ReportBuyerInvoiceOutDto;
import com.prcsteel.platform.order.model.dto.ReportInvoiceInAndOutDto;
import com.prcsteel.platform.order.model.dto.ReportSellerInvoiceInDto;
import com.prcsteel.platform.order.model.dto.UnInvoicedDto;
import com.prcsteel.platform.order.model.dto.UninvoicedInDto;
import com.prcsteel.platform.order.model.enums.ReportBuyerInvoiceOutType;
import com.prcsteel.platform.order.model.model.Allowance;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ReportBuyerInvoiceOut;
import com.prcsteel.platform.order.model.model.ReportSellerInvoiceIn;
import com.prcsteel.platform.order.model.query.IncomeSummaryQuery;
import com.prcsteel.platform.order.model.query.InvoiceInListQuery;
import com.prcsteel.platform.order.model.query.InvoiceoutExpectQuery;
import com.prcsteel.platform.order.model.query.ReportBuyerInvoiceOutQuery;
import com.prcsteel.platform.order.model.query.ReportInvoiceInAndOutQuery;
import com.prcsteel.platform.order.model.query.ReportSellerInvoiceInDataQuery;

/**
 * Created by Rabbit Mao on 2015/8/19.
 */
public interface ReportFinanceService {
    List<UnInvoicedDto> queryUnInvoicedList(Map<String, Object> param);

    /**
     * 根据条件查询进项发票清单
     *
     * @param queryParam
     * @return
     */
    List<InvoiceInWithDetailDto> queryInvoiceInDetailListByParams(InvoiceInListQuery queryParam);

    /**
     * 根据条件查询进项发票清单总记录数
     *
     * @param queryParam
     * @return
     */
    int queryInvoiceInDetailTotalByParams(InvoiceInListQuery queryParam);

    List<UninvoicedInDto> queryUninvoicedInList(Map<String, Object> param);

    Integer countUninvoicedInList(Map<String, Object> param);

    List<InvoiceOutItemDetailDto> queryInvoiceoutExpect(InvoiceoutExpectQuery query);

    int queryInvoiceoutExpectCount(InvoiceoutExpectQuery query);
    
    void orderOperation(String operation,Long orderId,User user);

	void allowanceOperation(String operation, Allowance allowance, User user);

	void invoiceOperation(String operation, Long invoiceId, User user);

    void insertInvoiceOut(ReportBuyerInvoiceOut record);
    
    /**
     * 添加一个记录到report_buyer_invoice_out表里面，
     * @param order 		订单编号
     * @param operator		操作人
     * @param amount		金额变化
     * @param operateType	操作类型
     * @param number		当操作为：销售调价，销售调价回滚，开销项票的时候，number不为空
     */
    void pushToReportInvoiceOut(ConsignOrder order, User operator, BigDecimal amount, ReportBuyerInvoiceOutType operateType, String number);

    /**
	 * 客户销项报表列表
	 * @author tuxianming
	 * @return
	 */
	List<ReportBuyerInvoiceOutDto> queryReportBuyerInvoiceOutByPage(ReportBuyerInvoiceOutQuery query);
	/**
	 * 客户销项报表列表总条数
	 * @author tuxianming
	 * @param query
	 * @return
	 */
	Integer queryReportBuyerInvoiceOutTotalByPage(ReportBuyerInvoiceOutQuery query);
    
	/**
	 * 客户销项报表明细
	 * @author tuxianming
	 * @return
	 */
	List<ReportBuyerInvoiceOutDto> queryReportBuyerInvoiceOutDetailByPage(ReportBuyerInvoiceOutQuery query);
	/**
	 * 客户销项报表明细总条数
	 * @author tuxianming
	 * @param query
	 * @return
	 */
	Integer queryReportBuyerInvoiceOutDetailTotalByPage(ReportBuyerInvoiceOutQuery query);
	
	ReportBuyerInvoiceOut getBuyerInvoceOut(Long buyerId);

	void pushInvoiceToReportInvoiceOut(InvoiceOutItemDetailDto dto, String systemId, String djh, BigDecimal totalAmount);
	
	/**
	 * 供应商进项报表 总量
	 * @author dq
	 */
	Integer queryReportSellerInvoiceInDataCount(ReportSellerInvoiceInDataQuery query);
	/**
	 * 供应商进项报表 数据
	 * @author dq
	 */
	List<ReportSellerInvoiceInDto> queryReportSellerInvoiceInData(ReportSellerInvoiceInDataQuery query);
	/**
	 * 供应商进项详情报表 总量
	 * @author dq
	 */
	Integer queryReportSellerInvoiceInDetailDataCount(ReportSellerInvoiceInDataQuery query);
	/**
	 * 供应商进项详情报表 总量
	 * @author dq
	 */
	List<ReportSellerInvoiceIn> queryReportSellerInvoiceInDetailData(ReportSellerInvoiceInDataQuery query);
	/**
	 * 供应商进项报表 - 条件范围 - 期初期末金额
	 * @author dq
	 */
	ReportSellerInvoiceInDto queryReportSellerInvoiceInRangeAmount(ReportSellerInvoiceInDataQuery query);

	/**
	 * @author tuxianming
	 */
	Map<String, Map<String, List<IncomeSummaryDto>>> queryIncomeSummaryForBuyer(IncomeSummaryQuery query);


	/**
	 * @author tuxianming
	 */
	Map<String, Map<String, List<IncomeSummaryDto>>> queryIncomeSummaryForSeller(IncomeSummaryQuery query);
	
	/**
	 *  @author tuxianming
	 */
	IncomeSummaryDto totalIncomeSummaryForBuyer(IncomeSummaryQuery query);

	/**
	 *  @author tuxianming
	 */
	IncomeSummaryDto totalIncomeSummaryForSeller(IncomeSummaryQuery query);
	
	/**
	 * 查询应收发票报表汇总
	 * tuxianming 20160614
	 * @param query
	 * @return
	 */
	List<ReportInvoiceInAndOutDto> queryInvoiceIn(ReportInvoiceInAndOutQuery query);
	
	/**
	 * 查询应付发票报表汇总
	 * tuxianming 20160614
	 * @param query
	 * @return
	 */
	List<ReportInvoiceInAndOutDto> queryInvoiceOut(ReportInvoiceInAndOutQuery query);
	
	/**
	 * 查询应收发票报表总记录数
	 * tuxianming 20160614
	 * @param query
	 * @return
	 */
	int totalInvoiceIn(ReportInvoiceInAndOutQuery query);
	
	/**
	 * 查询应付发票报表总记录数
	 * tuxianming 20160614
	 * @param query
	 * @return
	 */
	int totalInvoiceOut(ReportInvoiceInAndOutQuery query);
	
	
}
