package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.CountOrder;
import com.prcsteel.platform.order.model.EcOrder;
import com.prcsteel.platform.order.model.query.OrderEcAppQuery;
import com.prcsteel.platform.common.dto.BuyerOrderDetailDto;
import com.prcsteel.platform.order.model.AppOrder;
import com.prcsteel.platform.order.model.dto.AccountOrderDto;
import com.prcsteel.platform.order.model.dto.BuyerTradeStatisticsDto;
import com.prcsteel.platform.order.model.dto.CertificateInvoiceDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderUpdateDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderWithDetailsDto;
import com.prcsteel.platform.order.model.dto.ContractListDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyStatusDto;
import com.prcsteel.platform.order.model.dto.NsortBusinessReportDto;
import com.prcsteel.platform.order.model.dto.OrderContactDto;
import com.prcsteel.platform.order.model.dto.OrderItemDetailDto;
import com.prcsteel.platform.order.model.dto.OrderPayMentDto;
import com.prcsteel.platform.order.model.dto.OrderTradeCertificateDto;
import com.prcsteel.platform.order.model.dto.SellerOrderBusinessReportDto;
import com.prcsteel.platform.order.model.dto.SellerTurnoverStatisticsDto;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.query.BuyerTradeStatisticsQuery;
import com.prcsteel.platform.order.model.query.ChecklistDetailQuery;
import com.prcsteel.platform.order.model.query.ConsignOrderDetailQuery;
import com.prcsteel.platform.order.model.query.ConsignOrderQuery;
import com.prcsteel.platform.order.model.query.OrderPayMentQuery;
import com.prcsteel.platform.order.model.query.OrderTradeCertificateQuery;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


public interface ConsignOrderDao {
	int deleteByPrimaryKey(Long id);

	int insert(ConsignOrder record);

	int insertSelective(ConsignOrder record);

	ConsignOrder selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(ConsignOrder record);

	int updateByConditionSelective(ConsignOrderUpdateDto record);

	int updateByPrimaryKey(ConsignOrder record);

	/**
	 * 根据条件分页查询当前登录人的订单列表 默认查询当前登录人的订单列表
	 *
	 * @param paramMap
	 *            ：map封装参数说明 key:value userId： 当前登录人id dto ：
	 *            封装的订单dto:ConsignOrderDto start： 开始记录（分页参数） length： 每页记录数（分页参数）
	 *            array： 订单状态数组，用于in条件
	 * @return
	 * @author dengxiyan
	 */
	List<ConsignOrderDto> selectByConditions(Map<String, Object> paramMap);

	/**
	 * 根据条件统计订单总数
	 *
	 * @param paramMap
	 *            ：map封装参数说明 key:value userId： 当前登录人id dto ：
	 *            封装的订单dto:ConsignOrderDto start： 开始记录（分页参数） length： 每页记录数（分页参数）
	 *            array： 订单状态数组，用于in条件
	 * @return
	 * @author dengxiyan
	 */
	int totalOrderByConditions(Map<String, Object> paramMap);

	/**
	 * 根据订单id查询订单信息
	 *
	 * @param id
	 * @return
	 */
	ConsignOrder queryById(@Param("id") long id);

	ConsignOrder selectByCode(String code);

	/**
	 * 根据交易员查询
	 */
	List<ConsignOrder> selectByOwnerAndStatusBuyer(Long id, int status);

	/**
	 * 获得当前登录人待操作的某状态下的订单总数
	 *
	 * @param paramMap
	 *            ：map封装参数说明 key:value userId：当前登录人id list： 订单状态list用于in条件
	 * @return
	 * @author dengxiyan
	 */
	int countOrderByStatus(Map<String, Object> paramMap);

	/**
	 * 统计当前登录人的某付款状态的订单总数
	 *
	 * @param paramMap
	 *            ：map封装参数说明 key:value userId：当前登录人id list： 订单状态list用于in条件
	 *            payStatus：订单的付款状态
	 * @return
	 * @author dengxiyan
	 */
	int countPayOrderByStatus(Map<String, Object> paramMap);

	/**
	 * 根据id和状态查询
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	ConsignOrder selectByIdAndStatus(Long id, String status);

	Integer selectCountByIdAndFillupStatus(Long id, int fillupStatus);

	/**
	 * 查询未付款用于系统自动关闭订单
	 *
	 * @return
	 */
	List<ConsignOrder> selectOrderByPayStatus();

	/**
	 * 根据条件分页查询买家客户的采购记录/卖家的销售记录
	 *
	 * @param paramMap
	 *            map封装参数说明 key:value userId： 当前登录人id dto ：
	 *            封装的订单dto:ConsignOrderDto start： 开始记录（分页参数） length： 每页记录数（分页参数）
	 * @return
	 * @author dengxiyan
	 */
	List<ConsignOrderDto> selectAccountOrderListByConditions(
			Map<String, Object> paramMap);

	/**
	 * 根据条件统计买家客户的采购记录/卖家的销售记录
	 *
	 * @param paramMap
	 *            map封装参数说明 key:value userId： 当前登录人id dto ：
	 *            封装的订单dto:ConsignOrderDto
	 * @return
	 * @author dengxiyan
	 */
	int totalAccountOrderByConditions(Map<String, Object> paramMap);

	/**
	 * 获取订单审核通过时间
	 *
	 * @param id
	 *            　订单编号
	 * @return
	 */
	public Date queryApprovedDateByOrderId(Long id);

	/**
	 * 统计当前登录人的付款情况的已关联订单总数列表
	 *
	 * @param paramMap
	 *            ：map封装参数说明 key:value userId：当前登录人id list： 订单状态list用于in条件
	 * @return
	 * @author dengxiyan
	 */
	List<Map<String, Object>> countOrderGroupByPayStatus(
			Map<String, Object> paramMap);

	/**
	 * 统计当前登录人能查看的状态订单总数列表
	 *
	 * @param paramMap
	 *            ：map封装参数说明 key:value userId：当前登录人id
	 * @return
	 * @author dengxiyan
	 */
	List<Map<String, Object>> countOrderGroupByStatus(
			Map<String, Object> paramMap);

	/**
	 * 查询卖家交易信息列表
	 *
	 * @param dto
	 * @return
	 */
	List<SellerOrderBusinessReportDto> querySellerTradeListByDto(
			SellerOrderBusinessReportDto dto);

	/**
	 * 统计满足条件的所有卖家的交易重量之和
	 *
	 * @param dto
	 * @return
	 */
	BigDecimal sumAllSellerWeightByDto(SellerOrderBusinessReportDto dto);

	/**
	 * 统计卖家交易记录数
	 *
	 * @param dto
	 * @return
	 */
	int countSellerTradeByDto(SellerOrderBusinessReportDto dto);

	/**
	 * 查询品类交易信息列表
	 *
	 * @param dto
	 * @return
	 */
	List<NsortBusinessReportDto> queryNsortTradeListByDto(
			NsortBusinessReportDto dto);

	/**
	 * 卖家成交统计报表
	 *
	 * @param param
	 * @return
	 */
	List<SellerTurnoverStatisticsDto> querySellerTurnoverStatisticsByParams(
			Map<String, Object> param);

	/**
	 * 卖家成交统计记录数
	 *
	 * @param param
	 * @return
	 */
	int countSellerTurnoverStatisticsByParams(Map<String, Object> param);

	/**
	 * 查找指定条件的订单
	 *
	 * @param query
	 * @return
	 */
	List<ConsignOrderWithDetailsDto> queryOrders(ConsignOrderDetailQuery query);

	/**
	 * 查找指定条件的订单总数
	 *
	 * @return
	 */
	int queryOrdersCount(ConsignOrderDetailQuery query);

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
	List<BuyerTradeStatisticsDto> queryBuyerTrade(
			BuyerTradeStatisticsQuery query);

	/**
	 * 买家交易报表数据总数
	 *
	 * @param query
	 * @return
	 */
	int queryBuyerTradeCount(BuyerTradeStatisticsQuery query);

	/**
	 * 根据订单id更新订单
	 * 
	 * @param id
	 * @param code
	 * @return
	 */
	int updateCodeById(@Param("id") Long id, @Param("code") String code);

	/**
	 * 批量修改订单状态
	 * 
	 * @param consignOrderQuery
	 *            查询对象
	 * @return 成功条数
	 */
	int updateStatusByConsignOrderQuery(ConsignOrderQuery consignOrderQuery);

	/**
	 * 根据订单详情ID 查询订单ID
	 * 
	 * @param orderDetailIds
	 *            查询对象
	 * @return orderIds
	 */
	List<Long> queryOrderIdsByOrderDetailIds(
			@Param("orderDetailIds") List<Long> orderDetailIds);

	/**
	 * 查询订单的状态
	 * 
	 * @param orderId
	 *            订单id
	 * @return
	 */
	public String findByOrderId(Long orderId);


	/**
	 * 查询订单是否使用银票支付
	 *
	 * @param orderId
	 *            订单id
	 * @return
	 */
	public boolean checkIsPayedByAcceptDraft(Long orderId);

	/**
	 * 查询银票是否已被使用
	 *
	 * @param acceptDraftId
	 *            银票id
	 * @return
	 */
	public boolean checkAcceptDarftIsPayed(Long acceptDraftId);
	
	/**
     * 查询订单列表
     *
     * @param userId 当前登录人
     * @param date 查询日期 yyyy-MM-dd
     * @return
     * @author gelinzhong
     */
    List<Map<String,Object>> appOrderList(@Param("userId") Long userId,
    		@Param("date") String date,@Param("start") Integer start,@Param("length") Integer length);
    

	public List<ConsignOrder> selectConsignOrder (@Param("orderIds") List<Long> orderIds);

	/**
	 * 根据采购单编号查询
	 * @param id 采购单编号
	 * @return
	 */
	ConsignOrder buildConsignOrderByPurchaseOrderId(Long id);
	
	/**
	 * 得到关于订单的未开销项票金额之和= Σ（销售订单已关联金额）+Σ（±二结发生额） - 订单关闭金额
	 * @param buyerId
	 * @return
	 */
	public BigDecimal querySumAmountByBuyerId(@Param("buyerId") Long buyerId);

	
	/**
	 * tuxianming
	 * 根据order ids 得到所有
	 */
	public List<ConsignOrder> selectByOrderIds(List<Long> orderIds);

	/**
	 * 根据单号查询返利金额
	 * @param code
	 * @author wangxianjun
	 * @return
	 */
	OrderContactDto queryOrderContactByCode(String code);
	/**
	 * 根据单号更新买家联系人信息
	 * @param record
	 * @author wangxianjun
	 * @return
	 */
	int updateByCodeSelective(ConsignOrder record);

	/**
	 * 除了这个订单以外，最早开单的已二结订单id
	 * @Author Rabbit
	 * @param accountId
	 * @return
	 */
	ConsignOrder selectEarliestOrderByAccountIdExceptOrderId(@Param("accountId") Long accountId, @Param("orderId") Long orderId);
	
	/**
     * 查进项票所关联到的订单的买家
     * @param invoiceId
     * @return
     */
    List<AccountOrderDto> queryByInvoiceId(Long invoiceId);
    
    /**
     * 根据订单id和invoiceId查询销项票开票状态
     * @param invoiceId
     * @param orderDetailIds
     * @return
     */
    InvoiceOutApplyStatusDto queryInvoiceOutApplyStatus(@Param("invoiceId") Long invoiceId, @Param("orderDetailIds") List<Long> orderDetailIds);
    
    List<ConsignOrder> getFebOrder();

	/**
     * 初次付款信息报表
     * @author lixiang
     * @param orderPayMentQuery
     * @return
     */
    List<OrderPayMentDto> queryFirstPayMent(OrderPayMentQuery orderPayMentQuery);
    
    /**
     * 初次付款信息报表总记录数
     * @author lixiang
     * @param orderPayMentQuery
     * @return
     */
    Integer queryFirstPayMentCount(OrderPayMentQuery orderPayMentQuery);
    
    /**
     * 合计初次付款信息报表的初次付款金额
     * @author lixiang
     * @param orderPayMentQuery
     * @return
     */
    OrderPayMentDto totalFirstPayMent(OrderPayMentQuery orderPayMentQuery);
    
	List<OrderItemDetailDto> selectOrdersByParams(OrderTradeCertificateQuery query);

	int selectTotalOrdersByParams(OrderTradeCertificateQuery query);
	//通过买家找到对应任意一个卖家
	Long findSellerIdBybuyer(Map buyMap);

	List<OrderTradeCertificateDto> selectTradeCertificateList(OrderTradeCertificateQuery query);
	//根据凭证号查询所有对应的订单id
	List<Long> findOrderIdIdByCert(String cert);
	//根据买家凭证号查询买家对应卖家凭证
	String findSellerCertBybuyerCert(String cert);
	//根据买家凭证号查询买家对应卖家凭证
	String findBuyerCertBySellerCert(String cert);

	/**
	 *查询出大于等于开单日期的所有订单
	 * @author peanut
	 * @param openOrderDate 开单日期
	 * @return
     */
	List<CertificateInvoiceDto> selectOrderUpOpenDate(@Param("openOrderDate") String openOrderDate, @Param("accountType")String accountType);
	
	//查询合同清单 add by wangxianjun
	List<ContractListDto>  selectContractList(ChecklistDetailQuery query);
	//统计合同清单 add by wangxianjun
	int countContractList(ChecklistDetailQuery query);

	/**
	 *
	 * @Author: chengui
	 * @Description: 按买家账户ID、订单状态查询订单信息
	 * @param accountId 买家账户ID
	 * @param status 订单状态
	 */
	List<ConsignOrder> selectByAccountIdAndStatus(@Param("accountId")Long accountId, @Param("status")String status);
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 超市用户ID查询对应的订单信息
	 * @param query 超市用户ID
	 */
    List<AppOrder> selectOrderByecUserId(OrderEcAppQuery query);
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 清空订单明细中买家凭证号
	 * @param map
	 */
	int updateOrderByCertCode(Map map);
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 清空订单明细中买家凭证号
	 * @param map
	 */
	int updateOrderByBatchCertCode(Map map);
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 超市用户ID查询对应的订单数量
	 * @param query 超市用户ID
	 */
	int countOrderByecUserId(OrderEcAppQuery query);

	/**
	 * tuxianming
	 * 根据order ids 得到所有
	 */
	public List<EcOrder> selectEcOrdersByOrderIds(List<Long> orderIds);
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 超市用户ID查询对应的订单各状态数量
	 * @param userId 超市用户ID
	 */
	CountOrder countEcOrder(@Param("userId")Integer userId);

}

