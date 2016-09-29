package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.AppOrderItems;
import com.prcsteel.platform.order.model.EcOrderItems;
import com.prcsteel.platform.order.model.dto.AllowanceOrderItemsDto;
import com.prcsteel.platform.order.model.dto.BalanceDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderItemsDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderItemsInfoDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderItemsInvoiceDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderSellerInfoDto;
import com.prcsteel.platform.order.model.dto.IncomeSummaryDto;
import com.prcsteel.platform.order.model.dto.IncreaseInvoiceInDto;
import com.prcsteel.platform.order.model.dto.InvoiceApplicationDetailDto;
import com.prcsteel.platform.order.model.dto.OrderDetailModifier;
import com.prcsteel.platform.order.model.dto.OrderItemsDto;
import com.prcsteel.platform.order.model.dto.OrderItemsInvoiceInDto;
import com.prcsteel.platform.order.model.dto.PurchaseRecordDto;
import com.prcsteel.platform.order.model.dto.ReportInvoiceInAndOutDto;
import com.prcsteel.platform.order.model.dto.UnInvoicedDto;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.InvoiceInDetailOrderItem;
import com.prcsteel.platform.order.model.query.AllowanceOrderQuery;
import com.prcsteel.platform.order.model.query.ConsignOrderDetailQuery;
import com.prcsteel.platform.order.model.query.ConsignOrderItemsQuery;
import com.prcsteel.platform.order.model.query.IncomeSummaryQuery;
import com.prcsteel.platform.order.model.query.OrderItemsForInvoiceInQuery;
import com.prcsteel.platform.order.model.query.OrderItemsQuery;
import com.prcsteel.platform.order.model.query.ReportInvoiceInAndOutQuery;
import com.prcsteel.platform.order.model.query.ReportSellerInvoiceInQuery;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ConsignOrderItemsDao {
    int deleteByPrimaryKey(Long id);

    int insert(ConsignOrderItems record);

    int insertSelective(ConsignOrderItems record);

    ConsignOrderItems selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConsignOrderItems record);

    int updateByPrimaryKey(ConsignOrderItems record);

    List<ConsignOrderItems> selectByOrderIdAndSellerId(Long orderId, Long sellerId);

    List<Long> selectSellerIdByOrderId(Long orderId);

    List<ConsignOrderItems> selectByOrderId(Long orderId);

    int queryCountNotMatch(Long orderId);
    /**
     * 根据订单ID删除订单资源
     *
     * @param orderId 订单ID
     * @return
     */
    int deleteByOrderId(Long orderId);

    List<ConsignOrderSellerInfoDto> getSellerInfo(Long orderId);

    /**
     * 获取某业务员下某公司未开金额列表
     * @param sellerId 业务员编号
     * @param accountId　公司编号
     * @return
     */
    List<BalanceDto> queryAmountList(@Param("sellerId")Long sellerId, @Param("accountId") Long accountId);

    /**
     * 根据条件分页查询未开销项发票清单
     * @param param
     * @return
     */
    List<UnInvoicedDto> queryUnInvoicedList(Map<String, Object> param);


    List<ConsignOrderItemsInfoDto> queryOrderdetails(ConsignOrderDetailQuery query);

    List<ConsignOrderItemsInfoDto> queryOrderdetailsSecond(ConsignOrderDetailQuery query);
     /* 根据订单ID号，统计针对买家的合同金额（即销售金额：成交价*重量）
     * @return
     */
    BigDecimal countContractAmountByOrderId(@Param("orderId")Long orderId);
    
    List<OrderItemsInvoiceInDto> queryOrderItemsForInvoiceIn(OrderItemsForInvoiceInQuery query);
    
    List<OrderItemsInvoiceInDto> queryOrderItemsForInvoiceInByDetailId(@Param("detailId") Long detailId);
    
	List<InvoiceApplicationDetailDto> queryInvoiceApplicationDetailDtoByBuyerId(ConsignOrderItemsQuery consignOrderItemsQuery);

    int increaseInvoiceIn(IncreaseInvoiceInDto updInvoice);
    
    int restoreOrderitemInvoiceIn(InvoiceInDetailOrderItem invoiceDetail);
    
    int reEffectiveInvoiceIn(InvoiceInDetailOrderItem invoiceDetail);
    
    List<ConsignOrderItems> queryOrderItemsByDetailIds(List<Long> detailIds);
    
    List<ConsignOrderItems> queryByIds(List<Long> ids);

    /**
     * 查询实提重量与已开重量相等的订单Id集合
     * @param consignOrderItemsQuery  查询对象
     * @return                        根据订单Id合并处理的数据集合
     */
    List<Long> queryWeightByConsignOrderItemsQuery(ConsignOrderItemsQuery consignOrderItemsQuery);

	/**
	 * 查询买卖家订单记录
	 *
	 * @param allowanceOrderQuery
	 * 
	 * @return List<AllowanceOrderItemsDto>
	 */
	public List<AllowanceOrderItemsDto> queryOrderItems(AllowanceOrderQuery allowanceOrderQuery);

	List<Long> selectBuyerIdsBySellerId(Long sellerId);
	
	int updateOrderDetail (OrderDetailModifier orderDetailModifier);

    /**
     * 根据订单id获取这笔订单使用的银票号
     * @param orderId
     * @return
     */
    List<String> selectAcceptDraftCodeByOrderId(Long orderId);

    List<OrderItemsInvoiceInDto> queryClientCanceledOrderItemsForInvoiceIn(OrderItemsForInvoiceInQuery query);

    int batchUpdateAcceptDraf(List<ConsignOrderItems> list);

	BigDecimal querySumAmountBySellerId(ReportSellerInvoiceInQuery reportSellerInvoiceInQuery);

    /**
     * 销项票清单
     * @param orderItemsQuery
     * @return
     */
    List<OrderItemsDto> selectTicketList(OrderItemsQuery orderItemsQuery);
    
    /**
     * 销项票清单总记录数
     * @param orderItemsQuery
     * @return
     */
    Integer queryTicketTotal(OrderItemsQuery orderItemsQuery);
    
    /**
     * 未开票合计
     * @param orderItemsQuery
     * @return
     */
    OrderItemsDto totalTicketList(OrderItemsQuery orderItemsQuery);

	/**
     * 获取当天交易所有订单项
     * @return
     */
    List<ConsignOrderItemsDto> queryOrderItemsOfToday();

    /**
     * 根据报价单编号查询
     * @param id 报价单编号
     * @return
     */
    List<ConsignOrderItemsInfoDto> buildOrderItemsByQuotationOrderId(Long id);
    
    /**
     * 根据资源id查询出所有的开单信息
     * @param ids
     * @return
     */
    List<ConsignOrderItemsInfoDto> buildOrderItemsByResourceId(List<Long> ids);

    /**
     * 根据订单明细ID查询
     * author wangxianjun
     * @param id
     * @return
     */
     ConsignOrderItemsInvoiceDto queryOrderItemsById(Long id);
     
     List<IncomeSummaryDto> incomeSummaryForBuyer(IncomeSummaryQuery incomeQuery);
     
     List<IncomeSummaryDto> incomeSummaryForSeller(IncomeSummaryQuery incomeQuery);

     IncomeSummaryDto totalIncomeSummaryForBuyer(IncomeSummaryQuery incomeQuery);
     IncomeSummaryDto totalIncomeSummaryForSeller(IncomeSummaryQuery incomeQuery);

	void updateByOrderIdSelective(ConsignOrderItems item);
     
	List<AllowanceOrderItemsDto> querySellerQuantity(Map<String, Object> paramMap);
	List<AllowanceOrderItemsDto> querySellerOrgQuantity(Map<String, Object> paramMap);

	
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

    /**
     * cbms接口
     * 获取采购历史记录
     * @param phone
     * @return
     */
    List<PurchaseRecordDto> findPurchaseRecord(@Param("phone")String phone);

    /**
     *
     * @Author: wangxianjun
     * @Description: 订单ID查询对应的订单明细信息
     * @param orderId 订单ID
     */
    List<AppOrderItems> selectItemsByorderId(@Param("orderId")Long orderId);
    /**
     *
     * @Author: wangxianjun
     * @Description: 清空订单明细中卖家凭证号
     * @param map
     */
    int updateOrderItemsByCertCode(Map map);
    /**
     *
     * @Author: wangxianjun
     * @Description: 清空订单明细中卖家凭证号
     * @param map
     */
    int updateOrderItemsByBatchCertCode(Map map);

    /**
     *
     * @Author: wangxianjun
     * @Description: 订单ID查询对应的订单明细信息
     * @param orderId 订单ID
     */
    List<EcOrderItems> selectEcItemsByorderId(@Param("orderId")Long orderId);

    List<ConsignOrderItems> selectUnionAllByOrderId(Long orderId);

}