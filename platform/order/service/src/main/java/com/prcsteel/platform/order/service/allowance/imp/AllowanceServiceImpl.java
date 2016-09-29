package com.prcsteel.platform.order.service.allowance.imp;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.common.utils.CbmsNumberUtil;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.dto.AllowanceDto;
import com.prcsteel.platform.order.model.dto.AllowanceForUpdateDto;
import com.prcsteel.platform.order.model.dto.AllowanceOrderItemsDto;
import com.prcsteel.platform.order.model.dto.CustAccountDto;
import com.prcsteel.platform.order.model.dto.OrderDetailModifier;
import com.prcsteel.platform.order.model.dto.PoolInDetailModifier;
import com.prcsteel.platform.order.model.dto.PoolInModifier;
import com.prcsteel.platform.order.model.dto.PoolOutDetailDto;
import com.prcsteel.platform.order.model.dto.PoolOutDetailModifier;
import com.prcsteel.platform.order.model.dto.PoolOutModifier;
import com.prcsteel.platform.order.model.enums.AllowanceManner;
import com.prcsteel.platform.order.model.enums.AllowanceStatus;
import com.prcsteel.platform.order.model.enums.AllowanceType;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.PayType;
import com.prcsteel.platform.order.model.enums.ReportBuyerInvoiceOutType;
import com.prcsteel.platform.order.model.enums.ReportSellerInvoiceInOperationType;
import com.prcsteel.platform.order.model.model.Allowance;
import com.prcsteel.platform.order.model.model.AllowanceItemAmountHistory;
import com.prcsteel.platform.order.model.model.AllowanceOrderDetailItem;
import com.prcsteel.platform.order.model.model.AllowanceOrderItem;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.PoolIn;
import com.prcsteel.platform.order.model.model.PoolInDetail;
import com.prcsteel.platform.order.model.model.PoolOut;
import com.prcsteel.platform.order.model.model.PoolOutDetail;
import com.prcsteel.platform.order.model.query.AllowanceDetailItemQuery;
import com.prcsteel.platform.order.model.query.AllowanceListQuery;
import com.prcsteel.platform.order.model.query.AllowanceOrderQuery;
import com.prcsteel.platform.order.model.query.AllowanceOrderSave;
import com.prcsteel.platform.order.model.query.AllowanceUpdate;
import com.prcsteel.platform.order.persist.dao.AllowanceDao;
import com.prcsteel.platform.order.persist.dao.AllowanceItemAmountHistoryDao;
import com.prcsteel.platform.order.persist.dao.AllowanceOrderDetailItemDao;
import com.prcsteel.platform.order.persist.dao.AllowanceOrderItemDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsDao;
import com.prcsteel.platform.order.persist.dao.InvoiceInDetailOrderitemDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutItemDetailDao;
import com.prcsteel.platform.order.persist.dao.PoolInDao;
import com.prcsteel.platform.order.persist.dao.PoolInDetailDao;
import com.prcsteel.platform.order.persist.dao.PoolOutDao;
import com.prcsteel.platform.order.persist.dao.PoolOutDetailDao;
import com.prcsteel.platform.order.service.allowance.AllowanceOrderDetailItemService;
import com.prcsteel.platform.order.service.allowance.AllowanceService;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import com.prcsteel.platform.order.service.report.ReportFinanceService;

/**
 * 折让单的相关业务
 * @author dq
 */
@Service("allowanceService")
public class AllowanceServiceImpl implements AllowanceService {

	private static final Logger logger = Logger.getLogger(AllowanceServiceImpl.class);
	
	public static final String ATTACHMENTSAVEPATH = "upload" + File.separator + "img" + File.separator + "allowance" + File.separator;

	private static final String SYSTEM = "系统";
	@Resource
    FileService fileService;
	
	@Resource
	AccountDao accountDao;

	@Resource
	ConsignOrderDao consignOrderDao;

	@Resource
	ConsignOrderItemsDao consignOrderItemsDao;

	@Resource
	InvoiceInDetailOrderitemDao invoiceInDetailOrderitemDao;

	@Resource
	InvoiceOutItemDetailDao invoiceOutItemDetailDao;
	
	@Resource
	AllowanceItemAmountHistoryDao allowanceItemAmountHistoryDao;

	@Resource
	AllowanceDao allowanceDao;

	@Resource
	AllowanceOrderItemDao allowanceOrderItemDao;

	@Resource
	AllowanceOrderDetailItemDao allowanceOrderDetailItemDao;
	
	@Resource
	UserDao userDao;
	
	@Resource
	OrganizationDao organizationDao;
	
	@Resource
	PoolOutDao poolOutDao;
	
	@Resource
	PoolInDao poolInDao;
	
	@Resource
	PoolInDetailDao poolInDetailDao;
	
	@Resource
	PoolOutDetailDao poolOutDetailDao;
	
	@Resource
    AllowanceOrderDetailItemService allowanceOrderDetailItemService;

	@Resource
	OrderStatusService orderStatusService;
	
	@Resource
	ReportFinanceService reportFinanceService;
	
	@Resource
	AccountFundService accountFundService;
	
	/**
	 * 查询可以开折让单的订单资源数据
	 *
	 * @return
	 * @author dq
	 */
	@Override
	public List<AllowanceOrderItemsDto> queryOrders(AllowanceOrderQuery allowanceOrderQuery) {

		List<AllowanceOrderItemsDto> list = consignOrderItemsDao.queryOrderItems(allowanceOrderQuery);
		if(null == list || list.size() < 1) {
			return new ArrayList<AllowanceOrderItemsDto>();
		}
		// 排除卖家或买家已经开过折让单的数据
		excludeByAllowance(allowanceOrderQuery,list);
		if(null == list || list.size() < 1) {
			return new ArrayList<AllowanceOrderItemsDto>();
		}
		// 排除进项或销项票数据 
		excludeByInvoice(allowanceOrderQuery,list);
		if(null == list || list.size() < 1) {
			return new ArrayList<AllowanceOrderItemsDto>();
		}
		if(allowanceOrderQuery.getOrderIds() != null && allowanceOrderQuery.getOrderIds().size() > 0) {
			// 统计对应订单可开折让单的实体重量
			return setTotalActual(list);
		}
		// 根据订单ID去重复、 并合计实提金额重量
		return dereplication(list);
	}

	/*
	 * 统计对应订单可开折让单的实体重量金额
	 * 
	 * @return
	 * @author dq
	 */
	private List<AllowanceOrderItemsDto> setTotalActual(List<AllowanceOrderItemsDto> list) {
		List<Long> orderIds = list.stream().map(
				a -> a.getOrderId()).distinct().collect(Collectors.toList());
		orderIds.forEach(orderId -> {
			List<AllowanceOrderItemsDto> tempList = list.stream().filter(
					a -> a.getOrderId().equals(orderId)).collect(Collectors.toList());
			if (null == tempList || tempList.size() < 1) {
				return;
			}
			BigDecimal totalActualWeight = CbmsNumberUtil.buildWeight(
					tempList.stream().mapToDouble(b -> b.getActualWeight().doubleValue()).sum()
			);
			BigDecimal totalActualAmount = CbmsNumberUtil.buildWeight(
					tempList.stream().mapToDouble(b -> b.getActualAmount().doubleValue()).sum()
			);
			tempList.forEach(temp -> {
				temp.setTotalActualWeight(totalActualWeight);
				temp.setTotalActualAmount(totalActualAmount);
			});
		});
		return list;
	}

	/*
	 * 排除已经开过折让单的数据
	 *  allowanceOrderQuery.getAllowanceType() 可以判断是买家还是卖家，
	 *  	如果是卖家，则排除已经过卖家折让单的数据，如果是买家，刚排除已经开过买家 折让 单的数据 
	 * @return
	 * @author dq
	 */
	private void excludeByAllowance(AllowanceOrderQuery allowanceOrderQuery,List<AllowanceOrderItemsDto> list) {
		allowanceOrderQuery.setOrderDetailIds(list.stream().map(
				a -> a.getOrderDetailId()).collect(Collectors.toList()));
		if(AllowanceType.Seller.getKey().equals(allowanceOrderQuery.getAllowanceType())) {
			// 排除已经开过卖家折让单的数据
			List<Long> sellerOrderItemIds = allowanceOrderDetailItemDao.queryOrderDetailIds(allowanceOrderQuery);  //这里主要用到：allowanceOrderQuery.orderDetailIds这个字段作为条件
			if(null != sellerOrderItemIds && sellerOrderItemIds.size() > 0) {
				list.removeIf(a -> sellerOrderItemIds.contains(a.getOrderDetailId()));
			}

		}else if(AllowanceType.Buyer.getKey().equals(allowanceOrderQuery.getAllowanceType())) {
			// 排除已经开过买家折让单的数据
			List<Long> buyerOrderItemIds = allowanceOrderDetailItemDao.queryOrderDetailIds(allowanceOrderQuery);  //这里主要用到：allowanceOrderQuery.orderDetailIds这个字段作为条件
			if(null != buyerOrderItemIds && buyerOrderItemIds.size() > 0) {
				list.removeIf(a -> buyerOrderItemIds.contains(a.getOrderDetailId()));
			}
		}
	}

	/*
	 * 排除已经开过票的数据，如是卖家就是排除已经开过的销项票数据， 如果是卖家就是排除已经开过的进项票数据
	 *
	 * @return
	 * @author dq
	 */
	private void excludeByInvoice(AllowanceOrderQuery allowanceOrderQuery,List<AllowanceOrderItemsDto> list) {
		allowanceOrderQuery.setOrderDetailIds(list.stream().map(
				a -> a.getOrderDetailId()).collect(Collectors.toList()));
		if(AllowanceType.Seller.getKey().equals(allowanceOrderQuery.getAllowanceType())) {
			// 排除已经开过进项票的数据
			List<Long> invOrderItemIds = invoiceInDetailOrderitemDao.queryOrderItemIds(allowanceOrderQuery);
			if(null != invOrderItemIds && invOrderItemIds.size() > 0) {
				list.removeIf(a -> invOrderItemIds.contains(a.getOrderDetailId()));
			}

		}else if(AllowanceType.Buyer.getKey().equals(allowanceOrderQuery.getAllowanceType())) {
			// 排除已经开过销项票的数据
			List<Long> outOrderItemIds = invoiceOutItemDetailDao.queryOrderItemIds(allowanceOrderQuery);
			if(null != outOrderItemIds && outOrderItemIds.size() > 0) {
				list.removeIf(a -> outOrderItemIds.contains(a.getOrderDetailId()));
			}
		}
	}

	/*
	 * 根据订单ID去重复、 并合计实提金额重量
	 *
	 * @return
	 * @author dq
	 */
	private List<AllowanceOrderItemsDto> dereplication(List<AllowanceOrderItemsDto> list) {
		List<AllowanceOrderItemsDto> resultList = new ArrayList<AllowanceOrderItemsDto>();
		
		//根据oderId去掉重复的数据
		List<Long> orderIds = list.stream().map(
				a -> a.getOrderId()).distinct().collect(Collectors.toList());
		
		//计算
		orderIds.forEach(orderId -> {
			List<AllowanceOrderItemsDto> tempList = list.stream().filter(
					a -> a.getOrderId().equals(orderId)).collect(Collectors.toList());
			if (null == tempList || tempList.size() < 1) {
				return;
			}
			BigDecimal totalActualWeight = CbmsNumberUtil.buildWeight(CbmsNumberUtil.formatWeight(
					tempList.stream().mapToDouble(b -> b.getActualWeight().doubleValue()).sum()
			));
			BigDecimal totalActualAmount = CbmsNumberUtil.buildMoney(CbmsNumberUtil.formatMoney(
					tempList.stream().mapToDouble(b -> b.getActualAmount().doubleValue()).sum()
			));
			AllowanceOrderItemsDto allowanceOrderItemsDto = tempList.stream().findFirst().get();
			allowanceOrderItemsDto.setTotalActualWeight(totalActualWeight);
			allowanceOrderItemsDto.setTotalActualAmount(totalActualAmount);
			resultList.add(allowanceOrderItemsDto);

		});
		return resultList;
	}

	@Override
	public Allowance selectByPrimaryKey(Long id) {
		return allowanceDao.selectByPrimaryKey(id);
	}

	/**
	 * 折让单保存
	 *
	 * @return
	 * @author dq
	 */
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void save(MultipartFile image, AllowanceOrderSave allowanceOrderSave,List<AllowanceOrderItemsDto> orderList) {
		//如果是卖家，则需要上传图片
		if(AllowanceType.Seller.getKey().equals(allowanceOrderSave.getAllowanceType())) {
			if (image == null) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
						"没有上传折让单号！");
			}
			saveImg(image, allowanceOrderSave);
		}
		// 保存折让单主表数据， 主表id
		Long id = saveCommon(allowanceOrderSave, orderList);
		
		// 如果为卖家这让单，则自动生成买家这让单 (20160325修改 buyerIds替换成buyerDeptIds)
		if(AllowanceType.Seller.getKey().equals(allowanceOrderSave.getAllowanceType())) {
			//List<Long> buyerIds = null;
			List<Long> buyerDeptIds = null;//买家部门id集合
			allowanceOrderSave.setAllowanceType(AllowanceType.Buyer.getKey());
			allowanceOrderSave.setImgUrl(null);
			// 如果折让方式为重量和(金额加重量)则从orderList获取ID集合 否则从allowanceOrderSave获取买家ID集合  --改为获取买家部门id集合
			if(AllowanceManner.Weight.getKey().equals(allowanceOrderSave.getAllowanceManner())
					|| AllowanceManner.All.getKey().equals(allowanceOrderSave.getAllowanceManner())) {
				//buyerIds = orderList.stream().map(
				//		a -> a.getBuyerId()).distinct().collect(Collectors.toList());

				//获取买家部门id集合（去重）
				buyerDeptIds = orderList.stream().map(a->a.getBuyerDepartmentId()).distinct().collect(Collectors.toList());
			}else {
				/*List<String> tempbuyerIds = allowanceOrderSave.getBuyerIds();
				if(null == tempbuyerIds || tempbuyerIds.size() == 0) {
					return;
				}
				buyerIds = tempbuyerIds.stream().map(
						a -> Long.parseLong(a)).distinct().collect(Collectors.toList());*/

				//买家部门id 替换买家
				List<String> tempbuyerDeptIds = allowanceOrderSave.getBuyerDeptIds();
				if(null == tempbuyerDeptIds || tempbuyerDeptIds.size() == 0) {
					return;
				}
				buyerDeptIds = tempbuyerDeptIds.stream().map(
						a -> Long.parseLong(a)).distinct().collect(Collectors.toList());
			}
			// 获取相关买家折让单数据
			List<AllowanceOrderItemsDto> buyerList = getBuyerAllowance(orderList, buyerDeptIds);//原买家id 改成买家部门id
			// 根据买家部门数量 循环生成买家折让单（原使用买家id 改成使用买家部门id）
			buyerDeptIds.forEach(buyerDeptId -> {
				List<AllowanceOrderItemsDto> tempList = buyerList.stream().filter(
						a -> a.getBuyerDepartmentId().equals(buyerDeptId)).collect(Collectors.toList());
				allowanceOrderSave.setAllowanceId(id);
				// 保存折让单主表数据
				saveCommon(allowanceOrderSave,tempList);
			});
		}
	}

	/*
	 * 获取相关买家折让单数据
	 *
	 * @return
	 * @author dq
	 */
	private List<AllowanceOrderItemsDto> getBuyerAllowance(List<AllowanceOrderItemsDto> orderList, List<Long> buyerDeptIds) {
		// 找出orderList中包含buyerDeptIds中的数据
		List<AllowanceOrderItemsDto> sellerList = orderList.stream().filter(
				a -> buyerDeptIds.contains(a.getBuyerDepartmentId())).collect(Collectors.toList());
		// 获取卖家折让单集合的订单详情ID集合
		List<Long> orderDetailIds = sellerList.stream().map(
				a -> a.getOrderDetailId()).collect(Collectors.toList());
		AllowanceOrderQuery allowanceOrderQuery = new AllowanceOrderQuery();
		allowanceOrderQuery.setAllowanceType(AllowanceType.Buyer.getKey());
		allowanceOrderQuery.setOrderDetailIds(orderDetailIds);
		// 查询买家折让单集合
		List<AllowanceOrderItemsDto> buyerList = consignOrderItemsDao.queryOrderItems(allowanceOrderQuery);
		if(buyerList.size() != orderDetailIds.size()) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"已生成相关买家折让单！");
		}
		int tempSize = 0;
		for(AllowanceOrderItemsDto buyerOrder : buyerList) {
			// 根据订单详情ID过滤卖家折让单集合，并获取过滤之后的集合第一条记录
			AllowanceOrderItemsDto sellerOrder = sellerList.stream().filter(
					a -> a.getOrderDetailId().equals(buyerOrder.getOrderDetailId())).findFirst().get();
			// 如果卖家折让金额小于等于买家折让金额，则判断条件记录+1
			if(sellerOrder.getAllowanceAmount().abs().doubleValue() <= buyerOrder.getActualAmount().abs().doubleValue()) {
				tempSize += 1;
			}
			buyerOrder.setAllowanceAmount(sellerOrder.getAllowanceAmount());
			buyerOrder.setAllowanceWeight(sellerOrder.getAllowanceWeight());
		}
		// 根据判断条件记录数量是否等于orderDetailIds集合数量
		if(tempSize != orderDetailIds.size()) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"折让的金额大于买家的实提金额，不能提交！");
		}
		// 根据订单ID去重复、 并合计实提金额重量
		dereplication(buyerList);
		return buyerList;
	}

	/*
	 * 折让单图片上传保存
	 *
	 * @return
	 * @author dq
	 */
	private void saveImg(MultipartFile image, AllowanceOrderSave allowanceOrderSave) {
		String urlPath = ATTACHMENTSAVEPATH + Tools.dateToStr(new Date(),"yyyy-MM") + File.separator;
		String basePath = urlPath;
		String temppath = new Date().getTime() + "."
				+ FileUtil.getFileSuffix(image.getOriginalFilename());
		String savePath = basePath + temppath;
		String url = "";
		try {
			url = fileService.saveFile(image.getInputStream(), savePath);
			if (StringUtils.isEmpty(url)) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
						"文件保存失败");
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"文件保存失败");
		}

		String imgUrl = allowanceOrderSave.getImgUrl();
		if(imgUrl != null) {
			try {
				fileService.removeFile(imgUrl);
			} catch (Exception e) {
				logger.error("图片删除失败！imgUrl:"+imgUrl, e);
			}
		}
		allowanceOrderSave.setImgUrl(url);
	}

	/*
	 * 编辑后 保存或修改折让单数据， 删除reb_allowance_order_detail_item, 
	 * 		reb_allowance_order_item里面的相应在记录，并重新插入折让 数据 
	 * @return
	 * @author dq
	 */
	private void saveSellerCommon(MultipartFile image, AllowanceOrderSave allowanceOrderSave,List<AllowanceOrderItemsDto> orderList){
		String imgUrl = allowanceOrderSave.getImgUrl();
		
		//如果是卖家，则需要上传图片
		if(AllowanceType.Seller.getKey().equals(allowanceOrderSave.getAllowanceType()) && null != imgUrl) {
			if (image == null) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
						"没有上传折让单号！");
			}
			saveImg(image, allowanceOrderSave);
		}
		
		Long id = saveCommon(allowanceOrderSave, orderList);
		
		//如是卖家，则生成买家折让
		if(AllowanceType.Seller.getKey().equals(allowanceOrderSave.getAllowanceType())) {
			
			//如果是重量或者是重量和金额，则生成买家折让单
			if(AllowanceManner.Weight.getKey().equals(allowanceOrderSave.getAllowanceManner())
					|| AllowanceManner.All.getKey().equals(allowanceOrderSave.getAllowanceManner())) {
				
				//根据折让id查询去查询折让详情（也就是买家折让单）
				AllowanceDetailItemQuery detailItemQuery = new AllowanceDetailItemQuery();
				detailItemQuery.setAllowanceId(id);
				
				//得到当前折让单的下面的所有买家 
				List<AllowanceOrderItemsDto> buyerOrderList = allowanceOrderDetailItemService.queryDetails(detailItemQuery);
				
				//将类型设置成买家，用于生成折让单详情(也就是买家折让单)
				allowanceOrderSave.setAllowanceType(AllowanceType.Buyer.getKey());
				allowanceOrderSave.setAllowanceId(id);
				allowanceOrderSave.setImgUrl(null);	//买家折让单是没有图片的
				
				//得到所有买家
				/*List<Long> buyerIds = buyerOrderList.stream().map(
						a -> a.getBuyerId()).distinct().collect(Collectors.toList());*/

				//得到所有买家部门(原得到所有买家，根据买家数量生成买家折让单 现改成得到所有买家部门)
				List<Long> buyerDeptIds = buyerOrderList.stream().map(
						a -> a.getBuyerDepartmentId()).distinct().collect(Collectors.toList());

				buyerDeptIds.forEach(buyerDeptId -> {
					List<AllowanceOrderItemsDto> tempList = buyerOrderList.stream().filter(
							a -> a.getBuyerDepartmentId().equals(buyerDeptId)).collect(Collectors.toList());
					int tempListSize = 0;
					for(int i=0 ; i<tempList.size() ; i++) {
						AllowanceOrderItemsDto temp = tempList.get(i);
						AllowanceOrderItemsDto order = orderList.stream().filter(
								a -> a.getOrderDetailId().equals(temp.getOrderDetailId())).findFirst().get();
						if(temp.getAllowanceWeight().doubleValue() != order.getAllowanceWeight().doubleValue()) {
							temp.setAllowanceWeight(order.getAllowanceWeight());
						}else {
							tempListSize += 1;
						}
					
					}
					Long buyerAllowanceId = tempList.stream().findFirst().get().getId();
					if(tempListSize != tempList.size()) {
						//逻辑删除买家相关联的订单及详情
						unbindOrderAndDetails(allowanceOrderSave.getUser(), Arrays.asList(tempList.stream().findFirst().get().getId()));
						
						allowanceOrderSave.setId(buyerAllowanceId);
						saveCommon(allowanceOrderSave,tempList);
					}else {
						Allowance allowance = allowanceDao.selectByPrimaryKey(buyerAllowanceId);
						allowance.setStatus(allowanceOrderSave.getStatus());
						allowance.setModificationNumber(allowance.getModificationNumber()+1);
						allowance.setLastUpdated(allowanceOrderSave.getDate());
						allowance.setLastUpdatedBy(allowanceOrderSave.getUser().getName());
						allowanceDao.updateByPrimaryKeySelective(allowance);
					}
				});
			}
		}
	}

	/*
	 * 保存折让单主表数据
	 *
	 * @return
	 * @author dq
	 */
	private Long saveCommon(AllowanceOrderSave allowanceOrderSave,List<AllowanceOrderItemsDto> orderList) {
		// 查询是否关联过进项票
		AllowanceOrderQuery allowanceOrderQuery = new AllowanceOrderQuery();
		allowanceOrderQuery.setOrderDetailIds(orderList.stream().map(
				a -> a.getOrderDetailId()).collect(Collectors.toList()));
		List<Long> invOrderItemIds = invoiceInDetailOrderitemDao.queryOrderItemIds(allowanceOrderQuery);
		if(null != invOrderItemIds && invOrderItemIds.size() > 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"该折让单关联的交易单的进项票已关联，不能提交审核");
		}
		AllowanceOrderItemsDto odt = orderList.stream().findFirst().get();
		List<Long> orderIds = orderList.stream().map(a -> a.getOrderId()).distinct().collect(Collectors.toList());
		Allowance allowance = new Allowance();
		// 如果是卖家折让单 将客户设置为卖家，如果是买家折让单 将客户设置为买家
		if(AllowanceType.Seller.getKey().equals(allowanceOrderSave.getAllowanceType())) {
			allowance.setAccountId(odt.getSellerId());
			allowance.setAccountName(odt.getSellerName());
			allowance.setDepartmentId(odt.getSellerDepartmentId());
			allowance.setDepartmentName(odt.getSellerDepartmentName());
		}else if(AllowanceType.Buyer.getKey().equals(allowanceOrderSave.getAllowanceType())) {
			//如买家已开销项票，不能提交审核
			List<Long> outOrderItemIds = invoiceOutItemDetailDao.queryOrderItemIds(allowanceOrderQuery);
			if(null != outOrderItemIds && outOrderItemIds.size() > 0) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
						"该折让单关联的交易单的销项票已申请待开或已开，不能提交审核！");
			}
			allowance.setAccountId(odt.getBuyerId());
			allowance.setAccountName(odt.getBuyerName());
			allowance.setDepartmentId(odt.getBuyerDepartmentId());
			allowance.setDepartmentName(odt.getBuyerDepartmentName());
		}
		allowance.setAllowanceType(allowanceOrderSave.getAllowanceType());
		allowance.setStatus(allowanceOrderSave.getStatus());
		allowance.setImgUrl(allowanceOrderSave.getImgUrl());
		allowance.setAllowanceManner(allowanceOrderSave.getAllowanceManner());
		allowance.setAllowanceId(allowanceOrderSave.getAllowanceId());
		// 遍历orderIds 获得的总数量、总重量、总金额、实提总重量、实提总金额
		orderIds.forEach(orderId -> {
			AllowanceOrderItemsDto tempOrder = orderList.stream().filter(
					a -> a.getOrderId().equals(orderId)).findFirst().get();

			allowance.setTotalQuantity(allowance.getTotalQuantity() + tempOrder.getTotalQuantity());
			allowance.setTotalWeight(allowance.getTotalWeight().add(tempOrder.getTotalWeight()));
			allowance.setTotalAmount(allowance.getTotalAmount().add(tempOrder.getTotalAmount()));
			allowance.setActualTotalWeight(allowance.getActualTotalWeight().add(tempOrder.getTotalActualWeight()));
			allowance.setActualTotalAmount(allowance.getActualTotalAmount().add(tempOrder.getTotalActualAmount()));

		});
		allowance.setAllowanceTotalWeight(new BigDecimal(orderList.stream().mapToDouble(
				a -> a.getAllowanceWeight().doubleValue()).sum()));
		allowance.setAllowanceTotalAmount(new BigDecimal(orderList.stream().mapToDouble(
				a -> a.getAllowanceAmount().doubleValue()).sum()));
		allowance.setNote(allowanceOrderSave.getNote());
		// 保留修改记录
		allowance.setCreated(allowanceOrderSave.getDate());
		allowance.setCreatedBy(allowanceOrderSave.getUser().getName());
		allowance.setLastUpdated(allowanceOrderSave.getDate());
		allowance.setLastUpdatedBy(allowanceOrderSave.getUser().getName());
		// 设置修改次数
		allowance.setModificationNumber(0);

		Long id = allowanceOrderSave.getId();
		Allowance tempAllowance = allowanceDao.selectByPrimaryKey(id);
		// 根据id查询是否已经存在折让单，如果存在则修改，否则新增
		if(tempAllowance != null) {
			allowance.setId(tempAllowance.getId());
			allowance.setTotalQuantity(tempAllowance.getTotalQuantity());
			allowance.setModificationNumber(tempAllowance.getModificationNumber()+1);
			allowanceDao.updateByPrimaryKeySelective(allowance);
		}else {
			setAllowanceCode(allowanceOrderSave, allowance);
			allowanceDao.insertSelective(allowance);
		}
		// 保存折让单从表数据
		saveCommonItem(allowanceOrderSave, orderList, orderIds, allowance);

		return allowance.getId();
	}
	
	/*
	 * 生成折让单号
	 *
	 * @return
	 * @author dq
	 */
	private void setAllowanceCode(AllowanceOrderSave allowanceOrderSave, Allowance allowance) {
		// 按规则生成折让单号
//			Account account = null;
//			
//			if(AllowanceType.Seller.getKey().equals(allowanceOrderSave.getAllowanceType())) {
//				account = accountDao.selectByPrimaryKey(odt.getSellerId());
//			}else if(AllowanceType.Buyer.getKey().equals(allowanceOrderSave.getAllowanceType())) {
//				account = accountDao.selectByPrimaryKey(odt.getBuyerId());
//			}
//			User user = userDao.queryById(account.getManagerId());
		// 获取当前用户的服务中心
		User user = allowanceOrderSave.getUser();
		Organization organization = organizationDao.queryById(user.getOrgId());
		
		String allowanceCode = null;
		if(AllowanceType.Seller.getKey().equals(allowanceOrderSave.getAllowanceType())) {
			allowanceCode = "ZRS"+organization.getSeqCode()+
					Tools.dateToStr(new Date(),"yyyyMM");
		}else if(AllowanceType.Buyer.getKey().equals(allowanceOrderSave.getAllowanceType())) {
			allowanceCode = "ZRB"+organization.getSeqCode()+
					Tools.dateToStr(new Date(),"yyyyMM");
		}
		// 设置服务中心ID 和 名称
		allowance.setOrgId(organization.getId());
		allowance.setOrgName(organization.getName());
		String str = allowanceDao.queryMaxAllowanceCode(allowanceCode);
		if(null == str) {
			allowanceCode = allowanceCode + "001";
		}else {
			int code = Integer.parseInt(str.substring(str.length()-3,str.length()))+1;
			if(code < 10) {
				allowanceCode = allowanceCode + "00" + code;
			}else if(code < 100) {
				allowanceCode = allowanceCode + "0" + code;
			}else {
				allowanceCode = allowanceCode + code;
			}
		}

		allowance.setAllowanceCode(allowanceCode);
	}

	/*
	 * 保存折让单从表数据
	 *
	 * @return
	 * @author dq
	 */
	private void saveCommonItem(AllowanceOrderSave allowanceOrderSave, List<AllowanceOrderItemsDto> orderList,
			List<Long> orderIds, Allowance allowance) {
		Long allowanceId = allowance.getId();
		orderIds.forEach(orderId -> {
			// 根据orderId获取orderList的数据
			List<AllowanceOrderItemsDto> tempOrderList = orderList.stream().filter(
					a -> a.getOrderId().equals(orderId)).collect(Collectors.toList());
			// 获取集合第一条数据
			AllowanceOrderItemsDto tempOrder = tempOrderList.stream().findFirst().get();

			AllowanceOrderItem allowanceOrderItem = new AllowanceOrderItem();
			allowanceOrderItem.setAllowanceId(allowanceId);

			allowanceOrderItem.setOrderId(orderId);
			allowanceOrderItem.setOrderCode(tempOrder.getOrderCode());
			allowanceOrderItem.setOrderTime(tempOrder.getOrderTime());
			allowanceOrderItem.setWeight(tempOrder.getTotalWeight());
			allowanceOrderItem.setAmount(tempOrder.getTotalAmount());
			allowanceOrderItem.setActualWeight(tempOrder.getTotalActualWeight());
			allowanceOrderItem.setActualAmount(tempOrder.getTotalActualAmount());
			allowanceOrderItem.setAllowanceWeight(tempOrder.getAllowanceWeight());
			allowanceOrderItem.setAllowanceAmount(tempOrder.getAllowanceAmount());

			// 保留修改记录
			allowanceOrderItem.setCreated(allowanceOrderSave.getDate());
			allowanceOrderItem.setCreatedBy(allowanceOrderSave.getUser().getName());
			allowanceOrderItem.setLastUpdated(allowanceOrderSave.getDate());
			allowanceOrderItem.setLastUpdatedBy(allowanceOrderSave.getUser().getName());
			// 设置修改次数
			allowanceOrderItem.setModificationNumber(0);

			allowanceOrderItemDao.insertSelective(allowanceOrderItem);
			// 保存折让单详情关联表数据
			saveCommonDetailItem(allowanceOrderSave, allowanceOrderItem, tempOrderList);

		});
	}

	/*
	 * 保存折让单详情关联表数据
	 *
	 * @return
	 * @author dq
	 */
	private void saveCommonDetailItem(AllowanceOrderSave allowanceOrderSave, AllowanceOrderItem allowanceOrderItem,
			List<AllowanceOrderItemsDto> tempOrderList) {
		List<AllowanceOrderDetailItem> details = new ArrayList<AllowanceOrderDetailItem>();
		Long allowanceOrderItemsId = allowanceOrderItem.getId();

		for(AllowanceOrderItemsDto temp : tempOrderList) {
			AllowanceOrderDetailItem allowanceOrderDetailItem = new AllowanceOrderDetailItem();
			allowanceOrderDetailItem.setAllowanceOrderId(allowanceOrderItemsId);

			allowanceOrderDetailItem.setBuyerId(temp.getBuyerId());
			allowanceOrderDetailItem.setBuyerName(temp.getBuyerName());

			//买家部门
			allowanceOrderDetailItem.setBuyerDepartmentId(temp.getBuyerDepartmentId());
			allowanceOrderDetailItem.setBuyerDepartmentName(temp.getBuyerDepartmentName());

			allowanceOrderDetailItem.setOrderDetailId(temp.getOrderDetailId());
			allowanceOrderDetailItem.setNsortName(temp.getNsortName());
			allowanceOrderDetailItem.setMaterial(temp.getMaterial());
			allowanceOrderDetailItem.setSpec(temp.getSpec());

			allowanceOrderDetailItem.setActualWeight(temp.getActualWeight());
			allowanceOrderDetailItem.setActualAmount(temp.getActualAmount());
			allowanceOrderDetailItem.setAllowanceWeight(temp.getAllowanceWeight());
			allowanceOrderDetailItem.setAllowanceAmount(temp.getAllowanceAmount());

			// 保留修改记录
			allowanceOrderDetailItem.setCreated(allowanceOrderSave.getDate());
			allowanceOrderDetailItem.setCreatedBy(allowanceOrderSave.getUser().getName());
			allowanceOrderDetailItem.setLastUpdated(allowanceOrderSave.getDate());
			allowanceOrderDetailItem.setLastUpdatedBy(allowanceOrderSave.getUser().getName());
			// 设置修改次数
			allowanceOrderDetailItem.setModificationNumber(0);

			details.add(allowanceOrderDetailItem);
		}
		allowanceOrderDetailItemDao.batchInsert(details);
	}

	/**
	 * 审核折让单
	 *
	 * @return
	 * @author lx
	 */
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void checkAllowance(AllowanceUpdate allowanceUpdate) {
		Allowance allowance = allowanceDao.selectByPrimaryKey(allowanceUpdate.getAllowanceId());
		allowanceUpdate.setAuditDate(new Date());
		allowanceUpdate.setRemark(allowanceUpdate.getRemark());
		allowanceUpdate.setStatus(allowanceUpdate.getStatus());
		allowanceUpdate.setLastUpdated(new Date());
		allowanceUpdate.setLastUpdatedBy(allowanceUpdate.getUser().getName());
		if(AllowanceType.Buyer.getKey().equals(allowance.getAllowanceType())){
			if(!AllowanceManner.Amount.getKey().equals(allowance.getAllowanceManner())){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
						"该买家折让单与卖家折让单有关联，不能单独操作,请到卖家折让单里审核！");
			}
		}
		//不通过审核
		if(allowanceUpdate.getRemark() != null) {
			allowanceUpdate.setId(allowance.getId());
			updateRebateState (allowanceUpdate);
			//如存在关联的买家，将所有买家一起修改
			if (allowanceUpdate.getBuyerAllowanceIds() != null && allowanceUpdate.getBuyerAllowanceIds().length() > 0) {
				List<String> allowanceIds = Arrays.asList(allowanceUpdate.getBuyerAllowanceIds().split(","));
				allowanceIds.forEach(allowanceId -> {
					Allowance buyerAllowance = allowanceDao.selectByPrimaryKey(Long.parseLong(allowanceId));
					allowanceUpdate.setId(buyerAllowance.getId());
					updateRebateState (allowanceUpdate);
				});
			}
		}else {
			allowanceUpdate.setId(allowance.getId());
			checkCommonAllowance(allowanceUpdate, allowance);//修改折让单
			if (allowanceUpdate.getBuyerAllowanceIds() != null && allowanceUpdate.getBuyerAllowanceIds().length() > 0) {
				List<String> allowanceIds = Arrays.asList(allowanceUpdate.getBuyerAllowanceIds().split(","));
				allowanceUpdate.setAllowanceType(AllowanceType.Buyer.getKey());
				allowanceIds.forEach(allowanceId -> {
					Allowance buyerAllowance = allowanceDao.selectByPrimaryKey(Long.parseLong(allowanceId));
					allowanceUpdate.setId(buyerAllowance.getId());
					checkCommonAllowance(allowanceUpdate, buyerAllowance);
				});
			}
			if(AllowanceType.Seller.getKey().equals(allowance.getAllowanceType()) && allowance.getTotalAmount().doubleValue() > 0) {
				allowanceOperation(allowanceUpdate, allowance);
			}
		}
	}

	private void allowanceOperation(AllowanceUpdate allowanceUpdate, Allowance allowance) {
		if (AllowanceStatus.Approved.getKey().equals(allowanceUpdate.getStatus())) {
			// 供应商进项报表踩点 - 采购调价踩点
			reportFinanceService.allowanceOperation(ReportSellerInvoiceInOperationType.Allowance.getOperation(),allowance,allowanceUpdate.getUser());
		}else if (AllowanceStatus.ToSubmit.getKey().equals(allowanceUpdate.getStatus())) {
			// 供应商进项报表踩点 - 采购调价回滚踩点
			reportFinanceService.allowanceOperation(ReportSellerInvoiceInOperationType.UnAllowance.getOperation(),allowance,allowanceUpdate.getUser());
		}
	}
	
	/*
	 * 修改折让单状态
	 *
	 * @return
	 * @author lx
	 */
	private void updateRebateState (AllowanceUpdate allowanceUpdate) {
		int num  = allowanceDao.updateRebateState(allowanceUpdate);
		if (num != 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"该折让单状态已变更，无需再次操作！");
		}
	}
	
	/*
	 * 修改折让单状态 
	 *
	 * @return
	 * @author lx
	 */
	private void checkCommonAllowance(AllowanceUpdate allowanceUpdate, Allowance allowance) {
		AllowanceDetailItemQuery detailItemQuery = new AllowanceDetailItemQuery();
		AllowanceOrderQuery allowanceOrderQuery = new AllowanceOrderQuery();
		List<AllowanceOrderItemsDto> orderList = null;
		int tempSize;
		// 修改折让单状态
		allowanceUpdate.setState(allowanceUpdate.getState());
		updateRebateState(allowanceUpdate);
		// 查询折让数据
		detailItemQuery.setId(allowanceUpdate.getId());
		orderList = allowanceOrderDetailItemDao.queryDetails(detailItemQuery);
		if(null == orderList || orderList.size() == 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"该折让单信息已变更，不能审核通过！");
		}
		//查询是否开过销项进项票
		allowanceOrderQuery.setAllowanceType(allowance.getAllowanceType());
		tempSize = orderList.size();
		excludeByInvoice(allowanceOrderQuery, orderList);
		if (tempSize != orderList.size()) {
			if (AllowanceStatus.Approved.getKey().equals(allowanceUpdate.getStatus())) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
						"该折让单已开过销项或进项票，不能审核通过！");
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
						"该折让单已开过销项或进项票，不能取消审核通过！");
			}
		}
		//修改订单详情资源
		allowanceUpdate.setStatus(allowanceUpdate.getStatus());
		allowanceUpdate.setAllowanceType(allowance.getAllowanceType());
		updateOrderDetails(allowanceUpdate,orderList);

		if(AllowanceType.Seller.getKey().equals(allowance.getAllowanceType())) {
			// 修改进项票池数据
			updateInvoicePoolInData(allowance, allowanceUpdate);
		}else {
			// 修改销项票池数据
			updateInvoicePoolOutData(allowanceUpdate);
		}
		

		//code by:tuxianming
		BigDecimal allowanceTotalAmount = BigDecimal.ZERO;

		// 修改二结账户数据
		//如果折让金额为零或者折让方式是重量方式不需要对二结账后修改
		if(allowance.getAllowanceTotalAmount().doubleValue() != 0 && !allowance.getAllowanceManner().equals(AllowanceManner.Weight.getKey())) {
			AllowanceOrderItemsDto secondDto = orderList.stream().findFirst().get();
			secondDto.setAllowanceAmount(
					new BigDecimal(orderList.stream().mapToDouble(
							a -> a.getAllowanceAmount().doubleValue()).sum())
					);
			allowanceUpdate.setAllowanceType(allowance.getAllowanceType());
			updateSecondSettlement(secondDto, allowanceUpdate, allowance);//修改二结余额
			
			//code by: tuxianming
			allowanceTotalAmount = AllowanceStatus.Approved.getKey().equals(allowanceUpdate.getStatus())?secondDto.getAllowanceAmount():secondDto.getAllowanceAmount().negate();
			
		}
		
		//插入买家 折让金额 到报表里, tuxianming
		if(AllowanceType.Buyer.getKey().equals(allowance.getAllowanceType())){
			reportFinanceService.pushToReportInvoiceOut(
					consignOrderDao.selectByPrimaryKey(orderList.get(0).getOrderId()),
					allowanceUpdate.getUser(), 
					allowanceTotalAmount,
					allowanceUpdate.getStatus().equals(AllowanceStatus.Approved.getKey())? ReportBuyerInvoiceOutType.Discount: ReportBuyerInvoiceOutType.DiscountRollback,
					allowance.getAllowanceCode());
		}
		
	}
	
	/*
	 * 修改客户二结余额
	 *
	 * @return
	 * @author lx
	 */
	private void updateSecondSettlement (AllowanceOrderItemsDto secondDto, AllowanceUpdate allowanceUpdate, Allowance allowance) {
		BigDecimal balanceWithdraw = secondDto.getAllowanceAmount();//二结发生额取折让金额
		if (AllowanceType.Seller.getKey().equals(allowanceUpdate.getAllowanceType())) {
			if (!AllowanceStatus.Approved.getKey().equals(allowanceUpdate.getStatus())) {
				balanceWithdraw = balanceWithdraw.negate();//审核通过，折让金额取本身
				/**
				orderStatusService.updatePayment(allowance.getAccountId(), secondDto.getAllowanceCode(), null,
						balanceWithdraw.doubleValue(), null, allowanceUpdate.getUser().getId(), allowanceUpdate.getUser().getName(),
						new Date(), Type.ROLLBACK_REBATE.getCode());*/
				//此处原先按客户id修改二结账户余额，现按部门id lixiang 2016-3-28
//				orderStatusService.updatePayment(allowance.getDepartmentId(), secondDto.getAllowanceCode(), null,
//						balanceWithdraw.doubleValue(), null, allowanceUpdate.getUser().getId(), allowanceUpdate.getUser().getName(),
//						new Date(), Type.ROLLBACK_REBATE.getCode());
				
				accountFundService.updateAccountFund(allowance.getDepartmentId(), AssociationType.REBATE, secondDto.getAllowanceCode(), 
						AccountTransApplyType.ROLLBACK_REBATE, BigDecimal.ZERO, BigDecimal.ZERO, balanceWithdraw, BigDecimal.ZERO, 
						BigDecimal.ZERO, BigDecimal.ZERO, PayType.ALLOWANCE_SETTLEMENT, allowanceUpdate.getUser().getId(), allowanceUpdate.getUser().getName(), new Date());
				
			} else {
				/**
				orderStatusService.updatePayment(allowance.getAccountId(), secondDto.getAllowanceCode(), null,
						balanceWithdraw.doubleValue(), null, allowanceUpdate.getUser().getId(), allowanceUpdate.getUser().getName(),
						new Date(), Type.REBATE.getCode());*/
				//此处原先按客户id修改二结账户余额，现按部门id lixiang 2016-3-28
//				orderStatusService.updatePayment(allowance.getDepartmentId(), secondDto.getAllowanceCode(), null,
//						balanceWithdraw.doubleValue(), null, allowanceUpdate.getUser().getId(), allowanceUpdate.getUser().getName(),
//						new Date(), Type.REBATE.getCode());
				
				 accountFundService.updateAccountFund(allowance.getDepartmentId(), AssociationType.REBATE, secondDto.getAllowanceCode(), 
						AccountTransApplyType.REBATE, BigDecimal.ZERO, BigDecimal.ZERO, balanceWithdraw, BigDecimal.ZERO, 
						BigDecimal.ZERO, BigDecimal.ZERO, PayType.ALLOWANCE_SETTLEMENT, allowanceUpdate.getUser().getId(), allowanceUpdate.getUser().getName(), new Date());
				 
			}
		}
		if (AllowanceType.Buyer.getKey().equals(allowanceUpdate.getAllowanceType())) {
			if (AllowanceStatus.Approved.getKey().equals(allowanceUpdate.getStatus())) {
				balanceWithdraw = balanceWithdraw.negate();//审核通过，折让金额取反
				/**orderStatusService.updatePayment(secondDto.getBuyerId(), secondDto.getAllowanceCode(), null,
						balanceWithdraw.doubleValue(), null, allowanceUpdate.getUser().getId(), allowanceUpdate.getUser().getName(),
						new Date(), Type.REBATE.getCode());*/
				//此处原先按客户id修改二结账户余额，现按部门id lixiang 2016-3-28
//				orderStatusService.updatePayment(allowance.getDepartmentId(), secondDto.getAllowanceCode(), null,
//						balanceWithdraw.doubleValue(), null, allowanceUpdate.getUser().getId(), allowanceUpdate.getUser().getName(),
//						new Date(), Type.REBATE.getCode());
				accountFundService.updateAccountFund(allowance.getDepartmentId(), AssociationType.REBATE, secondDto.getAllowanceCode(), 
						AccountTransApplyType.REBATE, BigDecimal.ZERO, BigDecimal.ZERO, balanceWithdraw, BigDecimal.ZERO, 
						BigDecimal.ZERO, BigDecimal.ZERO, PayType.ALLOWANCE_SETTLEMENT, allowanceUpdate.getUser().getId(), allowanceUpdate.getUser().getName(), new Date());
			
			} else {
				/**orderStatusService.updatePayment(secondDto.getBuyerId(), secondDto.getAllowanceCode(), null,
						balanceWithdraw.doubleValue(), null, allowanceUpdate.getUser().getId(), allowanceUpdate.getUser().getName(),
						new Date(), Type.ROLLBACK_REBATE.getCode());*/
				//此处原先按客户id修改二结账户余额，现按部门id lixiang 2016-3-28
				accountFundService.updateAccountFund(allowance.getDepartmentId(), AssociationType.REBATE, secondDto.getAllowanceCode(), 
						AccountTransApplyType.ROLLBACK_REBATE, BigDecimal.ZERO, BigDecimal.ZERO, balanceWithdraw, BigDecimal.ZERO, 
						BigDecimal.ZERO, BigDecimal.ZERO, PayType.ALLOWANCE_SETTLEMENT, allowanceUpdate.getUser().getId(), allowanceUpdate.getUser().getName(), new Date());
				
			}
		}
		//增加自动抵扣还款二结余额lixiang
		accountFundService.payForCredit(secondDto.getDepartmentId(), AssociationType.REBATE, allowance.getAllowanceCode(), 0L, Constant.SYSTEMNAME, new Date());
		
	}
	
	/*
	 * 设置金额重量
	 *
	 * @return
	 * @author lx
	 */
	private void setAmountAndWeightOut(AllowanceUpdate allowanceUpdate,Optional<PoolOutDetail> outDetail,
			PoolOutModifier modifier, PoolOutDetailModifier detailModifier,
			AllowanceOrderItemsDto allowanDet) {
		if (AllowanceStatus.Approved.getKey().equals(allowanceUpdate.getStatus())) {
			modifier.setChangeAmount(allowanDet.getAllowanceAmount());
			detailModifier.setChangeAmount(allowanDet.getAllowanceAmount());
			modifier.setChangeWeight(allowanDet.getAllowanceWeight());
			detailModifier.setChangeWeight(allowanDet.getAllowanceWeight());
		}else if (AllowanceStatus.ToSubmit.getKey().equals(allowanceUpdate.getStatus())) {
			modifier.setChangeAmount(allowanDet.getAllowanceAmount().negate());
			detailModifier.setChangeAmount(allowanDet.getAllowanceAmount().negate());
			modifier.setChangeWeight(allowanDet.getAllowanceWeight().negate());
			detailModifier.setChangeWeight(allowanDet.getAllowanceWeight().negate());
		}
	}
	
	/*
	 * 销项票数据更新
	 *
	 * @return
	 * @author lx
	 */
	private void updateInvoicePoolOutData(AllowanceUpdate allowanceUpdate) {
		AllowanceDetailItemQuery detailItemQuery = new AllowanceDetailItemQuery();
		if(allowanceUpdate.getBuyerAllowanceIds() != null && allowanceUpdate.getBuyerAllowanceIds().length() > 0){//查询关联的买家的销项票数据
			detailItemQuery.setAllowanceIds(Arrays.asList(allowanceUpdate.getBuyerAllowanceIds().split(",")));
			allowanceUpdate.setId(null);
		} else {
			detailItemQuery.setId(allowanceUpdate.getAllowanceId());
		}
		List<AllowanceOrderItemsDto> dtoList = allowanceOrderDetailItemDao.queryDetails(detailItemQuery);
		//拿到订单ids
		List<Long> orderIds = dtoList.stream().map(a -> a.getOrderId()).collect(Collectors.toList());
		//原先按买家id取值，现按部门 lixiang 20163-28
		//List<Long> buyerIds = dtoList.stream().map(a -> a.getBuyerId()).collect(Collectors.toList());
		List<Long> buyerDepartmentIds = dtoList.stream().map(a -> a.getBuyerDepartmentId()).collect(Collectors.toList());
		if (orderIds == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"该折让单无订单相关数据！");
		}
		List<ConsignOrder> consignList = consignOrderDao.selectConsignOrder(orderIds);
		if (consignList == null || consignList.size() == 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"该折让单无订单相关数据！");
		}
		//拿到交易员ids
		List<Long> ownerIds = consignList.stream().map(a -> a.getOwnerId()).collect(Collectors.toList());
		//销项票数据
		// 按部门id取数 lixiang 2016-3-28 
		//TODO  现在有个问题是 订单二结后销项票数据inv_pool_out存在问题，部门及部门id错误
		//List<PoolOut> poolOutList = poolOutDao.selectOwnerIdAndBuyerId(ownerIds, buyerIds);
		List<PoolOut> poolOutList = poolOutDao.selectOwnerIdAndBuyerId(ownerIds, buyerDepartmentIds);
		if (poolOutList == null || poolOutList.size() == 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"该折让单无销项或进项票数据！");
		} 
		List<Long> poolOutIds = poolOutList.stream().map(a -> a.getId()).collect(Collectors.toList());
		PoolOutDetailDto poolOutDetailDto = new PoolOutDetailDto();
		poolOutDetailDto.setPoolOutIds(poolOutIds);
		//查销项票详情数据
		List<PoolOutDetail> outDetailLists = poolOutDetailDao.selectByPoolOutId(poolOutDetailDto);
		if (outDetailLists == null || outDetailLists.size() == 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"该折让单无销项或进项票数据！");
		} 
		for (AllowanceOrderItemsDto allowanDet : dtoList) {
			Optional<PoolOutDetail> poolOutDetail = outDetailLists.stream().filter(a -> a.getMaterial().trim().equals(allowanDet.getMaterial().trim())
					&& a.getNsortName().trim().equals(allowanDet.getNsortName().trim())
					&& a.getSpec().trim().equals(allowanDet.getSpec().trim())).findFirst();
			if (!poolOutDetail.isPresent()){
				continue;
			}
			Optional<ConsignOrder> opOrd = consignList.stream().filter(a -> a.getId().equals(allowanDet.getOrderId())).findFirst();
			if (!opOrd.isPresent()) {
				continue;
			}
			//原先按客户id取值，现按部门id取
			//Long buyerId = opOrd.get().getAccountId();
			Long departmentId = opOrd.get().getDepartmentId();
			Long ownerId = opOrd.get().getOwnerId();
			Optional<PoolOut> opPoolOut = poolOutList.stream().filter(a -> a.getDepartmentId().equals(departmentId) && a.getOwnerId().equals(ownerId)).findFirst();
			if (!opPoolOut.isPresent()) {
				continue;
			}
			PoolOutModifier modifier = new PoolOutModifier();
			PoolOutDetailModifier detailModifier = new PoolOutDetailModifier();
			setAmountAndWeightOut(allowanceUpdate, poolOutDetail,  modifier, detailModifier, allowanDet);
			//关联查询销项详情数据，修改进项表数据
			modifier.setPoolOutId(opPoolOut.get().getId());
			modifier.setLastUpdatedBy(allowanceUpdate.getUser().getName());
			modifier.setLastUpdated(new Date());
			poolOutDao.updatePoolOut(modifier);
			//修改销项详情表数据
			detailModifier.setPoolOutDetialId(poolOutDetail.get().getId());
			detailModifier.setLastUpdatedBy(allowanceUpdate.getUser().getName());
			detailModifier.setLastUpdated(new Date());
			poolOutDetailDao.updatePoolOutDetail(detailModifier);
		}
	}	

	/*
	 * 进项票数据更新
	 *
	 * @return
	 * @author lx
	 */
	private void updateInvoicePoolInData(Allowance allowance, AllowanceUpdate allowanceUpdate) {
		//查询进项池数据
		AllowanceDetailItemQuery detailItemQuery = new AllowanceDetailItemQuery();
		PoolIn poolIn = poolInDao.selectSellerId(allowance.getAccountId(), allowance.getDepartmentId());
		detailItemQuery.setId(allowanceUpdate.getAllowanceId());
		List<AllowanceOrderItemsDto> dtoList = allowanceOrderDetailItemDao.queryDetails(detailItemQuery);
		Long poolInId = poolIn.getId();
		//查询进项票详情数据
		List<PoolInDetail> poolInDetails = poolInDetailDao.selectByPoolInId(poolInId);
		PoolInModifier modifier = new PoolInModifier();
		PoolInDetailModifier detailModifier = new PoolInDetailModifier();
		if (poolIn == null || poolInDetails.size() == 0 || poolInDetails == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"该折让单无销项或进项票数据！");
		}
		for (AllowanceOrderItemsDto allowanceOrderItemsDto : dtoList) {
			Optional<PoolInDetail> poolInDetail = poolInDetails.stream().filter(a -> a.getMaterial().trim().equals(allowanceOrderItemsDto.getMaterial().trim()) 
						&& a.getNsortName().trim().equals(allowanceOrderItemsDto.getNsortName().trim()) 
						&& a.getSpec().trim().equals(allowanceOrderItemsDto.getSpec().trim())).findFirst();
			if (!poolInDetail.isPresent()){
				continue;
			}
			if (AllowanceStatus.Approved.getKey().equals(allowanceUpdate.getStatus())) {
				modifier.setChangeAmount(allowanceOrderItemsDto.getAllowanceAmount());
				modifier.setChangeWeight(allowanceOrderItemsDto.getAllowanceWeight());
				detailModifier.setChangeAmount(allowanceOrderItemsDto.getAllowanceAmount());
				detailModifier.setChangeWeight(allowanceOrderItemsDto.getAllowanceWeight());
			}else if (AllowanceStatus.ToSubmit.getKey().equals(allowanceUpdate.getStatus())) {
				modifier.setChangeAmount(BigDecimal.ZERO.subtract(allowanceOrderItemsDto.getAllowanceAmount()));
				modifier.setChangeWeight(BigDecimal.ZERO.subtract(allowanceOrderItemsDto.getAllowanceWeight()));
				detailModifier.setChangeAmount(BigDecimal.ZERO.subtract(allowanceOrderItemsDto.getAllowanceAmount()));
				detailModifier.setChangeWeight(BigDecimal.ZERO.subtract(allowanceOrderItemsDto.getAllowanceWeight()));
			}
			modifier.setPoolInId(poolIn.getId());
			modifier.setLastUpdatedBy(allowanceUpdate.getUser().getName());
			modifier.setLastUpdated(new Date());
			poolInDao.updatePoolIn(modifier);
			detailModifier.setPoolInDetialId(poolInDetail.get().getId());
			detailModifier.setLastUpdatedBy(allowanceUpdate.getUser().getName());
			detailModifier.setLastUpdated(new Date());
			poolInDetailDao.updatePoolInDetail(detailModifier);
		}
	}

	/**
	 * 修改折让单
	 *
	 * @return
	 * @author lcw
	 */
	@Override
	@Transactional
	public void updateAllowance(AllowanceOrderSave allowanceOrderSave,List<AllowanceOrderItemsDto> orderList) {
		Allowance allowance = allowanceDao.selectByPrimaryKey(allowanceOrderSave.getId());
		if (!AllowanceStatus.NotThrough.getKey().equals(allowance.getStatus())
				&& !AllowanceStatus.ToSubmit.getKey().equals(allowance.getStatus())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "当前状态不能编辑折让单。");
		}

		//检查买家折让单关联的订单是否二次结算订单关闭
		checkClosedOrder(allowance.getId());

		List<Long> ids=new ArrayList<>();
		ids.add(allowance.getId());
		//逻辑删除关联订单详情 取消关联
		deleteOrderDetailItemsByAllowanceId(allowanceOrderSave.getUser(), ids);

		//逻辑删除关联订单 取消关联
		deleteOrderItemsByAllowanceId(allowanceOrderSave.getUser(), ids);
		// 保存新数据
		saveCommon(allowanceOrderSave, orderList);

	}

	/**
	 * 关闭折让单并取消关联关系
	 *
	 * @return
	 * @author cc
	 */
	@Override
	@Transactional
	public void closeOrder(User user, Allowance allowance) {
		AllowanceForUpdateDto dto = new AllowanceForUpdateDto();
		try {
			BeanUtils.copyProperties(dto, allowance);
		} catch (Exception e) {
			logger.error("copy allowance failed.", e);
			throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "copy allowance failed");
		}

		//关闭折让单
		dto.setStatus(AllowanceStatus.Closed.getKey());
		dto.setOldStatus(AllowanceStatus.NotThrough.getKey());
		dto.setLastUpdated(new Date());
		dto.setLastUpdatedBy(user.getName());
		dto.setModificationNumber(allowance.getModificationNumber() + 1);
		int count = allowanceDao.updateByCondition(dto);
		if(count  == 0){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"关闭折让单失败");
		}

		//如果是卖家折让单且折让方式为重量或重量和金额方式 就同步关闭相关联买家折让单
		if (StringUtils.equals(AllowanceType.Seller.getKey(),allowance.getAllowanceType())
				&& !StringUtils.equals(AllowanceManner.Amount.getKey(),allowance.getAllowanceManner())){
			dto.setAllowanceId(allowance.getId());//设置卖家折让id
			int updateBuyerAllowceCount = allowanceDao.updateStatusByAllowanceId(dto);
			if(updateBuyerAllowceCount == 0){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"关闭卖家关联的买家折让单失败");
			}
		}

	}

	/*
	 * 逻辑删除从表数据
	 *
	 * @return
	 * @author dxy
	 */
	private void deleteOrderItemsByAllowanceId(User user,List<Long> allowanceIds){
		int count = allowanceOrderItemDao.updateDeletedByAllowanceId(allowanceIds, user.getName());
		if (count == 0){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"取消关联订单失败");
		}
	}

	/*
	 * 逻辑删除详情关联表数据
	 *
	 * @return
	 * @author dxy
	 */
	private void deleteOrderDetailItemsByAllowanceId(User user,List<Long> allowanceIds){
		int count =  allowanceOrderDetailItemDao.updateDeletedByAllowanceId(allowanceIds, user.getName());
		if (count == 0){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"取消关联订单详情失败");
		}
	}

	/**
	 * 查询折让主表数据
	 *
	 * @return
	 * @author cc
	 */
	@Override
	public List<AllowanceDto> queryAllowanceList(AllowanceListQuery query) {
		List<AllowanceDto> list= allowanceDao.queryAllowanceList(query);
		if(list!=null){
			list.stream().forEach(a ->a.setStatus(AllowanceStatus.getNameByKey(a.getStatus())));
		}
		return list;
	}

	/**
	 * 查询折让主表数据数量
	 *
	 * @return
	 * @author cc
	 */
	@Override
	public int countAllowanceList(AllowanceListQuery query) {
		return allowanceDao.countAllowanceList(query);
	}

	/**
	 * 删除卖家对应的买家折让单
	 *
	 * @return
	 * @author cc
	 */
	private void deleteBuyerAllowanceById(Long id,User user){
		if(id!=null){
			List<Allowance> allowanceBuyer = allowanceDao.queryByAllowanceId(id);
			List<Long> buyerIds = new ArrayList<>();
			if(allowanceBuyer!=null){
				for (Allowance item : allowanceBuyer) {
					buyerIds.add(item.getId());
				}
			}
			if (buyerIds.size() > 0) {
				//逻辑删除关联订单详情 取消关联
				deleteOrderDetailItemsByAllowanceId(user, buyerIds);

				//逻辑删除关联订单 取消关联
				deleteOrderItemsByAllowanceId(user, buyerIds);
				if (allowanceDao.deleteAllowanceByIds(buyerIds, user.getName()) != buyerIds.size()) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除对应买家折让单失败");
				}
			}
		}
	}

	/*
	 * 删除买家对应的卖家折让单
	 *
	 * @return
	 * @author cc
	 */
//	private void deleteSellerAllowanceById(Long allowaceId,User user){
//		if(allowaceId!=null){
//			Allowance allowanceSeller = allowanceDao.selectByPrimaryKey(allowaceId);
//			if(allowanceSeller!=null){
//				List<Long> ids = new ArrayList<>();
//				ids.add(allowanceSeller.getId());
//				//逻辑删除关联订单详情 取消关联
//				deleteOrderDetailItemsByAllowanceId(user, ids);
//				//逻辑删除关联订单 取消关联
//				deleteOrderItemsByAllowanceId(user, ids);
//
//				if (allowanceDao.deleteAllowanceByIds(ids, user.getLoginId()) != ids.size()) {
//					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除对应卖家折让单失败");
//				}
//			}
//		}
//	}

	/**
	 * 删除卖家折让单
	 *
	 * @return
	 * @author cc
	 */
	@Override
	@Transactional
	public void deleteAllowance(Long id, User user) {
		Allowance allowance = allowanceDao.selectByPrimaryKey(id);
		if (!AllowanceManner.Amount.getKey().equals(allowance.getAllowanceManner())) {
			if (AllowanceType.Buyer.getKey().equals(allowance.getAllowanceType())) { 
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该买家折让单与卖家折让单有关联，不能单独操作,请到卖家折让单里删除！");
			} else {
				// 删除买家折让单数据
				deleteBuyerAllowanceById(allowance.getId(),user);
			}
		}
		List<Long> ids = new ArrayList<>();
		ids.add(allowance.getId());
		//逻辑删除关联订单详情 取消关联
		deleteOrderDetailItemsByAllowanceId(user, ids);

		//逻辑删除关联订单 取消关联
		deleteOrderItemsByAllowanceId(user, ids);

		int count=allowanceDao.deleteAllowanceByIds(ids, user.getName());

		if ( count <= 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除折让单失败");
		}
	}

	/**
	 * 修改卖家折让单，如果是重量方式则同步修改买家折让单
	 *
	 * @return
	 * @author dxy
	 */
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void updateSellerAllowance(MultipartFile image, AllowanceOrderSave allowanceOrderSave, List<AllowanceOrderItemsDto> orderList) {
		Allowance allowance = selectByPrimaryKey(allowanceOrderSave.getId());

		if(allowance == null || !StringUtils.equals(allowanceOrderSave.getOldStatus(), allowance.getStatus())){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "折让单不存在或当前状态不能编辑折让单。");
		}

		//检查卖家折让单关联的订单是否二次结算订单关闭
		checkClosedOrder(allowance.getId());

		//逻辑删除卖家相关联的订单及详情
		unbindOrderAndDetails(allowanceOrderSave.getUser(), Arrays.asList(allowanceOrderSave.getId()));

		//保存新数据
		saveSellerCommon(image, allowanceOrderSave, orderList);
	}

	private void checkClosedOrder(Long allowanceId){
		//检查折让单关联的订单是否二次结算订单关闭
		int closedOrder = allowanceOrderItemDao.countSecondClosedOrderByAllwanceId(allowanceId);
		if (closedOrder > 0){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该折让单关联的交易单已关闭，不能提交审核。");
		}
	}

	/*
	 * 逻辑删除
	 *
	 * @return
	 * @author dxy
	 */
	private void unbindOrderAndDetails(User user,List<Long> ids){
		//逻辑删除关联订单详情 取消关联
		deleteOrderDetailItemsByAllowanceId(user,ids);

		//逻辑删除关联订单 取消关联
		deleteOrderItemsByAllowanceId(user,ids);
	}

	/*
	 * 修改订单资源折让重量和卖家折让金额
	 *
	 * @return
	 * @author dq
	 */
	private void updateOrderDetails(AllowanceUpdate allowanceUpdate, List<AllowanceOrderItemsDto> orderList) {
		List<Long> orderDetailIds = orderList.stream().map(a -> a.getOrderDetailId()).distinct().collect(Collectors.toList());
		List<ConsignOrderItems> orderDetails = consignOrderItemsDao.queryByIds(orderDetailIds);
		if(orderDetailIds.size() != orderDetails.size()) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该折让单对应的资源已经改变"); 
		}
		orderDetails.forEach(orderDetail -> {
			List<AllowanceOrderItemsDto> tempList = orderList.stream().filter(
					a -> a.getOrderDetailId().equals(orderDetail.getId())).collect(Collectors.toList());
			OrderDetailModifier modifier = new OrderDetailModifier();
			if (AllowanceType.Seller.getKey().equals(allowanceUpdate.getAllowanceType())) {
				modifier.setChangeWeight(new BigDecimal(tempList.stream().mapToDouble(
						a -> a.getAllowanceWeight().doubleValue()).sum()));
				modifier.setChangeAmount(new BigDecimal(tempList.stream().mapToDouble(
						a -> a.getAllowanceAmount().doubleValue()).sum()));
			} else { //buyer
				modifier.setChangeBuyerAmount(new BigDecimal(tempList.stream().mapToDouble(
						a -> a.getAllowanceAmount().doubleValue()).sum()));
			}
			if (!AllowanceStatus.Approved.getKey().equals(allowanceUpdate.getStatus())) {
				orderDetailModifierNegation(modifier);
			}
			modifier.setOrderDetailId(orderDetail.getId());
			modifier.setLastUpdatedBy(allowanceUpdate.getUser().getName());
			modifier.setLastUpdated(new Date());
			int i = consignOrderItemsDao.updateOrderDetail(modifier);
			if (i != 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该折让单对应的资源已经改变");
			}
		});
		
		
	}
	
	/*
	 * 订单资源折让金额重量取反
	 *
	 * @return
	 * @author dq
	 */
	private void orderDetailModifierNegation (OrderDetailModifier modifier) {
		modifier.setChangeWeight(modifier.getChangeWeight().negate());
		modifier.setChangeAmount(modifier.getChangeAmount().negate());
		modifier.setChangeBuyerAmount(modifier.getChangeBuyerAmount().negate());
	}

	/**
	 * 编辑或保存买家折让单
	 *
	 * @return
	 * @author dq
	 */
	@Override
	public void saveBuyerAllowances(AllowanceOrderSave allowanceOrderSave) {
		Long id = allowanceOrderSave.getAllowanceId();
		if(null == id) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "不存在卖家折让单");
		}
		Allowance allowance = allowanceDao.selectByPrimaryKey(id);
		allowanceOrderSave.setAllowanceType(allowance.getAllowanceType());
		allowanceOrderSave.setAllowanceManner(allowance.getAllowanceManner());
		if(!AllowanceManner.Amount.getKey().equals(allowance.getAllowanceManner())) {
			allowanceOrderSave.setStatus(allowance.getStatus());
		} else {
			allowanceOrderSave.setStatus(AllowanceStatus.ToSubmit.getKey());
		}
		
		AllowanceDetailItemQuery detailItemQuery = new AllowanceDetailItemQuery();
		detailItemQuery.setId(id);
		List<AllowanceOrderItemsDto> orderList = allowanceOrderDetailItemService.queryDetails(detailItemQuery);
		dereplication(orderList);
		if(AllowanceType.Seller.getKey().equals(allowanceOrderSave.getAllowanceType())) {
			if(AllowanceManner.Amount.getKey().equals(allowanceOrderSave.getAllowanceManner())) {
				allowanceOrderSave.setId(null);
				allowanceOrderSave.setAllowanceType(AllowanceType.Buyer.getKey());
				List<Long> buyerDeptIds = orderList.stream().map(
						a -> a.getBuyerDepartmentId()).distinct().collect(Collectors.toList());
				List<AllowanceOrderItemsDto> buyerList = getBuyerAllowance(orderList, buyerDeptIds);
				dereplication(buyerList);

				buyerDeptIds.forEach(buyerDeptId -> {
					List<AllowanceOrderItemsDto> tempList = buyerList.stream().filter(
							a -> a.getBuyerDepartmentId().equals(buyerDeptId)).collect(Collectors.toList());
					allowanceOrderSave.setAllowanceId(id);
					saveCommon(allowanceOrderSave,tempList);
				});
			}
		}
		
	}

	/*
	 * 关闭折让单
	 *
	 * @return
	 * @author dxy
	 */
	@Override
	@Transactional
	public void automaticClose() {
		List<Allowance> list = allowanceDao.selectAllNeedToClose();
		if (list == null || list.size() == 0) {
			return;
		}
		for (Allowance allowance : list) {
			AllowanceForUpdateDto dto = new AllowanceForUpdateDto();
			try {
				BeanUtils.copyProperties(dto, allowance);
			} catch (Exception e) {
				logger.error("copy allowance failed.", e);
				throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "copy allowance failed");
			}
			//关闭折让单
			dto.setId(allowance.getId());
			dto.setStatus(AllowanceStatus.Closed.getKey());//系统自动关闭折让单
			dto.setOldStatus(AllowanceStatus.NotThrough.getKey());
			dto.setLastUpdated(new Date());
			dto.setLastUpdatedBy(SYSTEM);
			dto.setModificationNumber(allowance.getModificationNumber() + 1);
			int count = allowanceDao.updateByCondition(dto);
			if (count  == 0) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"关闭折让单失败");
			}
		}
//		List<Long> ids = list.stream().map(
//				a -> a.getId()).distinct().collect(Collectors.toList());
//		User user = new User ();
//		//逻辑删除关联订单详情 取消关联
//		user.setLastUpdatedBy(SYSTEM);
//		deleteOrderDetailItemsByAllowanceId(user, ids);
//
//		//逻辑删除关联订单 取消关联
//		deleteOrderItemsByAllowanceId(user, ids);
	}


	@Override
	public List<CustAccountDto> queryDepartmentByAccoutId(Long accountId) {
		return allowanceDao.queryDepartmentByAccoutId(accountId);
	}
	/**
	 * 修改折让金额
	 * tuxianming
	 * @param allowanceItemId
	 * @param amount
	 * @param login
	 */
	public void updateAllowance(List<AllowanceDetailItemQuery> queries,User login) {
		
		if(queries==null || queries.size()==0)
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "未提交任何数据");
		
		queries.forEach(query -> {
			if(query.getAmount()==null)
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "金额为空！");
		});
		
		Allowance allowance = allowanceDao.selectByPrimaryKey(queries.get(0).getAllowanceId());
		if(allowance.getAllowanceManner().equals(AllowanceManner.All.toString()) 
				&& allowance.getAllowanceType().equals(AllowanceType.Buyer.toString())){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "仅支持买家金额和重量的折让方式的金额修改！");
		}
		
		List<AllowanceOrderItemsDto> items = allowanceOrderDetailItemDao.queryDetails(new AllowanceDetailItemQuery().setAllowanceKey(queries.get(0).getAllowanceId()));
		
		queries.forEach(query -> {
			//AllowanceOrderItemsDto item = items.stream().filter(a -> a.getId().equals(query.getId())).findFirst().get();
			
			Optional<AllowanceOrderItemsDto> optional = items.stream().filter(a -> a.getId().equals(query.getId())).findFirst();
			AllowanceOrderItemsDto  item = null;
			if(!optional.isPresent())
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该折让单不存在！");
			
			item = optional.get();
			
			//修改金额
			BigDecimal prevAmount = item.getAllowanceAmount();
			
			AllowanceOrderDetailItem updateObj = new AllowanceOrderDetailItem();
			updateObj.setId(item.getId());
			updateObj.setAllowanceAmount(query.getAmount());
			
			Date date = new Date();
			
			updateObj.setLastUpdated(date);
			updateObj.setLastUpdatedBy(login.getName());
			
			allowanceOrderDetailItemDao.updateByPrimaryKeySelective(updateObj);
			
			//保存以前的金额
			AllowanceItemAmountHistory history = new AllowanceItemAmountHistory();
			history.setAllowanceItemDetailId(query.getId());
			history.setAllowanceAmount(prevAmount!=null?prevAmount:BigDecimal.ZERO);
			history.setCreated(date);
			history.setLastUpdated(date);
			history.setCreatedBy(login.getName());
			history.setLastUpdatedBy(login.getName());
			
			allowanceItemAmountHistoryDao.insertSelective(history);
			
		});		

	}
}
