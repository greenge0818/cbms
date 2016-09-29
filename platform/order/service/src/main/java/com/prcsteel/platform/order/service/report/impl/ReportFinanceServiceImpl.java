package com.prcsteel.platform.order.service.report.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.utils.CbmsNumberUtil;
import com.prcsteel.platform.order.model.dto.AllowanceOrderItemsDto;
import com.prcsteel.platform.order.model.dto.IncomeSummaryDto;
import com.prcsteel.platform.order.model.dto.InvoiceInWithDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutItemDetailDto;
import com.prcsteel.platform.order.model.dto.ReportBuyerInvoiceOutDto;
import com.prcsteel.platform.order.model.dto.ReportInvoiceInAndOutDto;
import com.prcsteel.platform.order.model.dto.ReportSellerInvoiceInDto;
import com.prcsteel.platform.order.model.dto.UnInvoicedDto;
import com.prcsteel.platform.order.model.dto.UninvoicedInDto;
import com.prcsteel.platform.order.model.enums.ConsignOrderPayStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.enums.InvoiceInStatus;
import com.prcsteel.platform.order.model.enums.ReportBuyerInvoiceOutType;
import com.prcsteel.platform.order.model.enums.ReportSellerInvoiceInOperationType;
import com.prcsteel.platform.order.model.model.Allowance;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderContract;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.InvoiceIn;
import com.prcsteel.platform.order.model.model.PoolIn;
import com.prcsteel.platform.order.model.model.ReportBuyerInvoiceOut;
import com.prcsteel.platform.order.model.model.ReportSellerInvoiceIn;
import com.prcsteel.platform.order.model.query.AllowanceDetailItemQuery;
import com.prcsteel.platform.order.model.query.IncomeSummaryQuery;
import com.prcsteel.platform.order.model.query.InvoiceInListQuery;
import com.prcsteel.platform.order.model.query.InvoiceoutExpectQuery;
import com.prcsteel.platform.order.model.query.ReportBuyerInvoiceOutQuery;
import com.prcsteel.platform.order.model.query.ReportInvoiceInAndOutQuery;
import com.prcsteel.platform.order.model.query.ReportSellerInvoiceInDataQuery;
import com.prcsteel.platform.order.model.query.ReportSellerInvoiceInQuery;
import com.prcsteel.platform.order.persist.dao.AllowanceDao;
import com.prcsteel.platform.order.persist.dao.AllowanceOrderDetailItemDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderContractDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsDao;
import com.prcsteel.platform.order.persist.dao.InvoiceInDao;
import com.prcsteel.platform.order.persist.dao.InvoiceInDetailDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutCheckListDetailDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutItemDetailDao;
import com.prcsteel.platform.order.persist.dao.PoolInDao;
import com.prcsteel.platform.order.persist.dao.ReportBusinessDao;
import com.prcsteel.platform.order.persist.dao.ReportBuyerInvoiceOutDao;
import com.prcsteel.platform.order.persist.dao.ReportSellerInvoiceInDao;
import com.prcsteel.platform.order.service.allowance.AllowanceOrderDetailItemService;
import com.prcsteel.platform.order.service.report.ReportFinanceService;

/**
 * Created by Rabbit Mao on 2015/8/19.
 */
@Service("reportFinanceService")
public class ReportFinanceServiceImpl implements ReportFinanceService {

	private static final Logger logger = LoggerFactory.getLogger(ReportFinanceServiceImpl.class);

	@Resource
	private ConsignOrderItemsDao itemsDao;
	@Resource
	private ReportBusinessDao reportBusinessDao;
	@Autowired
	private InvoiceOutItemDetailDao invoiceOutItemDetailDao;

	@Resource
	private InvoiceInDetailDao invoiceInDetailDao;

	@Resource
	private ReportSellerInvoiceInDao reportSellerInvoiceInDao;

	@Resource
	private PoolInDao poolInDao;

	@Resource
	private InvoiceInDao invoiceInDao;

	@Resource
	private ConsignOrderItemsDao consignOrderItemsDao;

	@Resource
	private AllowanceDao allowanceDao;

	@Resource
	AllowanceOrderDetailItemService allowanceOrderDetailItemService;

	@Resource
	private ReportBuyerInvoiceOutDao reportBuyerInvoiceOutDao;

	@Resource
	private ConsignOrderDao consignOrderDao;
	@Resource
	InvoiceOutCheckListDetailDao invoiceOutCheckListDetailDao;
	@Resource
	AllowanceOrderDetailItemDao allowanceOrderDetailItemDao;
	@Resource
	ConsignOrderContractDao consignOrderContractDao;

	@Override
	public List<UnInvoicedDto> queryUnInvoicedList(Map<String, Object> param) {
		return itemsDao.queryUnInvoicedList(param);
	}

	/**
	 * 根据条件查询进项发票清单
	 *
	 * @param queryParam
	 * @return
	 */
	@Override
	public List<InvoiceInWithDetailDto> queryInvoiceInDetailListByParams(InvoiceInListQuery queryParam) {
		List<InvoiceInWithDetailDto> list = invoiceInDetailDao.queryInvoiceInDetailListByParams(queryParam);
		if (list != null && list.size() > 0) {
			//设置状态的名称
			list = list.stream().peek(a -> a.setStatus(InvoiceInStatus.getName(a.getStatus()))).collect(Collectors.toList());
		}
		return list;
	}

	/**
	 * 根据条件查询进项发票清单总记录数
	 *
	 * @param queryParam
	 * @return
	 */
	@Override
	public int queryInvoiceInDetailTotalByParams(InvoiceInListQuery queryParam) {
		return invoiceInDetailDao.queryInvoiceInDetailTotalByParams(queryParam);
	}

	@Override
	public List<UninvoicedInDto> queryUninvoicedInList(Map<String, Object> param) {
		return reportBusinessDao.queryUninvoicedInList(param);
	}

	@Override
	public Integer countUninvoicedInList(Map<String, Object> param) {
		return reportBusinessDao.countUninvoicedInList(param);
	}

	/** 未开销项发票清单
	 * @param query
	 * @return
	 */
	@Override
	public List<InvoiceOutItemDetailDto> queryInvoiceoutExpect(InvoiceoutExpectQuery query) {
		return invoiceOutItemDetailDao.queryInvoiceoutExpect(query);
	}

	/**
	 * 未开销项发票清单总数
	 * @param query
	 * @return
	 */
	@Override
	public int queryInvoiceoutExpectCount(InvoiceoutExpectQuery query) {
		return invoiceOutItemDetailDao.queryInvoiceoutExpectCount(query);
	}

	/**
	 * 供应商进项报表踩点 - 订单部分
	 * @author dq
	 * @return
	 */
	@Override
	public void orderOperation(String operation,Long orderId,User user) {
		try {
			Date date = new Date();
			ConsignOrder order = consignOrderDao.selectByPrimaryKey(orderId);
			List<ConsignOrderItems> itemList = consignOrderItemsDao.selectByOrderId(orderId);
			List<Long> sellerIds = itemList.stream().map(
					a -> a.getSellerId()).distinct().collect(Collectors.toList());
			for(int i=0 ; i<sellerIds.size() ; i++) {
				Long sellerId = sellerIds.get(i);
				List<ConsignOrderItems> tempItemList = itemList.stream().filter(
						a -> a.getSellerId().equals(sellerId)).collect(Collectors.toList());
				ConsignOrderItems tempItem = tempItemList.stream().findFirst().get();

				ReportSellerInvoiceIn reportSellerInvoiceIn = new ReportSellerInvoiceIn();
				reportSellerInvoiceIn.setSellerId(tempItem.getSellerId());
				reportSellerInvoiceIn.setSellerName(tempItem.getSellerName());
				// 保留修改记录
				reportSellerInvoiceIn.setCreated(date);
				reportSellerInvoiceIn.setCreatedBy(user.getName());
				reportSellerInvoiceIn.setLastUpdated(date);
				reportSellerInvoiceIn.setLastUpdatedBy(user.getName());
				// 设置修改次数
				reportSellerInvoiceIn.setModificationNumber(0);
				// 业务发生时间
				reportSellerInvoiceIn.setHappenTime(date);

				// 设置报表数据
				ConsignOrderContract consignOrderContract = consignOrderContractDao.queryByOrderIdAndCustomerId(order.getId(),sellerId);
				reportSellerInvoiceIn.setContractCode(consignOrderContract.getContractCodeAuto());
				reportSellerInvoiceIn.setOrderId(order.getId());
				reportSellerInvoiceIn.setOrderCode(order.getCode());

				// 初次付款
				if(operation.equals(ReportSellerInvoiceInOperationType.FirstPayment.getOperation())) {
					reportSellerInvoiceIn.setOrderAmount(
							new BigDecimal(
									tempItemList.stream().mapToDouble(
											a -> CbmsNumberUtil.formatMoney(a.getWeight().multiply(a.getCostPrice()))).sum()
									));
					reportSellerInvoiceIn.setRemark(ReportSellerInvoiceInOperationType.FirstPayment.getRemark(order.getCode()));
					// 订单关闭
				}else if(operation.equals(ReportSellerInvoiceInOperationType.OrderClose.getOperation())) {
					// -7 二结后
					Double tempAmount = tempItemList.stream().mapToDouble(
							a -> a.getActualPickWeightServer().doubleValue()).sum();
					if(tempAmount != 0) {
						reportSellerInvoiceIn.setOrderAmount(
								new BigDecimal(
										tempItemList.stream().mapToDouble(
												a -> CbmsNumberUtil.formatMoney(a.getActualPickWeightServer().multiply(a.getCostPrice()))).sum()
										).negate());
					}else {
						// -2 -4 -8二结前
						reportSellerInvoiceIn.setOrderAmount(
								new BigDecimal(
										tempItemList.stream().mapToDouble(
												a -> CbmsNumberUtil.formatMoney(a.getWeight().multiply(a.getCostPrice()))).sum()
										).negate());
					}
					reportSellerInvoiceIn.setRemark(ReportSellerInvoiceInOperationType.OrderClose.getRemark(order.getCode()));
					// 二结
				}else if(operation.equals(ReportSellerInvoiceInOperationType.SecondSettlement.getOperation())) {
					BigDecimal orderAmount = BigDecimal.ZERO;
					BigDecimal amount = BigDecimal.ZERO;
					orderAmount = new BigDecimal(
							tempItemList.stream().mapToDouble(
									a -> CbmsNumberUtil.formatMoney(a.getWeight().multiply(a.getCostPrice()))).sum()
							);
					amount = new BigDecimal(
							tempItemList.stream().mapToDouble(
									a -> CbmsNumberUtil.formatMoney(a.getActualPickWeightServer().multiply(a.getCostPrice()))).sum()
							);
					reportSellerInvoiceIn.setOrderAmount(amount.subtract(orderAmount));
					reportSellerInvoiceIn.setRemark(ReportSellerInvoiceInOperationType.SecondSettlement.getRemark(order.getCode()));
				}

				reportSellerInvoiceIn.setOperationType(operation);

				// 保存经销商进项报表数据
				int flag = saveReportSellerInvoiceIn(reportSellerInvoiceIn);
				if(flag != 1) {
					logger.warn("经销商进项报表数据踩点失败，订单踩点插入失败！ reportSellerInvoiceIn:" + reportSellerInvoiceIn.toString());
				}
			}
		} catch (Exception e) {
			logger.error("经销商进项报表数据踩点失败，订单踩点失败！"
					+ "operation：" + operation + " , orderId:" + orderId
					+ " , userID:" + user.getId() + " , userName" + user.getName(), e);
		}
	}

	/**
	 * 供应商进项报表踩点 - 折让部分
	 * @author dq
	 * @return
	 */
	@Override
	public void allowanceOperation(String operation, Allowance allowance, User user) {
		try {
			Date date = new Date();
			AllowanceDetailItemQuery detailItemQuery = new AllowanceDetailItemQuery();
			detailItemQuery.setId(allowance.getId());
			List<AllowanceOrderItemsDto> orderList = allowanceOrderDetailItemService.queryDetails(detailItemQuery);
			List<Long> orderIds = orderList.stream().map(
					a -> a.getOrderId()).distinct().collect(Collectors.toList());
			orderIds.forEach(orderId -> {
				List<AllowanceOrderItemsDto> tempOrderList = orderList.stream().filter(
						a ->  a.getOrderId().equals(orderId)).collect(Collectors.toList());
				Long sellerId = allowance.getAccountId();


				ReportSellerInvoiceIn reportSellerInvoiceIn = new ReportSellerInvoiceIn();
				reportSellerInvoiceIn.setSellerId(sellerId);
				reportSellerInvoiceIn.setSellerName(allowance.getAccountName());
				// 保留修改记录
				reportSellerInvoiceIn.setCreated(date);
				reportSellerInvoiceIn.setCreatedBy(user.getName());
				reportSellerInvoiceIn.setLastUpdated(date);
				reportSellerInvoiceIn.setLastUpdatedBy(user.getName());
				// 设置修改次数
				reportSellerInvoiceIn.setModificationNumber(0);
				// 业务发生时间
				reportSellerInvoiceIn.setHappenTime(date);

//				// 设置报表数据
//				ConsignOrder order = consignOrderDao.selectByPrimaryKey(tempOrderList.stream().findFirst().get().getOrderId());
//				ConsignOrderContract consignOrderContract = consignOrderContractDao.queryByOrderIdAndCustomerId(order.getId(),sellerId);
//				reportSellerInvoiceIn.setContractCode(consignOrderContract.getContractCodeAuto());
//				reportSellerInvoiceIn.setOrderId(order.getId());
//				reportSellerInvoiceIn.setOrderCode(order.getCode());

				// 采购调价单号
				if(operation.equals(ReportSellerInvoiceInOperationType.Allowance.getOperation())) {
					reportSellerInvoiceIn.setOrderAmount(new BigDecimal(
							tempOrderList.stream().mapToDouble(
									a -> a.getAllowanceAmount().doubleValue()).sum()
							));
					reportSellerInvoiceIn.setRemark(ReportSellerInvoiceInOperationType.Allowance.getRemark(allowance.getAllowanceCode()));
					// 调价回滚
				}else if(operation.equals(ReportSellerInvoiceInOperationType.UnAllowance.getOperation())) {
					reportSellerInvoiceIn.setOrderAmount(new BigDecimal(
							tempOrderList.stream().mapToDouble(
									a -> a.getAllowanceAmount().doubleValue()).sum()
							).negate());
					reportSellerInvoiceIn.setRemark(ReportSellerInvoiceInOperationType.UnAllowance.getRemark(allowance.getAllowanceCode()));
				}

				reportSellerInvoiceIn.setOperationType(operation);

				// 保存经销商进项报表数据
				int flag = saveReportSellerInvoiceIn(reportSellerInvoiceIn);
				if(flag != 1) {
					logger.warn("经销商进项报表数据踩点失败，折让踩点插入失败！ reportSellerInvoiceIn:" + reportSellerInvoiceIn.toString());
				}
			});
		} catch (Exception e) {
			logger.error("经销商进项报表数据踩点失败，折让踩点失败！"
					+ "operation：" + operation + " , allowanceId:" + allowance.getId()
					+ " , userID:" + user.getId() + " , userName" + user.getName(), e);
		}

	}

	/**
	 * 供应商进项报表踩点 - 进项票部分
	 * @author dq
	 * @return
	 */
	@Override
	public void invoiceOperation(String operation, Long invoiceId, User user) {
		try{
			Date date = new Date();
			InvoiceIn invoice = invoiceInDao.selectByPrimaryKey(invoiceId);

			ReportSellerInvoiceIn reportSellerInvoiceIn = new ReportSellerInvoiceIn();
			reportSellerInvoiceIn.setSellerId(invoice.getSellerId());
			reportSellerInvoiceIn.setSellerName(invoice.getSellerName());
			// 保留修改记录
			reportSellerInvoiceIn.setCreated(date);
			reportSellerInvoiceIn.setCreatedBy(user.getName());
			reportSellerInvoiceIn.setLastUpdated(date);
			reportSellerInvoiceIn.setLastUpdatedBy(user.getName());
			// 设置修改次数
			reportSellerInvoiceIn.setModificationNumber(0);
			// 业务发生时间
			reportSellerInvoiceIn.setHappenTime(date);

			// 进项票确认
			if(operation.equals(ReportSellerInvoiceInOperationType.InvoiceIn.getOperation())) {
				reportSellerInvoiceIn.setInvoiceInAmount(invoice.getTotalAmount());
				reportSellerInvoiceIn.setRemark(ReportSellerInvoiceInOperationType.InvoiceIn.getRemark(invoice.getCode()));
				// 进项票取消确认
			}else if(operation.equals(ReportSellerInvoiceInOperationType.UnInvoiceIn.getOperation())) {
				reportSellerInvoiceIn.setInvoiceInAmount(invoice.getTotalAmount().negate());
				reportSellerInvoiceIn.setRemark(ReportSellerInvoiceInOperationType.UnInvoiceIn.getRemark(invoice.getCode()));
			}

			reportSellerInvoiceIn.setOperationType(operation);

			// 保存经销商进项报表数据
			int flag = saveReportSellerInvoiceIn(reportSellerInvoiceIn);
			if(flag != 1) {
				logger.warn("经销商进项报表数据踩点失败，进项票踩点插入失败！ reportSellerInvoiceIn:" + reportSellerInvoiceIn.toString());
			}
		} catch (Exception e) {
			logger.error("经销商进项报表数据踩点失败，进项票踩点失败！"
					+ "operation：" + operation + " , invoiceId:" + invoiceId
					+ " , userID:" + user.getId() + " , userName" + user.getName(), e);
		}
	}

	/*
	 * 保存经销商进项报表数据
	 * @author dq
	 * @return
	 */
	private int saveReportSellerInvoiceIn(ReportSellerInvoiceIn reportSellerInvoiceIn) {
		ReportSellerInvoiceInQuery reportSellerInvoiceInQuery = new ReportSellerInvoiceInQuery();
		reportSellerInvoiceInQuery.setSellerId(reportSellerInvoiceIn.getSellerId());
		BigDecimal invoiceInBalance = BigDecimal.ZERO;
		BigDecimal orderAmount = reportSellerInvoiceIn.getOrderAmount();
		if(orderAmount == null) {
			orderAmount = BigDecimal.ZERO;
		}
		BigDecimal invoiceInAmount = reportSellerInvoiceIn.getInvoiceInAmount();
		if(invoiceInAmount == null) {
			invoiceInAmount = BigDecimal.ZERO;
		}
		// 根据卖家Id 和修改次数为0 查找上一次记录
		ReportSellerInvoiceIn maxReportSellerInvoiceIn = reportSellerInvoiceInDao.queryLastReportSellerInvoice(reportSellerInvoiceInQuery);
		if(null != maxReportSellerInvoiceIn) {
			// 获取 应收进项余额：发生本次操作之前的结余+本次操作交易发生额-本次操作进项票发生额
			reportSellerInvoiceInQuery.setMaxReportSellerInvoiceIn(maxReportSellerInvoiceIn);
			BigDecimal lastInvoiceInBalance = getLastInvoiceInBalance(reportSellerInvoiceInQuery);
			invoiceInBalance = lastInvoiceInBalance.add(orderAmount).subtract(invoiceInAmount);
		}else {
			invoiceInBalance = getInvoiceInBalance(reportSellerInvoiceInQuery);
		}
		reportSellerInvoiceIn.setInvoiceInBalance(invoiceInBalance);
		return reportSellerInvoiceInDao.insertSelective(reportSellerInvoiceIn);
	}

	/*
	 * 获取上一次应收进项余额
	 * @author dq
	 * @return
	 */
	private BigDecimal getLastInvoiceInBalance(ReportSellerInvoiceInQuery reportSellerInvoiceInQuery) {
		BigDecimal lastInvoiceInBalance = BigDecimal.ZERO;
		reportSellerInvoiceInQuery.getMaxReportSellerInvoiceIn().setModificationNumber(1);
		int flag = 0;
		for(int i=1 ; i<=5 ; i++) {
			flag = reportSellerInvoiceInDao.updateByPrimaryKeySelective(reportSellerInvoiceInQuery.getMaxReportSellerInvoiceIn());
			if(flag != 1 && i != 5) {
				try{
					Thread.sleep(500);
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}else {
				break;
			}
		}
		if(flag != 1) {
			logger.warn("重新计算应收进项余额");
			lastInvoiceInBalance = CbmsNumberUtil.buildMoney(getInvoiceInBalance(reportSellerInvoiceInQuery));
		}else {
			lastInvoiceInBalance = CbmsNumberUtil.buildMoney(reportSellerInvoiceInQuery.getMaxReportSellerInvoiceIn().getInvoiceInBalance());
		}
		return lastInvoiceInBalance;
	}

	/**
	 * 插入一条销项记录
	 * @author tuxianming
	 */
	@Override
	public void insertInvoiceOut(ReportBuyerInvoiceOut record) {

		BigDecimal invoiceOutBalance = null;

		//计算期初余额
		//根据买家Id
		ReportBuyerInvoiceOut lastRecord = reportBuyerInvoiceOutDao.selectByBuyer(record.getBuyerId());
		//如果没有得到记录，则重新计算期初余额
		if(lastRecord==null){
			invoiceOutBalance  = calculateInvoiceOutBalance(record.getBuyerId());


		}else{
			//期初余额  = Σ期初未开销项金额+Σ交易发生额-Σ销项票发生额
			invoiceOutBalance = getLastInvoiceOutBalance(lastRecord, record.getBuyerId());
		}

		if(invoiceOutBalance==null)
			invoiceOutBalance = BigDecimal.ZERO;

		//如果lastRecord为空，则说明：该买家第一次生成订单，所以在报表里面，之前是没有数据的。
		//因此期初余额是通过：calculateInvoiceOutBalance得到的，
		//所以当前的要插入的记录的金额就已经在：calculateInvoiceOutBalance中计算过了。
		//因此：当lastRecord为空的时候，则不需要在增加
		if(lastRecord!=null && record.getOrderAmount()!=null)
			invoiceOutBalance = invoiceOutBalance.add(record.getOrderAmount());
		if(lastRecord!=null && record.getInvoiceOutAmount()!=null)
			invoiceOutBalance = invoiceOutBalance.subtract(record.getInvoiceOutAmount());

		record.setInvoiceOutBalance(invoiceOutBalance);

		reportBuyerInvoiceOutDao.insertSelective(record);

	}

	/**
	 * 计算本次插入时：该买家：应开销项余额（一般用于第一次插入）
	 * 未开销项票金额之和= Σ（销售订单已关联金额）+Σ（±二结发生额）-Σ （已开销项金额）+调价金额 -订单关闭金额；
	 * @param lastRecord 
	 * @return
	 */
	private BigDecimal calculateInvoiceOutBalance(Long buyerId) {

		//Σ（销售订单已关联金额）+Σ（±二结发生额） -订单关闭金额
		BigDecimal balance = consignOrderDao.querySumAmountByBuyerId(buyerId);
		if(balance==null)
			balance = BigDecimal.ZERO;
		//折让调价
		BigDecimal allowanceAmount = allowanceOrderDetailItemDao.queryAllowanceAmount(buyerId);
		if(allowanceAmount==null)
			allowanceAmount = BigDecimal.ZERO;

		//已开销项金额
		BigDecimal invoiceOutAmount = invoiceOutCheckListDetailDao.querySumAmountByBuyerId(buyerId);
		if(invoiceOutAmount==null)
			invoiceOutAmount = BigDecimal.ZERO;

		return balance.add(allowanceAmount).subtract(invoiceOutAmount);
	}

	//获取上一次应收销项余额
	private BigDecimal getLastInvoiceOutBalance(ReportBuyerInvoiceOut lastRecord, Long buyerId) {
		BigDecimal lastInvoiceInBalance = BigDecimal.ZERO;
		if(lastRecord==null)
			return lastInvoiceInBalance;
		lastRecord.setModificationNumber(1);
		int flag = 0;
		for(int i=1 ; i<=5 ; i++) {
			flag = reportBuyerInvoiceOutDao.updateByPrimaryKeySelective(lastRecord);
			if(flag != 1 && i != 5) {
				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}else {
				break;
			}
		}
		if(flag != 1) {
			lastInvoiceInBalance = calculateInvoiceOutBalance(buyerId);
		}else {
			lastInvoiceInBalance = lastRecord.getInvoiceOutBalance();
		}
		return lastInvoiceInBalance;
	}

	/*
	 * 重新计算应收进项余额
	 * @author dq
	 * @return
	 */
	private BigDecimal getInvoiceInBalance(ReportSellerInvoiceInQuery reportSellerInvoiceInQuery) {
		PoolIn poolIn = poolInDao.selectSellerId(reportSellerInvoiceInQuery.getSellerId(), null);
		BigDecimal unPollInAmount = BigDecimal.ZERO;
		if(null != poolIn) {
			unPollInAmount = poolIn.getTotalAmount().subtract(poolIn.getTotalReceivedAmount());
		}
		// 订单出纳确认金额合计
		// 订单主表 pay_status字段 为 PAYED 已确认付款  status为4、5、6
		reportSellerInvoiceInQuery.setStatusList(Arrays.asList(ConsignOrderStatus.RELATED.getCode(),
				ConsignOrderStatus.CLOSEREQUEST2.getCode(),
				ConsignOrderStatus.SECONDSETTLE.getCode()));
		
		reportSellerInvoiceInQuery.setPayStatus(ConsignOrderPayStatus.PAYED.getCode());
		
		BigDecimal sumOrderAmount = consignOrderItemsDao.querySumAmountBySellerId(reportSellerInvoiceInQuery);
		if(null == sumOrderAmount) {
			sumOrderAmount = BigDecimal.ZERO;
		}
		// 进项出纳未确认金额合计
		reportSellerInvoiceInQuery.setStatusList(Arrays.asList(InvoiceInStatus.AWAITS.getCode(),
				InvoiceInStatus.RECEIVED.getCode(),
				InvoiceInStatus.SENT.getCode()));
		BigDecimal sumInvoiceAmount = invoiceInDao.querySumAmountBySellerId(reportSellerInvoiceInQuery);
		if(null == sumInvoiceAmount) {
			sumInvoiceAmount = BigDecimal.ZERO;
		}
		// 未开票金额 + 订单财务确认金额  + 进项财务未确认金额
		return unPollInAmount.add(sumOrderAmount).add(sumInvoiceAmount);
	}

	@Override
	public void pushToReportInvoiceOut(ConsignOrder order, User operator, BigDecimal amount, ReportBuyerInvoiceOutType operateType, String number){
		try {
			
			Date date = new Date();
			
			//insert invoice out
			ReportBuyerInvoiceOut record = new ReportBuyerInvoiceOut();
			record.setCreated(date);
			record.setCreatedBy(operator.getName());
			record.setBuyerId(order.getAccountId());
			record.setBuyName(order.getAccountName());
			
			//订单关闭的，资金应该为负数
			if(ReportBuyerInvoiceOutType.CloseOrder.equals(operateType) && amount.doubleValue()>0)
				amount = amount.negate();

			//二结结算完成，得到的资金，针对买家：当实提数量大于订单数量的时候：为负数(表示需要从买家处多扣除金额)
			//当实提数量小于订单数量的时候，为正数(表示需要给买家返回金额)
			//但是存入报表的时候，多提是：正数，少提是：负数，因此需要取反
			if(ReportBuyerInvoiceOutType.SecondSettle.equals(operateType)){
				amount = amount.negate();
			}

			if(operateType.equals(ReportBuyerInvoiceOutType.Invoice)){
				record.setInvoiceNo(number);  //单据号
				record.setInvoiceOutAmount(amount);
			}else
				record.setOrderAmount(amount);

			//当发生折让，不需要订单号, 合同号，但需要对应的折让单号
			if(ReportBuyerInvoiceOutType.Discount.equals(operateType)
					|| ReportBuyerInvoiceOutType.DiscountRollback.equals(operateType)){
				record.setRemark(operateType.getDesc()+"(单号："+number+")");
				
			}else{
				record.setOrderCode(order.getCode());
				record.setOrderId(order.getId());
				record.setRemark(operateType.getDesc());
				record.setContractCode(order.getContractCode());
			}
			
			record.setLastUpdated(date);
			record.setLastUpdatedBy(operator.getName());
			record.setModificationNumber(0);
			record.setOperationType(operateType.getCode());
			insertInvoiceOut(record);
		} catch (Exception e) {
			logger.error("添加进report_buyer_invoice_out失败", e);
		}
	}

	/**
	 * 客户销项报表明细
	 * @author tuxianming
	 */
	@Override
	public List<ReportBuyerInvoiceOutDto> queryReportBuyerInvoiceOutByPage(ReportBuyerInvoiceOutQuery query) {
		List<ReportBuyerInvoiceOutDto> list = reportBuyerInvoiceOutDao.selectAllByParams(query);
		return list;
	}

	/**
	 * 客户销项报表明细总数
	 * @author tuxianming
	 */
	@Override
	public Integer queryReportBuyerInvoiceOutTotalByPage(ReportBuyerInvoiceOutQuery query) {
		return reportBuyerInvoiceOutDao.totalAllByParams(query);
	}

	/**
	 * 客户销项报表列表
	 * @author tuxianming
	 */
	@Override
	public List<ReportBuyerInvoiceOutDto> queryReportBuyerInvoiceOutDetailByPage(ReportBuyerInvoiceOutQuery query) {
		List<ReportBuyerInvoiceOutDto> list = reportBuyerInvoiceOutDao.selectByParams(query);
		return list;
	}

	/**
	 * 客户销项报表列表总数
	 * @author tuxianming
	 */
	@Override
	public Integer queryReportBuyerInvoiceOutDetailTotalByPage(ReportBuyerInvoiceOutQuery query) {
		return reportBuyerInvoiceOutDao.totalByParams(query);
	}

	@Override
	public ReportBuyerInvoiceOut getBuyerInvoceOut(Long buyerId) {
		return reportBuyerInvoiceOutDao.selectByBuyer(buyerId);
	}

	@Override
	public void pushInvoiceToReportInvoiceOut(InvoiceOutItemDetailDto dto, String systemId, String djh, BigDecimal totalAmount) {
		
		ConsignOrder order = new ConsignOrder();
		order.setAccountId(dto.getBuyerId());
		order.setAccountName(dto.getBuyerName());
		
		User user = new User();
		user.setName(systemId);
		
		this.pushToReportInvoiceOut(order, user, totalAmount, ReportBuyerInvoiceOutType.Invoice, djh);
		
	}

	/**
	 * 供应商进项报表 总量
	 * @author dq
	 */
	@Override
	public Integer queryReportSellerInvoiceInDataCount(ReportSellerInvoiceInDataQuery query) {
		return reportSellerInvoiceInDao.queryReportSellerInvoiceInDataCount(query);
	}

	/**
	 * 供应商进项报表 数据
	 * @author dq
	 */
	@Override
	public List<ReportSellerInvoiceInDto> queryReportSellerInvoiceInData(ReportSellerInvoiceInDataQuery query) {
		return reportSellerInvoiceInDao.queryReportSellerInvoiceInData(query);
	}

	/**
	 * 供应商进项详情报表 总量
	 * @author dq
	 */
	@Override
	public Integer queryReportSellerInvoiceInDetailDataCount(ReportSellerInvoiceInDataQuery query) {
		return reportSellerInvoiceInDao.queryReportSellerInvoiceInDetailDataCount(query);
	}

	/**
	 * 供应商进项详情报表 总量
	 * @author dq
	 */
	@Override
	public List<ReportSellerInvoiceIn> queryReportSellerInvoiceInDetailData(ReportSellerInvoiceInDataQuery query) {
		return reportSellerInvoiceInDao.queryReportSellerInvoiceInDetailData(query);
	}

	/**
	 * 供应商进项报表 - 条件范围 - 期初期末金额
	 * @author dq
	 */
	@Override
	public ReportSellerInvoiceInDto queryReportSellerInvoiceInRangeAmount(ReportSellerInvoiceInDataQuery query) {
		return reportSellerInvoiceInDao.queryReportSellerInvoiceInRangeAmount(query);
	}
	
	/**
	 * @author tuxianming
	 */
	@Override
	public Map<String, Map<String, List<IncomeSummaryDto>>> queryIncomeSummaryForBuyer(IncomeSummaryQuery query) {
		return buildIncomeSummaryList(query.getGroupType(), 
				consignOrderItemsDao.incomeSummaryForBuyer(query),
				query.getShowSubSummary());
	}
	
	/**
	 * @author tuxianming
	 */
	@Override
	public Map<String, Map<String, List<IncomeSummaryDto>>> queryIncomeSummaryForSeller(IncomeSummaryQuery query) {
		return buildIncomeSummaryList(query.getGroupType(), 
				consignOrderItemsDao.incomeSummaryForSeller(query),
				query.getShowSubSummary());
	}
	
	/**
	 * @param type  seller, buyer, category
	 * @param datas
	 * @return
	 * @author tuxianming
	 */
	private Map<String, Map<String, List<IncomeSummaryDto>>> buildIncomeSummaryList(String type, List<IncomeSummaryDto> datas, Boolean showSubSummary){
		//type == buyer: org, account, IncomeSummaryDto
		//type == category: org, category, IncomeSummaryDto
		showSubSummary = (showSubSummary!=null && showSubSummary==true);
		
		Map<String, Map<String, List<IncomeSummaryDto>>> sortMaps= new LinkedHashMap<>();
		
		if(type==null || datas==null)
			return sortMaps;
		
		for (IncomeSummaryDto dto : datas) {
			String orgName = dto.getName();
			String account = dto.getAccountName();
			String category = dto.getNsortName();
			Map<String, List<IncomeSummaryDto>> buyerSortMap = sortMaps.get(orgName);
			if(buyerSortMap==null){
				buyerSortMap = new LinkedHashMap<>();
				List<IncomeSummaryDto> items = new ArrayList<>();
				items.add(dto);
				if(type.equals("buyer")||type.equals("seller")){
					buyerSortMap.put(account, items);
				}else
					buyerSortMap.put(category, items);
				
				sortMaps.put(orgName, buyerSortMap);
			}else{
				String subKey = (type.equals("buyer")||type.equals("seller"))?account : category;
				
				List<IncomeSummaryDto> items = buyerSortMap.get(subKey);
				if(items==null){
					items = new ArrayList<>();
				}
				items.add(dto);
				//buyerSortMap.put(account, items);
				if(type.equals("buyer")||type.equals("seller")){
					buyerSortMap.put(account, items);
				}else
					buyerSortMap.put(category, items);
			}
		}
		
		//添加小计
		//key: category||buyer
		for (String key : sortMaps.keySet()) {
			Map<String,List<IncomeSummaryDto>> buyerSortMap = sortMaps.get(key);
			
			//用于合计
			BigDecimal amount = BigDecimal.ZERO;
			BigDecimal saleAmount = BigDecimal.ZERO;
			BigDecimal weight = BigDecimal.ZERO;
			BigDecimal incomeAmount = BigDecimal.ZERO;
			
			//subKey: category||buyer
			for (String subKey : buyerSortMap.keySet()) {
				List<IncomeSummaryDto> items = buyerSortMap.get(subKey);
				
				//计算合计
				for (IncomeSummaryDto item : items) {
					amount=addBigDecimal(amount,item.getAmount());
					saleAmount=addBigDecimal(saleAmount,item.getSaleAmount());
					weight=addBigDecimal(weight,item.getWeight());
					incomeAmount=addBigDecimal(incomeAmount,item.getIncomeAmount());
				}
				
				//添加小计到最后一行。
				if(showSubSummary && items.size()>1){
					IncomeSummaryDto item = new IncomeSummaryDto();
					IncomeSummaryDto dto = items.get(0);
					item.setName(dto.getName());
					
					if(type.equals("buyer")||type.equals("seller")){
						item.setNsortName("小计：");
						item.setAccountName(dto.getAccountName());
					}else{
						item.setAccountName("小计：");
						item.setNsortName(dto.getNsortName());
					}
					
					for (IncomeSummaryDto idto : items) {
						item.setAmount(addBigDecimal(item.getAmount(), idto.getAmount()));
						item.setSaleAmount(addBigDecimal(item.getSaleAmount(), idto.getSaleAmount()));
						item.setWeight(addBigDecimal(item.getWeight(), idto.getWeight()));
						item.setIncomeAmount(addBigDecimal(item.getIncomeAmount(), idto.getIncomeAmount()));
					}
					items.add(item);
				}
			}
			
			//添加合计
			IncomeSummaryDto totalSummary = new IncomeSummaryDto();
			totalSummary.setName(key);
			if(type.equals("buyer")||type.equals("seller")){
				totalSummary.setAccountName("合计：");
			}else{
				totalSummary.setNsortName("合计：");
			}
			totalSummary.setAmount(amount);
			totalSummary.setSaleAmount(saleAmount);
			totalSummary.setWeight(weight);
			totalSummary.setIncomeAmount(incomeAmount);
			
			List<IncomeSummaryDto> items = new ArrayList<>();
			items.add(totalSummary);
			buyerSortMap.put("合计", items);
		}
		
		return sortMaps;
	}
	
	/**
	 * @author tuxianming
	 * @param b1
	 * @param b2
	 * @return
	 */
	private BigDecimal addBigDecimal(BigDecimal b1, BigDecimal b2){
		if(b1==null && b2==null)
			return BigDecimal.ZERO;
		if(b1==null && b2!=null)
			return b2;
		if(b2==null && b1!=null)
			return b1;
		return b1.add(b2);
	}

	/**
	 * @author tuxianming
	 */
	@Override
	public IncomeSummaryDto totalIncomeSummaryForBuyer(IncomeSummaryQuery query) {
		return consignOrderItemsDao.totalIncomeSummaryForBuyer(query);
	}

	/**
	 * @author tuxianming
	 */
	@Override
	public IncomeSummaryDto totalIncomeSummaryForSeller(IncomeSummaryQuery query) {
		return consignOrderItemsDao.totalIncomeSummaryForSeller(query);
	}

	@Override
	public List<ReportInvoiceInAndOutDto> queryInvoiceIn(ReportInvoiceInAndOutQuery query) {
		return consignOrderItemsDao.queryInvoiceIn(query);
	}

	@Override
	public List<ReportInvoiceInAndOutDto> queryInvoiceOut(ReportInvoiceInAndOutQuery query) {
		return consignOrderItemsDao.queryInvoiceOut(query);
	}

	@Override
	public int totalInvoiceIn(ReportInvoiceInAndOutQuery query) {
		return consignOrderItemsDao.totalInvoiceIn(query);
	}

	@Override
	public int totalInvoiceOut(ReportInvoiceInAndOutQuery query) {
		return consignOrderItemsDao.totalInvoiceOut(query);
	}
	
}
