package com.prcsteel.platform.order.service.invoice.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.account.model.enums.InvoiceDataStatus;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.CbmsNumberUtil;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.model.dto.InvoiceApplicationDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceApplicationDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDetailItemsListVoDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDetailItemsVoDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDetailListVoDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDetailVoDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutItemDetailsDto;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.enums.DistributionStatus;
import com.prcsteel.platform.order.model.enums.InvoiceInDetailStatus;
import com.prcsteel.platform.order.model.enums.InvoiceInStatus;
import com.prcsteel.platform.order.model.enums.InvoiceOutApplyStatus;
import com.prcsteel.platform.order.model.enums.OrderStatusType;
import com.prcsteel.platform.order.model.model.InvoiceOutApply;
import com.prcsteel.platform.order.model.model.InvoiceOutApplyDetail;
import com.prcsteel.platform.order.model.model.InvoiceOutItemDetail;
import com.prcsteel.platform.order.model.model.OrderAuditTrail;
import com.prcsteel.platform.order.model.query.ConsignOrderItemsQuery;
import com.prcsteel.platform.order.model.query.ConsignOrderQuery;
import com.prcsteel.platform.order.model.query.InvoiceOutApplyQuery;
import com.prcsteel.platform.order.persist.dao.ConsignOrderDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutApplyDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutApplyDetailDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutItemDetailDao;
import com.prcsteel.platform.order.persist.dao.OrderStatusDao;
import com.prcsteel.platform.order.persist.dao.PoolOutDao;
import com.prcsteel.platform.order.service.invoice.InvoiceInAllowanceService;
import com.prcsteel.platform.order.service.invoice.InvoiceOutApplyService;

/**
 * Created by rolyer on 15-8-3.
 */
@Service("invoiceOutApplyService")
public class InvoiceOutApplyServiceImpl implements InvoiceOutApplyService {

	@Autowired
	private InvoiceOutApplyDao invoiceOutApplyDao;

	@Autowired
	private InvoiceOutApplyDetailDao invoiceOutApplyDetailDao;

	@Autowired
	private InvoiceOutItemDetailDao invoiceOutItemDetailDao;

	@Autowired
	private PoolOutDao poolOutDao;

	@Autowired
	private ConsignOrderItemsDao consignOrderItemsDao;

	@Autowired
	private ConsignOrderDao consignOrderDao;

	@Autowired
	private OrderStatusDao orderStatusDao;

	@Resource
	private InvoiceInAllowanceService inAllowanceService;
	
	@Resource
	private SysSettingService sysSettingService;

	/**
	 * 分页查询申请开票数据(主表)
	 * 
	 * @param orgIds
	 *            服务中心Id集合
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param start
	 *            开始位置
	 * @param length
	 *            数据量
	 * @param statusList
	 *            状态集合
	 * @return List<InvoiceOutApplyDto>
	 * @author DQ
	 */
	@Override
	public List<InvoiceOutApplyDto> queryInvoiceOutApply(List<Long> orgIds, String startTime, String endTime,
			Integer start, Integer length, List<String> statusList) {
		List<InvoiceOutApplyDto> list = invoiceOutApplyDao.queryInvoiceOutApply(orgIds, startTime, endTime, start,
				length, statusList);
		for (InvoiceOutApplyDto applyDto : list) {
			applyDto.setStatusName(InvoiceOutApplyStatus.getName(applyDto.getStatus()));
		}
		return list;
	}

	/**
	 * 查询申请开票数据总量
	 * 
	 * @param orgIds
	 *            服务中心Id集合
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param statusList
	 *            状态集合
	 * @return int
	 * @author DQ
	 */
	public int totalInvoiceOutApply(List<Long> orgIds, String startTime, String endTime, List<String> statusList) {
		return invoiceOutApplyDao.totalInvoiceOutApply(orgIds, startTime, endTime, statusList);
	}


	/**
	 * (事物) 保存申请开票详情所有数据(附带关联关系)
	 * 
	 * @param user
	 *            用户对象
	 * @param invoiceOutApplyDto
	 *            申请开票详情所有数据(附带关联关系)
	 * @return void
	 * @throws BusinessException
	 * @author DQ
	 */
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void saveNewInvoicing(User user, InvoiceOutApplyDto invoiceOutApplyDto) {
		List<Long> invoiceOrderitemIds = invoiceOutApplyDto.getDetailList().stream()
				.map(a -> a.getItemDetailList().stream().map(b -> b.getInvoiceOrderitemId()).collect(Collectors.toList()))
				.collect(Collectors.reducing(null, (prev, next) -> {
					if (prev != null) {
						next.addAll(prev);
					}
					return next;
				})).stream().distinct().collect(Collectors.toList());
		int countItemDetail = invoiceOutItemDetailDao.queryCountByInvoiceOrderitemIds(invoiceOrderitemIds);
		if(countItemDetail > 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该申请已处理");
		}
		Date date = new Date();
		List<Long> orderDetailIds = invoiceOutApplyDto.getDetailList().stream()
				.map(a -> a.getItemDetailList().stream().map(b -> b.getOrderDetailId()).collect(Collectors.toList()))
				.collect(Collectors.reducing(null, (prev, next) -> {
					if (prev != null) {
						next.addAll(prev);
					}
					return next;
				})).stream().distinct().collect(Collectors.toList());
		List<Long> orderIds = consignOrderDao.queryOrderIdsByOrderDetailIds(orderDetailIds);
		updateOrderStatus(orderIds, user, date,false);
		// 保留修改记录
		invoiceOutApplyDto.setCreated(date);
		invoiceOutApplyDto.setCreatedBy(user.getName());
		invoiceOutApplyDto.setLastUpdated(date);
		invoiceOutApplyDto.setLastUpdatedBy(user.getName());
		// 设置修改次数
		invoiceOutApplyDto.setModificationNumber(0);
		// 插入申请开票主表数据
		int flag = invoiceOutApplyDao.insertSelective(invoiceOutApplyDto);
		if (flag < 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入申请开票表数据失败！");
		}
		// 获取主键ID
		List<InvoiceOutApplyDetailDto> detailList = invoiceOutApplyDto.getDetailList();
		if (detailList.size() < 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "无效的申请开票数据！");
		}
		for (InvoiceOutApplyDetailDto detail : detailList) {
			// 保留修改记录
			detail.setInvoiceOutApplyId(invoiceOutApplyDto.getId());
			detail.setCreated(date);
			detail.setCreatedBy(user.getName());
			detail.setLastUpdated(date);
			detail.setLastUpdatedBy(user.getName());
			// 设置修改次数
			detail.setModificationNumber(0);
			// 插入申请开票详情表数据
			flag = invoiceOutApplyDetailDao.insertSelective(detail);
			if (flag < 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入申请开票详情表数据失败！");
			}
			List<InvoiceOutItemDetail> itemDetails = new ArrayList<InvoiceOutItemDetail>();
			for (InvoiceOutItemDetailsDto itemDetail : detail.getItemDetailList()) {
				// 设置申请开票详情ID
				itemDetail.setApplyDetailId(detail.getId());
				// 保留修改记录
				itemDetail.setCreated(date);
				itemDetail.setCreatedBy(user.getName());
				itemDetail.setLastUpdated(date);
				itemDetail.setLastUpdatedBy(user.getName());
				// 设置修改次数
				itemDetail.setModificationNumber(0);
				// 添加进数组
				itemDetails.add(itemDetail);
			}
			if (itemDetails.size() < 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "无效的申请开票数据！");
			}
			// 批量插入详情关联表数据
			flag = invoiceOutItemDetailDao.batchInsert(itemDetails);
			if (flag < 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入详情关联表数据失败！");
			}
		}
	}

	/**
	 * 修改订单的状态
	 *
	 * @param orderIds  订单Id
	 * @param user 		操作用户
	 * @param now 		审核时间
	 * @param isNoPass  是否审核不通过
	 * *
	 */
	private void updateOrderStatus(List<Long> orderIds, User user, Date now,Boolean isNoPass) {

		// 修改订单状态为已开票
		if (orderIds.size() > 0) {
			ConsignOrderQuery consignOrderQuery = new ConsignOrderQuery();
			consignOrderQuery.setOrderIds(orderIds);
			// 审核不通过需要把订单状态修改回为 待开票申请
			if(isNoPass) {
				consignOrderQuery.setStatus(ConsignOrderStatus.INVOICEREQUEST.getCode());
				consignOrderQuery.setOldStatus(ConsignOrderStatus.INVOICE.getCode());
			}
			else {
				consignOrderQuery.setStatus(ConsignOrderStatus.INVOICE.getCode());
				consignOrderQuery.setOldStatus(ConsignOrderStatus.INVOICEREQUEST.getCode());
			}
			consignOrderQuery.setLastUpdatedBy(user.getLoginId());
			Integer successTotal = consignOrderDao.updateStatusByConsignOrderQuery(consignOrderQuery);
			// 添加订单状态记录
			List<OrderAuditTrail> records = new ArrayList<>();
			orderIds.forEach(id -> {
				OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
				orderAuditTrail.setOrderId(id);
				orderAuditTrail.setOperatorId(user.getId());
				orderAuditTrail.setOperatorName(user.getName());
				orderAuditTrail.setLastUpdated(now);
				orderAuditTrail.setLastUpdatedBy(user.getLoginId());
				if(isNoPass) {
					orderAuditTrail.setSetToStatus(ConsignOrderStatus.INVOICEREQUEST.getCode());
				}
				else {
					orderAuditTrail.setSetToStatus(ConsignOrderStatus.INVOICE.getCode());
				}
				orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
				orderAuditTrail.setCreated(now);
				orderAuditTrail.setCreatedBy(user.getLoginId());
				orderAuditTrail.setModificationNumber(0);
				records.add(orderAuditTrail);
			});
			successTotal = orderStatusDao.batchInsert(records);
			if (successTotal != orderIds.size()) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入订单状态失败！");
			}
		}
	}

	/**
	 * (事物) 逻辑删除申请开票详情所有数据
	 * 
	 * @param id
	 *            买家ID
	 * @param user
	 *            用户对象
	 * @return void
	 * @author DQ
	 */
	@Override
	@Transactional
	public void delete(Long id, User user) {
		List<Long> applyDetailIds = invoiceOutApplyDetailDao.selectIdByPrimaryKey(id);
		if(null == applyDetailIds || applyDetailIds.size() < 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该申请已处理");
		}
		
		int resultSize = invoiceOutApplyDao.deleteForId(id, user.getName(),
				Arrays.asList(InvoiceOutApplyStatus.PENDING_SUBMIT.getValue(),
				InvoiceOutApplyStatus.PENDING_APPROVAL.getValue(),
				InvoiceOutApplyStatus.DISAPPROVE.getValue()));
		if(resultSize!=1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该申请已处理");
		}
		invoiceOutApplyDetailDao.deleteForInvoiceOutApplyId(id, user.getName());
		invoiceOutItemDetailDao.deleteByDetailIds(applyDetailIds, user.getName());
	}

	/**
	 * 打回申请开票
	 * 
	 * @param id
	 *            买家ID
	 * @param user
	 *            用户对象
	 * @param status
	 *            状态
	 * @return void
	 * @author DQ
	 */
	@Override
	public void revoke(Long id, String status, User user) {
		InvoiceOutApply invoiceOutApply = invoiceOutApplyDao.selectByPrimaryKey(id);
		if (null == invoiceOutApply || !invoiceOutApply.getStatus().equals(InvoiceOutApplyStatus.APPROVED.getValue())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该申请已处理");
		}
		invoiceOutApply.setId(id);
		invoiceOutApply.setStatus(status);
		invoiceOutApply.setLastUpdated(new Date());
		invoiceOutApply.setLastUpdatedBy(user.getName());
		invoiceOutApply.setModificationNumber(invoiceOutApply.getModificationNumber() + 1);

		invoiceOutApplyDao.updateByPrimaryKeySelective(invoiceOutApply);
	}

	/**
	 * 审核申请开票
	 * 
	 * @param id
	 *            买家ID
	 * @param user
	 *            用户对象
	 * @param status
	 *            状态
	 * @return void
	 * @author DQ
	 */
	@Override
	public void approve(Long id, String status, User user) {
		InvoiceOutApply invoiceOutApply = invoiceOutApplyDao.selectByPrimaryKey(id);
		if (null == invoiceOutApply || !invoiceOutApply.getStatus().equals(InvoiceOutApplyStatus.PENDING_APPROVAL.getValue())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该申请已处理");
		}
		invoiceOutApply.setId(id);
		invoiceOutApply.setStatus(status);
		invoiceOutApply.setLastUpdated(new Date());
		invoiceOutApply.setLastUpdatedBy(user.getName());
		invoiceOutApply.setModificationNumber(invoiceOutApply.getModificationNumber() + 1);

		invoiceOutApplyDao.updateByPrimaryKeySelective(invoiceOutApply);

		// 如果是审核不通过，那么订单的申请状态需要修改回去
		if (InvoiceOutApplyStatus.DISAPPROVE.getValue().equals(status)) {
			List<Long> orderIds = invoiceOutApplyDao.selectOrderIdsByApplyId(id);
			if (orderIds != null && orderIds.size() > 0) {
				updateOrderStatus(orderIds, user, new Date(), true);
			}
		}
	}

	/**
	 * 批量更新状态
	 * 
	 * @param ids
	 *            被新的id集合
	 * @param status
	 *            状态
	 * @return
	 * @author LCW
	 */
	@Override
	public int batchUpdateStatusById(List<Long> ids, String status) {
		return invoiceOutApplyDao.batchUpdateStatusById(ids, status);
	}

	/**
	 * 查询申请开票总额
	 * 
	 * @param invOutApplyQuery
	 *            查询条件
	 * @return 申请总额
	 * @author LCW
	 */
	@Override
	public BigDecimal queryTotalApplyAmount(InvoiceOutApplyQuery invOutApplyQuery) {
		return invoiceOutApplyDao.queryTotalApplyAmount(invOutApplyQuery);
	}

	/**
	 * 进项票打回
	 *
	 * @param invoiceId  进项票ID
	 * @param user       用户对象
	 * @return void
	 * @author DQ
	 */
	@Override
	public void invoiceInNegative(Long invoiceId, User user) {
		// 删除进项票折让关联表数据
		inAllowanceService.deleteByInvoiceInId(invoiceId, user.getName());

		List<String> statusList = Arrays.asList(InvoiceOutApplyStatus.PENDING_SUBMIT.getValue(), 
				InvoiceOutApplyStatus.PENDING_APPROVAL.getValue(), InvoiceOutApplyStatus.APPROVED.getValue(),
				InvoiceOutApplyStatus.PARTIAL_INVOICED.getValue());
		List<Long> outItemDetailIds = invoiceOutItemDetailDao.queryOutItemDetailIdsByInvoiceId(invoiceId, statusList);
		if(outItemDetailIds == null || outItemDetailIds.size() < 1){
			return;
		}
		List<InvoiceOutItemDetail> outItemDetailList = invoiceOutItemDetailDao.queryOutItemDetailOrderByIds(invoiceId,outItemDetailIds);
		if(outItemDetailList == null || outItemDetailList.size() != outItemDetailIds.size()){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "进项票关联的销项票已生成开票清单，无法打回该进项票！");
		}
		// 获取所有detailId
		List<Long> detailIds = outItemDetailList.stream().map(a -> a.getApplyDetailId()).distinct()
				.collect(Collectors.toList());

		// 查询所有detail
		List<InvoiceOutApplyDetail> detailList = invoiceOutApplyDetailDao.queryInvoiceOutApplyDetails(detailIds,
				invoiceId);

		// 逻辑删除
		List<Long> itemIds = outItemDetailList.stream().map(a -> a.getId()).distinct().collect(Collectors.toList());
		if (itemIds != null && itemIds.size() > 0) {
			invoiceOutItemDetailDao.deleteByIds(itemIds, user.getName());
		}

		// 扣减detail中的金额
		List<Long> needDeleteIds = new LinkedList<>();
		detailIds.forEach(id -> {
			double decreaseAmount = outItemDetailList.stream().filter(a -> a.getApplyDetailId().equals(id))
					.mapToDouble(a -> a.getAmount().doubleValue()).sum();
			InvoiceOutApplyDetail detail = detailList.stream().filter(a -> a.getId().equals(id)).findFirst().get();
			if (detail.getApplyAmount().doubleValue() > decreaseAmount) {
				invoiceOutApplyDetailDao.decreaseApplyAmount(decreaseAmount, id, user.getName());
				detail.setApplyAmount(BigDecimal.valueOf(decreaseAmount));
			} else {
				needDeleteIds.add(id);
			}
		});
		if (needDeleteIds.size() > 0) {
			invoiceOutApplyDetailDao.batchDelete(needDeleteIds, user.getName());
		}

		// 获取主表id集合
		List<Long> appyIds = detailList.stream().map(a -> a.getInvoiceOutApplyId()).distinct()
				.collect(Collectors.toList());
		for (Long id : appyIds) {
			double amount = detailList.stream().filter(a -> a.getInvoiceOutApplyId().equals(id))
					.mapToDouble(a -> a.getApplyAmount().doubleValue()).sum();
			if(amount == 0) {
				invoiceOutApplyDao.deleteForId(id, user.getName(),null);
			}else {
				invoiceOutApplyDao.decreaseApplyAmount(amount, id, user.getName());
			}

			// 如果是订单的开票金额与吨位为0，那么订单的状态需要回退
			List<Long> orderIds = invoiceOutApplyDao.selectOrderIdsByApplyId(id);
			if (orderIds != null && orderIds.size() > 0) {
				updateOrderStatus(orderIds, user, new Date(), true);
			}
		}
	}

	/**
	 * 查询可申请开票详情所有数据(附带关联关系)
	 * 
	 * @param outApplyId 申请开票ID
	 * 
	 * @return List<InvoiceOutApplyDetailVoDto>
	 * 
	 * @author DQ
	 */
	@Override
	public List<InvoiceOutApplyDetailVoDto> queryDetail(Long orgId, Long outApplyId) {
		List<InvoiceApplicationDto> list = null;
		if(outApplyId != null && outApplyId > 0){
			list = poolOutDao.queryInvoiceApplicationDtoByApplyId(outApplyId);
		}else{
			list = poolOutDao.queryInvoiceApplicationDtoByOrgId(orgId);
		}
		return setFirstLayerList(orgId, outApplyId, list);
	}
	
	// 设置业务员申请开票数据集合
	private List<InvoiceOutApplyDetailVoDto> setFirstLayerList(Long orgId, Long outApplyId, List<InvoiceApplicationDto> list) {
		List<InvoiceOutApplyDetailVoDto> detailVoList = new ArrayList<InvoiceOutApplyDetailVoDto>();
		InvoiceOutApplyDetailVoDto detailVoDto = null;
		InvoiceOutApplyDetailListVoDto detailListVoDto = null;
		Long id = -1l;
		for (int i = 0; i < list.size(); i++) {	
			InvoiceApplicationDto applDto = list.get(i);
			if(!id.equals(applDto.getOwnerId())) {
				// 设置并添加业务员申请开票数据
				detailVoDto = setFirstLayer(applDto, outApplyId);
				detailVoList.add(detailVoDto);
			}
			// 设置并添加买家申请开票数据
			List<InvoiceOutApplyDetailListVoDto> detailListVoDtos = setSecondLayer(applDto, outApplyId);
			if(null != detailListVoDtos && !detailListVoDtos.isEmpty()) {
				detailVoDto.addAllDetail(detailListVoDtos);
			}
			id = list.get(i).getOwnerId();
		}
		setFirstLayerListCount(detailVoList);
		return detailVoList;
	}
	
	// 设置业务员申请开票数据合计
	private void setFirstLayerListCount(List<InvoiceOutApplyDetailVoDto> detailVoList) {
		Iterator<InvoiceOutApplyDetailVoDto> iter = detailVoList.iterator();
		while(iter.hasNext()) {
			InvoiceOutApplyDetailVoDto detailVoDto = iter.next();
			if(null == detailVoDto.getDetailList() || detailVoDto.getDetailList().size() < 1) {
				iter.remove();
			}else {
				// 累计已申请开票金额
				detailVoDto.setTotalAmount(BigDecimal.valueOf(
						detailVoDto.getDetailList().stream().mapToDouble(a -> a.getAmount().doubleValue()).sum()));
				// 累计未开票金额
				detailVoDto.setTotalUnAmount(BigDecimal.valueOf(
						detailVoDto.getDetailList().stream().mapToDouble(a -> a.getUnAmount().doubleValue()).sum()));
				// 累计未开票重量
				detailVoDto.setTotalUnWeight(BigDecimal.valueOf(
						detailVoDto.getDetailList().stream().mapToDouble(a -> a.getUnWeight().doubleValue()).sum()));
			}
		}
	}

	// 设置业务员申请开票数据
	private InvoiceOutApplyDetailVoDto setFirstLayer(InvoiceApplicationDto applDto,
			Long outApplyId) {
		InvoiceOutApplyDetailVoDto detailVoDto = new InvoiceOutApplyDetailVoDto();
		// 服务中心
		detailVoDto.setOrgId(applDto.getOrgId());
		detailVoDto.setOrgName(applDto.getOrgName());
		// 业务员
		detailVoDto.setOwnerId(applDto.getOwnerId());
		detailVoDto.setOwnerName(applDto.getOwnerName());
		return detailVoDto;
	}

	// 设置买家申请开票数据
	private List<InvoiceOutApplyDetailListVoDto> setSecondLayer(InvoiceApplicationDto applDto,
			Long outApplyId) {

		// 查询申请开票详情数据
		List<InvoiceApplicationDetailDto> list = queryInvoiceApplicationDetailDtoByBuyerId(applDto.getDepartmentId(), applDto.getOwnerId(), outApplyId);
		if(null == list || list.size() < 1) {
			return null;
		}
		
		//处理打印凭证
		List<List<InvoiceApplicationDetailDto>> results = processCredential(list);
		
		List<InvoiceOutApplyDetailListVoDto> invoiceOutApplyDetailListVoDtos = new ArrayList<InvoiceOutApplyDetailListVoDto>();
		for (List<InvoiceApplicationDetailDto> mylist : results) {
			
			InvoiceOutApplyDetailListVoDto detailListVoDto = new InvoiceOutApplyDetailListVoDto();
			
			if(mylist.size()==0)
				continue;
			
			InvoiceApplicationDetailDto dto = mylist.get(0);
			
			detailListVoDto.setBuyerCredential(setCredentialMessage(dto, dto.getBuyerCheck()));
			detailListVoDto.setSellerCredential(setCredentialMessage(dto, dto.getSellerCheck()));
			
			// 买家ID
			detailListVoDto.setBuyerId(applDto.getBuyerId());
			// 买家名字
			detailListVoDto.setBuyerName(applDto.getBuyerName());
			//部门ID
			detailListVoDto.setDepartmentId(applDto.getDepartmentId());
			//部门名称
			detailListVoDto.setDepartmentName(applDto.getDepartmentName());
			// 二次结算应收金额
			if (applDto.getBalanceSecondSettlement() == null || applDto.getBalanceSecondSettlement().doubleValue() >= 0) {
				detailListVoDto.setBalanceSecondSettlement(BigDecimal.ZERO);
			} else {
				detailListVoDto.setBalanceSecondSettlement(applDto.getBalanceSecondSettlement().abs());
			}
			// 开票资料是否审核通过; 0/否、1/是
			if (null != applDto.getInvoiceDataStatus()
					&& InvoiceDataStatus.Approved.getCode().equals(applDto.getInvoiceDataStatus())) {
				detailListVoDto.setIsOkId(DistributionStatus.Yes.getCode());
				detailListVoDto.setIsOkName(DistributionStatus.Yes.getName());
			} else {
				detailListVoDto.setIsOkId(DistributionStatus.No.getCode());
				detailListVoDto.setIsOkName(DistributionStatus.No.getName());
			}
			// 开票资料是否审核通过
			if(null != detailListVoDto.getIsOkId() && InvoiceDataStatus.Approved.getCode().equals(detailListVoDto.getIsOkId())) {
				// 本次自动分配金额  大于或等于 本次申请开票金额 
		      	if(detailListVoDto.getAmount().compareTo(detailListVoDto.getAutomaticAmount()) != 1) {
		      		// 已分配
		      		detailListVoDto.setIsAllotId(DistributionStatus.Lready.getCode());
		      		detailListVoDto.setIsAllotName(DistributionStatus.Lready.getName());
		      	}else {
		      		// 未分配
		      		detailListVoDto.setIsAllotId(DistributionStatus.Sure.getCode());
		      		detailListVoDto.setIsAllotName(DistributionStatus.Sure.getName());
		      	}
	      	}
			
			// 未开发票超期月数
			Date orderDate =  new Date(mylist.stream().mapToLong(
					a -> a.getCreated().getTime()).min().getAsLong());
			detailListVoDto.setCountMonth(Tools.monthsDiff(orderDate, new Date()));
			// 设置买家 - 申请开票详情数据集合
			List<InvoiceOutApplyDetailItemsVoDto> itemsList = getThirdLayerList(mylist);
			detailListVoDto.setItemsList(itemsList);
			
			// 累计已申请开票金额
			detailListVoDto.setAmount(BigDecimal.valueOf(
					itemsList.stream().mapToDouble(a -> a.getAmount().doubleValue()).sum()));
			// 累计未开票金额
			detailListVoDto.setUnAmount(BigDecimal.valueOf(
					itemsList.stream().mapToDouble(a -> a.getUnAmount().doubleValue()).sum()));
			// 累计未开票重量
			detailListVoDto.setUnWeight(BigDecimal.valueOf(
					itemsList.stream().mapToDouble(a -> a.getUnWeight().doubleValue()).sum()));
		
			invoiceOutApplyDetailListVoDtos.add(detailListVoDto);
		}
		
		return invoiceOutApplyDetailListVoDtos;
	}

	/**
	 * 处理打印凭证：按审核是不是通过分组：通过审核为一组，不通过审核为一组
	 * @param list List<InvoiceApplicationDetailDto> list： 同一个买家的订单
	 * @return
	 */
	private List<List<InvoiceApplicationDetailDto>> processCredential(List<InvoiceApplicationDetailDto> list) {
		
		List<List<InvoiceApplicationDetailDto>> result = new ArrayList<>();
		
		/*
		SysSetting controlInvoice = sysSettingService.queryByType("ControlPinSettings");
		String controlInvoiceCheck = controlInvoice.getSettingValue();
		//0,不开启， 则在前台就不显示。
		if("0".equals(controlInvoiceCheck)){
			list.forEach(item -> {
				item.setBuyerCheck(1);
				item.setSellerCheck(1);
			});
			result.add(list);
			return result;
		}
		*/
		
		List<InvoiceApplicationDetailDto> list1 = new ArrayList<InvoiceApplicationDetailDto>();
		List<InvoiceApplicationDetailDto> list2 = new ArrayList<InvoiceApplicationDetailDto>();
		
		for (InvoiceApplicationDetailDto dto : list) {
			//List<InvoiceApplicationDetailDto> dtos = checkMaps()
			
			/***
			 * 检查买家凭证：
			 * BuyerCheckValue：1：需要审核， 0：不需要审核
			 * 当BuyerCheckValue为1的时候：BuyerCredentialStatus和BatchBuyerCredentialStatus其中一个为APPROVED时候，表示审核通过
			 * 当BuyerCheckValue为1的时候：BuyerCredentialCode和BatchBuyerCredentialCode为空的时候，表示审核不通过
			 */
			int buyerCheck = 0, sellerCheck = 0;
			if("1".equals(dto.getBuyerCheckValue())){ //为1表示，需要审核
				
				if(dto.getBuyerCredentialCode()!=null || dto.getBatchBuyerCredentialCode()!=null){ //单个凭证号状态和批量凭证号状态有一不为空
					if("APPROVED".equals(dto.getBuyerCredentialStatus()) 
							|| "APPROVED".equals(dto.getBatchBuyerCredentialStatus())){ //单个凭证号状态和批量凭证号状态有一个为APPROVED，则审核通过
						buyerCheck = 1;
					}else{
						buyerCheck = 0;
					}
				}else{
					buyerCheck = 0;
				}
				
			}else{ //BuyerCheckValue: 不为：1
				buyerCheck = 1;
			}
			
			if("1".equals(dto.getSellerCheckValue())){ 		//为1表示，需要审核
				
				if(dto.getSellerCredentialCode()!=null || dto.getBatchSellerCredentialCode()!=null){ //单个凭证号状态和批量凭证号状态有一不为空
					if("APPROVED".equals(dto.getSellerCredentialStatus()) 
							|| "APPROVED".equals(dto.getBatchSellerCredentialStatus())){ //单个凭证号状态和批量凭证号状态有一个为APPROVED，则审核通过
								sellerCheck = 1;
					}else{
						sellerCheck = 0;
					}
				}else{
					sellerCheck = 0;
				}
			}else{ //SellerCheckValue: 不为：1
				sellerCheck = 1;
			}
			
			dto.setBuyerCheck(buyerCheck);
			dto.setSellerCheck(sellerCheck);
			if(sellerCheck==1 && buyerCheck==1){
				list1.add(dto);
			}else 
				list2.add(dto);
		}
		
		//添加全部审核通过的
		if(list1.size()>0){
			list1.forEach(item -> {
				item.setBuyerCheck(1);
				item.setSellerCheck(1);
			});
			result.add(list1);
		}
		
		int buyerRes = 1, sellerRes = 1;
		for (InvoiceApplicationDetailDto dto : list2) {
			if(dto.getBuyerCheck()==0){
				buyerRes = 0;
			}
			if(dto.getSellerCheck()==0){
				sellerRes = 0;
			}
		}
		
		for (InvoiceApplicationDetailDto dto : list2) {
			dto.setSellerCheck(sellerRes);
			dto.setBuyerCheck(buyerRes);
		}
		
		//添加全部审核不通过的
		if(list2.size()>0){
			result.add(list2);
		}
		
		return result;
	}
	
	private String setCredentialMessage(InvoiceApplicationDetailDto list, int checkValue){
		String checkMessage = ""; 
		if(checkValue==-1){
			checkMessage = "不需要审核";
		}else if(checkValue == 0){
			checkMessage = "否";
		}else if(checkValue == 1){
			checkMessage = "是";
		}
		return checkMessage;
	}

	// 查询申请开票详情数据集合
	private List<InvoiceApplicationDetailDto> queryInvoiceApplicationDetailDtoByBuyerId(Long departmentId, Long ownerId, Long outApplyId) {
		ConsignOrderItemsQuery consignOrderItemsQuery = new ConsignOrderItemsQuery();
		consignOrderItemsQuery.setOutApplyId(outApplyId);
		consignOrderItemsQuery.setOwnerId(ownerId);
//		consignOrderItemsQuery.setBuyerId(buyerId);
		consignOrderItemsQuery.setDepartmentId(departmentId);
		consignOrderItemsQuery.setConsignOrderStatus(Arrays.asList(
				ConsignOrderStatus.INVOICEREQUEST.getCode(),ConsignOrderStatus.INVOICE.getCode()));
		consignOrderItemsQuery.setRelationStatus(InvoiceInDetailStatus.HasRelation.getCode());
		consignOrderItemsQuery.setInvoiceInStatus(Arrays.asList(
				InvoiceInStatus.RECEIVED.getCode(),InvoiceInStatus.SENT.getCode(),
				InvoiceInStatus.WAIT.getCode(),InvoiceInStatus.ALREADY.getCode()));
		consignOrderItemsQuery.setInvoiceOutApplyStatus(InvoiceOutApplyStatus.getApplyExcludeStatus());
		return consignOrderItemsDao.queryInvoiceApplicationDetailDtoByBuyerId(consignOrderItemsQuery);
	}
	
	// 设置买家 - 申请开票详情数据集合
	private List<InvoiceOutApplyDetailItemsVoDto> getThirdLayerList(List<InvoiceApplicationDetailDto> list) {
		List<InvoiceOutApplyDetailItemsVoDto> itemsList = new ArrayList<InvoiceOutApplyDetailItemsVoDto>(); 
		InvoiceOutApplyDetailItemsVoDto detailItemsVoDto = null;
		InvoiceOutApplyDetailItemsListVoDto detailItemsListVoDto = null;
		Long id = -1l;
		
		for (int i = 0; i < list.size(); i++) {
			InvoiceApplicationDetailDto applDetailDto = list.get(i);
			if(!id.equals(applDetailDto.getOrderId())) {
				// 申请开票详情数据 - 设置并添加订单数据
				detailItemsVoDto = getThirdLayer(applDetailDto);
				itemsList.add(detailItemsVoDto);
			}
			// 申请开票详情数据 - 订单数据 - 设置并添加销项数据
			detailItemsListVoDto = setFourthLayer(applDetailDto);
			detailItemsVoDto.addDetail(detailItemsListVoDto);
			id = list.get(i).getOrderId();
		}
		
		getThirdLayerListCount(itemsList);
		return itemsList;
	}

	// 设置申请开票详情数据 - 订单数据合计
	private void getThirdLayerListCount(List<InvoiceOutApplyDetailItemsVoDto> itemsList) {
		for(InvoiceOutApplyDetailItemsVoDto detailItemsVoDto : itemsList) {
			// 累计已申请开票金额
			detailItemsVoDto.setAmount(BigDecimal.valueOf(
					detailItemsVoDto.getDetailList().stream().mapToDouble(a -> a.getAmount().doubleValue()).sum()));
			// 根据订单详情ID去重复 
			List<Long> orderItems = detailItemsVoDto.getDetailList().stream().map(a -> a.getOrderDetailId()).distinct().collect(Collectors.toList());
			// 未开票金额
			BigDecimal unAmount = BigDecimal.ZERO;
			// 未开票重量
			BigDecimal unWeight = BigDecimal.ZERO;
			// 实提总金额
			BigDecimal totalActualAmount = BigDecimal.ZERO;
			// 实提总重量
			BigDecimal totalActualWeight = BigDecimal.ZERO;
			// 合同总金额
			BigDecimal totalContractAmount = BigDecimal.ZERO;
			for(Long ordId : orderItems) {
				InvoiceOutApplyDetailItemsListVoDto detailItem = detailItemsVoDto.getDetailList().stream().filter(a -> a.getOrderDetailId().equals(ordId)).findFirst().get();
				unAmount = unAmount.add(detailItem.getUnAmount());
				unWeight = unWeight.add(detailItem.getUnWeight());
				totalActualAmount = totalActualAmount.add(detailItem.getActualAmount());
				totalActualWeight = totalActualWeight.add(detailItem.getActualWeight());
				totalContractAmount = totalContractAmount.add(detailItem.getContractAmount());
			}
			detailItemsVoDto.setUnAmount(unAmount);
			detailItemsVoDto.setUnWeight(unWeight);
			detailItemsVoDto.setTotalActualAmount(totalActualAmount);
			detailItemsVoDto.setTotalActualWeight(totalActualWeight);
			detailItemsVoDto.setTotalContractAmount(totalContractAmount);
			
		}
	}

	// 设置申请开票详情数据 - 订单数据
	private InvoiceOutApplyDetailItemsVoDto getThirdLayer(InvoiceApplicationDetailDto applDetailDto) {
		InvoiceOutApplyDetailItemsVoDto detailItemsVoDto = new InvoiceOutApplyDetailItemsVoDto();
		// 交易单号
		detailItemsVoDto.setOrderId(applDetailDto.getOrderId());
		detailItemsVoDto.setOrderCode(applDetailDto.getOrderCode());
		// 开单时间
		detailItemsVoDto.setCreated(String.valueOf(Tools.dateToStr(applDetailDto.getCreated())));
		return detailItemsVoDto;
	}
	
	// 设置申请开票详情数据 - 订单数据 - 销项数据
	private InvoiceOutApplyDetailItemsListVoDto setFourthLayer(
			InvoiceApplicationDetailDto applDetailDto) {
		// 是否有折让过金额或重量
		if(applDetailDto.getAllowanceBuyerAmount().doubleValue() != 0 || applDetailDto.getAllowanceWeight().doubleValue() != 0) {
			// 实提重量 减去开过进项票重量 是否等于此次申请开票重量
			if(applDetailDto.getActualWeight().add(applDetailDto.getAllowanceWeight()).subtract(applDetailDto.getUsedWeight()).doubleValue()
					!= applDetailDto.getWeight().doubleValue()) {
				BigDecimal amount = applDetailDto.getActualAmount().add(applDetailDto.getAllowanceBuyerAmount());
				BigDecimal weight = applDetailDto.getActualWeight().add(applDetailDto.getAllowanceWeight());
				BigDecimal price = amount.divide(weight,10,BigDecimal.ROUND_HALF_DOWN);
				BigDecimal appAmount = applDetailDto.getWeight().multiply(price);
				applDetailDto.setAmount(appAmount);
				applDetailDto.setUnAmount(appAmount);
			}else {
				BigDecimal residualAmount = applDetailDto.getActualAmount().
						add(applDetailDto.getAllowanceBuyerAmount()).
						subtract(applDetailDto.getUsedAmount());
				applDetailDto.setAmount(residualAmount);
				applDetailDto.setUnAmount(residualAmount);
			}
		}
		
		InvoiceOutApplyDetailItemsListVoDto detailItemsListVoDto = new InvoiceOutApplyDetailItemsListVoDto();
		// 品名
		detailItemsListVoDto.setNsortName(applDetailDto.getNsortName());
		// 材质
		detailItemsListVoDto.setMaterial(applDetailDto.getMaterial());
		// 规格
		detailItemsListVoDto.setSpec(applDetailDto.getSpec());
		
		// 实提金额
		detailItemsListVoDto.setActualAmount(CbmsNumberUtil.buildMoney(
				CbmsNumberUtil.formatMoney(applDetailDto.getActualAmount())));
		// 实提重量
		detailItemsListVoDto.setActualWeight(CbmsNumberUtil.buildWeight(
				CbmsNumberUtil.formatWeight(applDetailDto.getActualWeight())));
		
		// 未开票金额
		detailItemsListVoDto.setUnAmount(CbmsNumberUtil.buildMoney(
				CbmsNumberUtil.formatMoney(applDetailDto.getUnAmount())));
		// 未开票重量
		detailItemsListVoDto.setUnWeight(CbmsNumberUtil.buildWeight(
				CbmsNumberUtil.formatWeight(applDetailDto.getUnWeight())));
		
		// 成交价
		detailItemsListVoDto.setDealPrice(CbmsNumberUtil.buildMoney(
				CbmsNumberUtil.formatMoney(applDetailDto.getDealPrice())));
		// 合同金额
		detailItemsListVoDto.setContractAmount(CbmsNumberUtil.buildMoney(
				CbmsNumberUtil.formatMoney(applDetailDto.getContractAmount())));
		
		// 订单详情ID
		detailItemsListVoDto.setOrderDetailId(applDetailDto.getOrderDetailId());
		// 进项票ID
		detailItemsListVoDto.setInvoiceInId(applDetailDto.getInvoiceInId());
		// 进项票详情ID
		detailItemsListVoDto.setInvoiceInDetailId(applDetailDto.getInvoiceInDetailId());
		// 订进项票详情与订单详情关联表ID
		detailItemsListVoDto.setInvoiceOrderitemId(applDetailDto.getInvoiceOrderitemId());
		
		// 重量
		detailItemsListVoDto.setWeight(CbmsNumberUtil.buildWeight(
				CbmsNumberUtil.formatWeight(applDetailDto.getWeight())));
		// 含税金额
		detailItemsListVoDto.setAmount(CbmsNumberUtil.buildMoney(
				CbmsNumberUtil.formatMoney(applDetailDto.getAmount())));
		// 不含税金额
		detailItemsListVoDto.setNoTaxAmount(CbmsNumberUtil.buildMoney(
				CbmsNumberUtil.formatMoney(detailItemsListVoDto.getAmount().
				divide(new BigDecimal(1.17),10,BigDecimal.ROUND_HALF_DOWN))));
		// 税额
		detailItemsListVoDto.setTaxAmount(CbmsNumberUtil.buildMoney(
				CbmsNumberUtil.formatMoney(detailItemsListVoDto.getAmount().
				subtract(detailItemsListVoDto.getNoTaxAmount()))));
		
		return detailItemsListVoDto;
	}

}
