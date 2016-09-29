package com.prcsteel.platform.smartmatch.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lts.core.commons.utils.DateUtils;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.DateUtil;
import com.prcsteel.platform.common.utils.StringUtil;
import com.prcsteel.platform.core.model.dto.BaseDataDto;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.persist.dao.CityDao;
import com.prcsteel.platform.core.service.BillSequenceService;
import com.prcsteel.platform.smartmatch.api.RestOrganizationService;
import com.prcsteel.platform.smartmatch.api.RestSendSmsService;
import com.prcsteel.platform.smartmatch.api.RestUserService;
import com.prcsteel.platform.smartmatch.model.dto.InquiryDetailDto;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderMessageQueue;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderDetailDto;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderDto;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderDtoForShow;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderItemsAttributeDto;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderItemsDto;
import com.prcsteel.platform.smartmatch.model.dto.RequirementStatusDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchResultItemsDto;
import com.prcsteel.platform.smartmatch.model.enums.PurchaseOrderStatus;
import com.prcsteel.platform.smartmatch.model.enums.QuotationOrderStatus;
import com.prcsteel.platform.smartmatch.model.model.InquiryDetailContact;
import com.prcsteel.platform.smartmatch.model.model.InquiryDetailData;
import com.prcsteel.platform.smartmatch.model.model.InquiryDetailRes;
import com.prcsteel.platform.smartmatch.model.model.PurchaseOrder;
import com.prcsteel.platform.smartmatch.model.model.PurchaseOrderItemAttributes;
import com.prcsteel.platform.smartmatch.model.model.PurchaseOrderItems;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrder;
import com.prcsteel.platform.smartmatch.model.query.PurchaseOrderItemsQuery;
import com.prcsteel.platform.smartmatch.model.query.PurchaseOrderQuery;
import com.prcsteel.platform.smartmatch.persist.dao.CreatePruchaseOrderDao;
import com.prcsteel.platform.smartmatch.persist.dao.PurchaseOrderDao;
import com.prcsteel.platform.smartmatch.persist.dao.PurchaseOrderItemAttributesDao;
import com.prcsteel.platform.smartmatch.persist.dao.PurchaseOrderItemsDao;
import com.prcsteel.platform.smartmatch.persist.dao.QuotationOrderDao;
import com.prcsteel.platform.smartmatch.service.PurchaseOrderService;
import com.prcsteel.platform.smartmatch.service.RequirementStatusService;
import com.prcsteel.platform.smartmatch.service.SearchService;

/**
 * Created by Rolyer on 2015/11/19.
 */
@Service("purchaseOrderService")
public class PurchaseOrderServiceImpl implements PurchaseOrderService, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

	@Resource
	private PurchaseOrderDao purchaseOrderDao;
	@Resource
	private PurchaseOrderItemsDao purchaseOrderItemsDao;
	@Resource
	private BillSequenceService billSequenceService;
	@Resource
	private PurchaseOrderItemAttributesDao attributesDao;
	@Resource
	private PurchaseOrderItemsDao itemsDao;
	@Resource
	private SearchService searchService;
	@Resource
	private QuotationOrderDao quotationOrderDao;
	@Resource
	private RequirementStatusService requirementStatusService;
	@Resource
	private CreatePruchaseOrderDao createPruchaseOrderDao;
	@Resource
	private CityDao cityDao;
	@Resource
	private UserDao userDao;
	
	@Resource
	OrganizationDao organizationDao;
	
	// @Resource
	private RestUserService restUserService = null;
	private RestSendSmsService restSendSmsService;

	// spring上下文
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 获取用户的REST接口
	 *
	 * @return
	 */
	private RestUserService getRestUserService() {
		if (restUserService == null) {
			restUserService = (RestUserService) this.getRestService("restUserService");
		}
		return restUserService;
	}

	/**
	 * 使用spring上下文获取Rest的SpringBEAN
	 *
	 * @param serviceName
	 * @return
	 */
	private Object getRestService(String serviceName) {
		Object bean = applicationContext.getBean(serviceName);
		if (bean != null) {
			return bean;
		} else {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "获取不到REST服务：" + serviceName);
		}
	}

	/**
	 * 获取短信的bean
	 */
	private void getRestSendSmsServiceBean() {
		if (restSendSmsService == null) {
			Object beanObj = this.applicationContext.getBean("restSendSmsService");
			if (beanObj != null) {
				restSendSmsService = (RestSendSmsService) beanObj;
			}
		}
	}

	/**
	 * 保存采购单或者修改采购单
	 *
	 * @param purchaseOrder
	 * @param dtos
	 *            表体资源信息等
	 * @param user
	 *            操作人
	 * @return
	 */
	@Override
	@Transactional
	public PurchaseOrder save(PurchaseOrder purchaseOrder, List<PurchaseOrderItemsDto> dtos, User user) {
		Date now = new Date();
		purchaseOrder.setOrgId(user.getOrgId());
		purchaseOrder.setOwnerId(user.getId());
		purchaseOrder.setLastUpdatedBy(user.getLoginId());
		purchaseOrder.setLastUpdated(now);
		Long purchaseOrderId = purchaseOrder.getId();
		// 统计采购重量
		final BigDecimal tatolWeight = BigDecimal
				.valueOf(dtos.stream().mapToDouble(a -> a.getWeight().doubleValue()).sum());
		purchaseOrder.setTotalWeight(tatolWeight);
		List<PurchaseOrderItems> items = new LinkedList<>();
		if (purchaseOrderId != null && purchaseOrderId > 0) { // 如果有id说明是对原采购单的更新操作

			/*** 添加当前用户权限控制 add by peanut on 15-12-21 start ***/
			PurchaseOrder old = purchaseOrderDao.selectByPrimaryKey(purchaseOrderId);
			// 数据库里的用户id集
			String userIds = old.getUserIds();
			// 当前操作的用户
			String userId = user.getId().toString();
			// 不包含当前用户则添加
			if (!StringUtils.isEmpty(userIds)) {
				if (!Arrays.asList(userIds.split(",")).contains(userId)) {
					userIds += "," + userId;
				}
			}else {
				userIds = userId.toString();
			}
			purchaseOrder.setUserIds(userIds);
			/******* end ********/

			if (purchaseOrderDao.updateByPrimaryKeySelective(purchaseOrder) != 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新表头失败");
			}
			attributesDao.deleteByPurchaseOrderId(purchaseOrderId); // 先删除最底层扩展属性表
			// 详情表和属性表需要判断是否做更新还是删除
			items = getItemList(purchaseOrderId);
			List<Long> ids = items.stream().map(a -> a.getId()).collect(Collectors.toList());
			for (PurchaseOrderItemsDto dto : dtos) {
				// 品规和钢厂需要是包含关系
				CheckItemToSaveOrUpdate check = new CheckItemToSaveOrUpdate(dto);
				Optional op = items.stream().filter(a -> {
					CheckItemToSaveOrUpdate origin = new CheckItemToSaveOrUpdate(a);
					return (check.categoryUuid.equals(origin.categoryUuid)
							&& check.materialUuid.equals(origin.materialUuid)
							&& check.factoryIds.containsAll(origin.factoryIds)
							&& check.spec1List.containsAll(origin.spec1List) && check.minSpec2 <= origin.minSpec2
							&& check.maxSpec2 >= origin.maxSpec2 && check.minSpec3 <= origin.minSpec3
							&& check.maxSpec3 >= origin.maxSpec3);
				}).findFirst();
				if (op.isPresent()) {
					// update
					PurchaseOrderItems needToUpdate = (PurchaseOrderItems) op.get();
					ids.remove(needToUpdate.getId()); // 如果要做更新就不删除了
					dto.setOption(Constant.EDIT);
					dto.setOriginId(needToUpdate.getId());
				} else {
					dto.setOption(Constant.ADD);
				}
			}
			if (ids != null && !ids.isEmpty()) {
				itemsDao.deleteByIds(ids); // 再删除详情表，如果要做更新要不做删除了
			}
		} else { // 新建的采购单没有id执行insert操作

			purchaseOrder.setCode("temp"); // 先使用temp代替code
			purchaseOrder.setCreated(now); // 为了返回到页面显示所有手工设置创建时间
			purchaseOrder.setCreatedBy(user.getLoginId());
			purchaseOrder.setStatus(PurchaseOrderStatus.PENDING_QUOTE.getCode());
			/** 添加当前用户权限控制 add by peanut on 15-12-21 start **/
			purchaseOrder.setUserIds(user.getId().toString());
			/******* end ********/

			if (purchaseOrderDao.insertSelective(purchaseOrder) != 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存表头失败");
			}
			purchaseOrderId = purchaseOrder.getId();

			/**
			 * 通过生成的id生成code
			 */
			Long minId = purchaseOrderDao.selectMinIdByDate(new SimpleDateFormat("yyyy-MM-dd").format(now));
			String code = billSequenceService.generatePurchaseOrderCode(now, minId, purchaseOrderId);
			purchaseOrderDao.updateCodeById(purchaseOrderId, code);
		}

		List<PurchaseOrderItemAttributes> attributesList = new LinkedList<>();
		// 保存表体
		for (PurchaseOrderItemsDto itemDto : dtos) {
			if (Constant.EDIT.equals(itemDto.getOption())) {
				itemDto.setId(itemDto.getOriginId()); // 给一个id用于attribute存itemsId
				PurchaseOrderItems needToUpdate = items.stream().filter(a -> a.getId().equals(itemDto.getOriginId()))
						.findFirst().get();
				needToUpdate.setCategoryUuid(itemDto.getCategoryUuid());
				needToUpdate.setCategoryName(itemDto.getCategoryName());
				needToUpdate.setMaterialUuid(itemDto.getMaterialUuid());
				needToUpdate.setMaterialName(itemDto.getMaterialName());
				needToUpdate.setSpec1(itemDto.getSpec1());
				needToUpdate.setSpec2(itemDto.getSpec2());
				needToUpdate.setSpec3(itemDto.getSpec3());
				needToUpdate.setFactoryIds(itemDto.getFactoryIds());
				needToUpdate.setFactoryNames(itemDto.getFactoryNames());
				needToUpdate.setWeight(itemDto.getWeight());
				needToUpdate.setQuantity(itemDto.getQuantity());
				needToUpdate.setLastUpdatedBy(user.getLoginId());
				needToUpdate.setLastUpdated(now);
				if (itemsDao.updateByPrimaryKeySelective(needToUpdate) != 1) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新详情失败");
				}
			} else {
				itemDto.setPurchaseOrderId(purchaseOrderId);
				itemDto.setCreated(now);
				itemDto.setCreatedBy(user.getLoginId());
				itemDto.setLastUpdatedBy(user.getLoginId());
				itemDto.setLastUpdated(now);
				if (itemsDao.insertSelective(itemDto) != 1) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存详情失败");
				}
			}
			Long itemsId = itemDto.getId();
			for (PurchaseOrderItemAttributes attributes : itemDto.getAttributeList()) {
				if (StringUtils.isNotBlank(attributes.getValue())) {
					attributes.setPurchaseOrderItemId(itemsId);
					attributes.setCreated(now);
					attributes.setCreatedBy(user.getLoginId());
					attributesList.add(attributes);
				}
			}
		}

		if (attributesList != null && !attributesList.isEmpty()) {
			int num = attributesDao.batchInsert(attributesList);
			if (num != attributesList.size()) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存扩展属性失败");
			}
		}
		return purchaseOrder;
	}

	/**
	 * 用于封装purchaseOrderItems，判断items是更新还是
	 */
	private class CheckItemToSaveOrUpdate {
		Long itemsId;
		Double minSpec2;
		Double maxSpec2;
		Double minSpec3;
		Double maxSpec3;
		List factoryIds;
		List spec1List;
		String categoryUuid;
		String materialUuid;

		CheckItemToSaveOrUpdate(PurchaseOrderItems items) {
			// 品规和钢厂需要是包含关系
			itemsId = items.getId();
			categoryUuid = items.getCategoryUuid();
			materialUuid = items.getMaterialUuid();
			factoryIds = items.getFactoryIds() == null ? new ArrayList<>()
					: Arrays.asList(items.getFactoryIds().split(","));
			spec1List = Arrays.asList(items.getSpec1().split(","));
			if (items.getSpec2() == null || "".equals(items.getSpec2())) {
				minSpec2 = 0d;
				maxSpec2 = Double.MAX_VALUE;
			} else {
				List<String> spec2 = Arrays.asList(items.getSpec2().split("-"));
				if (spec2.size() > 1) {
					minSpec2 = StringUtils.isNotBlank(spec2.get(0)) && StringUtils.isNumeric(spec2.get(0))
							? Double.valueOf(spec2.get(0)) : 0;
					maxSpec2 = StringUtils.isNotBlank(spec2.get(1)) && StringUtils.isNumeric(spec2.get(1))
							? Double.valueOf(spec2.get(1)) : Double.MAX_VALUE;
				} else {
					minSpec2 = StringUtils.isNotBlank(spec2.get(0)) && StringUtils.isNumeric(spec2.get(0))
							? Double.valueOf(spec2.get(0)) : 0;
					maxSpec2 = StringUtils.isNotBlank(spec2.get(0)) && StringUtils.isNumeric(spec2.get(0))
							? Double.valueOf(spec2.get(0)) : Double.MAX_VALUE;
				}
			}
			if (items.getSpec3() == null || "".equals(items.getSpec3())) {
				minSpec3 = 0d;
				maxSpec3 = Double.MAX_VALUE;
			} else {
				List<String> spec3 = Arrays.asList(items.getSpec3().split("-"));
				if (spec3.size() > 1) {
					minSpec3 = StringUtils.isNotBlank(spec3.get(0)) && StringUtils.isNumeric(spec3.get(0))
							? Double.valueOf(spec3.get(0)) : 0;
					maxSpec3 = StringUtils.isNotBlank(spec3.get(1)) && StringUtils.isNumeric(spec3.get(1))
							? Double.valueOf(spec3.get(1)) : Double.MAX_VALUE;
				} else {
					minSpec3 = StringUtils.isNotBlank(spec3.get(0)) && StringUtils.isNumeric(spec3.get(0))
							? Double.valueOf(spec3.get(0)) : 0;
					maxSpec3 = StringUtils.isNotBlank(spec3.get(0)) && StringUtils.isNumeric(spec3.get(0))
							? Double.valueOf(spec3.get(0)) : Double.MAX_VALUE;
				}
			}
		}
	}

	@Override
	public PurchaseOrder queryById(Long id) {
		return purchaseOrderDao.selectByPrimaryKey(id);
	}

	@Override
	public PurchaseOrderDtoForShow queryByIdForShow(Long id) {
		return purchaseOrderDao.queryByIdForShow(id);
	}

	@Override
	public List<BaseDataDto> getAllAccount() {
		return purchaseOrderDao.getAllAccount();
	}

	@Override
	public List<AccountContact> queryAccountContact(String tel, String name) {
		return purchaseOrderDao.queryAccountContact(tel, name);
	}

	@Override
	public List<Account> queryAccount(String tel, String name) {
		return purchaseOrderDao.queryAccount(tel, name);
	}

	@Override
	public List<PurchaseOrderDto> selectPurchaseList(PurchaseOrderQuery purchaseOrderQuery) {
		return purchaseOrderDao.selectPurchaseList(purchaseOrderQuery);
	}

	@Override
	public int countPurchaseOrder(PurchaseOrderQuery purchaseOrderQuery) {
		return purchaseOrderDao.countPurchaseOrder(purchaseOrderQuery);
	}

	/**
	 * (非 Javadoc)
	 *
	 * @param purchaseOrderId
	 * @return
	 * @see PurchaseOrderService#getItemList(java.lang.Long)
	 **/
	@Override
	public List<PurchaseOrderItems> getItemList(Long purchaseOrderId) {
		return purchaseOrderItemsDao.getListByPOId(purchaseOrderId);
	}

	@Override
	public List<PurchaseOrderDto> getListByPurchaseOrderId(Long id) {
		return purchaseOrderItemsDao.getListByPurchaseOrderId(id);
	}

	@Override
	public PurchaseOrderDetailDto retrievePurchaseOrderById(Long purchaseOrderId) {
		// 获取并组装采购单明细
		PurchaseOrderDetailDto detail = new PurchaseOrderDetailDto();

		// 1. 获取采购单主表信息。
		PurchaseOrderDto purchaseOrderDto = purchaseOrderDao.queryByPurchaseOrderId(purchaseOrderId);
		// 2. 获取采购明细列表已经每条明细所包含的扩展属性。
		List<PurchaseOrderItemsDto> items = purchaseOrderItemsDao.getItemsByPurchaseOrderId(purchaseOrderId);
		items.stream().forEach(a -> a.setAttributeList(
				attributesDao.getAttributesByPurchaseOrderItemIdAndCategoryUuid(a.getCategoryUuid(), a.getId())));
		detail.setPurchaseOrderItems(items);
		// 3. 获取采购单对应的询价记录（已保存的询价信息）。
		// 4. 重新匹配信息询价。
		detail.setPurchaseOrder(purchaseOrderDto);

		return detail;
	}

	/**
	 * 更新指定采购单状态
	 *
	 * @param id
	 *            采购单ID
	 * @param status
	 *            状态值
	 * @param loginId
	 *            操作人，即：当前登录人
	 * @return
	 */
	public int updateStatusById(Long id, String status, String loginId) {
		return purchaseOrderDao.updateStatusById(id, status, loginId);
	}

	@Override
	public List<PurchaseOrderItemsDto> getHistoryPurchaseOrderItemsByTelOrAccount(PurchaseOrderItemsQuery query) {
		List<PurchaseOrderItemsDto> items = purchaseOrderItemsDao.getItemsByTelOrAccountName(query);
		items.stream().forEach(a -> a.setAttributeList(
				attributesDao.getAttributesByPurchaseOrderItemIdAndCategoryUuid(a.getCategoryUuid(), a.getId())));
		return items;
	}

	/**
	 * 激活或关闭采购单
	 *
	 * @param id
	 */
	@Override
	public void closeOrder(Long id, String operation, String reason, User user) {
		PurchaseOrder order = purchaseOrderDao.selectByPrimaryKey(id);
		List<String> statusList = new ArrayList<String>();
		statusList.add(PurchaseOrderStatus.QUOTED.getCode());
		statusList.add(PurchaseOrderStatus.PENDING_QUOTE.getCode());
		statusList.add(PurchaseOrderStatus.PENDING_DIRECTOR_ASSIGNED.getCode());
		statusList.add(PurchaseOrderStatus.PENDING_SERVER_MANAGER_ASSIGNED.getCode());
		statusList.add(PurchaseOrderStatus.PENDING_ACCEPTE.getCode());
		statusList.add(PurchaseOrderStatus.PENDING_CLERK_ACCEPTE.getCode());
		if (operation.equals(Constant.OPERATION_ACTIVE)
				&& order.getStatus().equals(PurchaseOrderStatus.CLOSED.getCode())) { // 激活订单且订单状态是关闭
			order.setStatus(order.getOriginStatus()); // 设置回关闭前的状态
		} else if (operation.equals(Constant.OPERATION_CLOSE) && (statusList.contains(order.getStatus()))) { // 关闭订单且订单状态是已报价或待报价
			order.setOriginStatus(order.getStatus()); // 把关闭前状态设置成原状态
			order.setStatus(PurchaseOrderStatus.CLOSED.toString()); // 设置新状态
		} else {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "订单状态不正确");
		}
		order.setLastUpdatedBy(user.getLoginId());
		order.setCloseReason(reason); // 关闭理由

		if (purchaseOrderDao.updateByPrimaryKeySelective(order) != 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "订单状态状态失败");
		}

		// 有需求单号并且关闭操作--添加更新需求单至mq
		if (StringUtils.isNotBlank(order.getRequirementCode())
				&& PurchaseOrderStatus.CLOSED.getCode().equals(order.getStatus())) {

			RequirementStatusDto requirementStatusDto = new RequirementStatusDto();
			requirementStatusDto.setStatusTo(PurchaseOrderStatus.CLOSED.getCode());
			requirementStatusDto.setCloseReason(reason);
			requirementStatusDto.setOperated(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			requirementStatusDto.setCode(order.getRequirementCode());

			// 查询出采购单对应的最新一条已报价并且已推送过的报价单id
			Long lastQuotationOrderId = quotationOrderDao
					.queryTheLatestQuotationOrderIdByPurchaseOrderId(order.getId());
			// 单子关闭前的状态如果是已报价，且已推送给最新的报价单id
			if (StringUtils.isNotBlank(order.getOriginStatus())
					&& PurchaseOrderStatus.QUOTED.getCode().equals(order.getOriginStatus())
					&& lastQuotationOrderId != null) {

				requirementStatusDto.setOperateCode(String.valueOf(lastQuotationOrderId));

			} else { // 给分捡过来的分捡单号
				requirementStatusDto.setOperateCode(order.getExt1());
			}

			// 发送消息
			requirementStatusService.send(requirementStatusDto);
		}
	}

	/**
	 * 根据品名获取钢厂信息
	 */
	@Override
	public Map<String, List<BaseDataDto>> loadFactoryByCategory(Long purchaseOrderId) {
		Map<String, List<BaseDataDto>> map = new HashMap<>();
		List<String> categoryUuids = purchaseOrderItemsDao.getCategoryByPurchaseOrderId(purchaseOrderId);
		for (int i = 0; i < categoryUuids.size(); i++) {
			String categoryUuid = categoryUuids.get(i);
			map.put(categoryUuid, createPruchaseOrderDao.selectFactoryByCategoryUUID(categoryUuid));
		}
		return map;
	}

	/**
	 * 根据采购单Id获取采购单明细
	 */
	@Override
	public List<SearchResultItemsDto> getItemsByPurchaseOrderId(Long purchaseOrderId) {
		return purchaseOrderItemsDao.queryByPurchaseOrderId(purchaseOrderId);
	}

	/**
	 * 根据采购单Id获取询价详情列表
	 *
	 * @param purchaseOrderId
	 * @return
	 */
	@Override
	public List<InquiryDetailData> getInquiryHistoryByPurchaseId(Long purchaseOrderId) {
		List<InquiryDetailData> dataList = new ArrayList<InquiryDetailData>();
		List<InquiryDetailDto> list = purchaseOrderDao.selectInquiryHistoryByPurchaseId(purchaseOrderId);
		Map<Long, InquiryDetailData> inquiryDataMap = new LinkedHashMap<Long, InquiryDetailData>();// 根据询价单Id分组的数据集合
		Map<Long, InquiryDetailData> accountInquiryDataMap = new LinkedHashMap<Long, InquiryDetailData>();// 根据客户Id分组的数据集合
		InquiryDetailData inquiryData = null;
		InquiryDetailData accountInquiryData = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (list != null) {
			// 根据询价单Id合并数据
			for (InquiryDetailDto detail : list) {
				inquiryData = inquiryDataMap.get(detail.getInquiryOrderId());
				if (inquiryData == null) {
					inquiryData = new InquiryDetailData();
				}
				inquiryData.setAccountName(detail.getAccountName());
				inquiryData.setAccountId(detail.getAccountId());
				inquiryData.setCreateDateStr(sdf.format(detail.getCreated()));
				inquiryData.setDate(detail.getCreated().getTime());
				// 同一条询价单,联系人数据合并
				inquiryData.getContactList().addAll(
						createContactList(detail.getContactNames(), detail.getContactTels(), detail.getAccountId()));
				// 同一条询价单,资源数据合并
				inquiryData.getResList().add(createRes(detail));
				inquiryDataMap.put(detail.getInquiryOrderId(), inquiryData);
			}
			// 根据客户Id合并数据
			for (Entry<Long, InquiryDetailData> entry : inquiryDataMap.entrySet()) {
				inquiryData = entry.getValue();
				accountInquiryData = accountInquiryDataMap.get(inquiryData.getAccountId());
				if (accountInquiryData == null) {
					accountInquiryDataMap.put(inquiryData.getAccountId(), inquiryData);
				} else {
					// 客户Id一致合并数据,创建时间取最新
					accountInquiryData.setCreateDateStr(accountInquiryData.getDate() > inquiryData.getDate()
							? accountInquiryData.getCreateDateStr() : inquiryData.getCreateDateStr());
					accountInquiryData.getResList().addAll(inquiryData.getResList());
					accountInquiryDataMap.put(inquiryData.getAccountId(), accountInquiryData);
				}
			}
			// 数据汇总
			for (Entry<Long, InquiryDetailData> entry : accountInquiryDataMap.entrySet()) {
				accountInquiryData = entry.getValue();
				// 联系人去重
				accountInquiryData.setContactList(toRepeat(accountInquiryData.getContactList()));
				// 品名中文排序,品名一致的前台不显示
				Collections.sort(accountInquiryData.getResList(), new ChineseCharComparator());
				dataList.add(accountInquiryData);
			}
		}
		return dataList;
	}

	/**
	 * 联系人列表
	 *
	 * @param contactNames
	 * @param contactTels
	 * @param accountId
	 * @return
	 */
	private List<InquiryDetailContact> createContactList(String contactNames, String contactTels, Long accountId) {
		List<InquiryDetailContact> list = new ArrayList<InquiryDetailContact>();
		if (StringUtils.isNotEmpty(contactNames) && StringUtils.isNotEmpty(contactTels)) {
			InquiryDetailContact createContact = null;
			String[] contactNameArray = contactNames.split(",");
			String[] contactTelArray = contactTels.split(",");
			int i = 0;
			for (; i < contactNameArray.length; i++) {
				if (contactNameArray.length > i && contactTelArray.length > i) {
					createContact = new InquiryDetailContact(contactNameArray[i], contactTelArray[i], accountId);
					list.add(createContact);
				}
			}
		}
		return list;
	}

	/**
	 * 去除重复练习人
	 *
	 * @return
	 */
	private List<InquiryDetailContact> toRepeat(List<InquiryDetailContact> list) {
		List<InquiryDetailContact> newList = new ArrayList<InquiryDetailContact>();
		List<String> telList = new ArrayList<String>();
		for (InquiryDetailContact contact : list) {
			if (!telList.contains(contact.getContactTel())) {
				newList.add(contact);
				telList.add(contact.getContactTel());
			}
		}
		return newList;
	}

	/**
	 * 创建资源
	 *
	 * @param detail
	 * @return
	 */
	private InquiryDetailRes createRes(InquiryDetailDto detail) {
		InquiryDetailRes res = new InquiryDetailRes(detail.getCategoryName(), detail.getMaterialName(),
				detail.getSpec(), detail.getFactoryName(), detail.getWarehouseName(), detail.getWeight(),
				detail.getPrice());
		return res;
	}

	/**
	 * 中文排序
	 *
	 * @author tangwei
	 */
	public class ChineseCharComparator implements Comparator<InquiryDetailRes> {
		@Override
		public int compare(InquiryDetailRes o1, InquiryDetailRes o2) {
			Collator collator = Collator.getInstance(Locale.CHINA);
			int result = collator.compare(o1.getCategoryName(), o2.getCategoryName());
			if (result < 0) {
				return -1;
			} else if (result > 0) {
				return 1;
			} else {
				o1.setDisplayCategoryName("");
				return 0;
			}
		}
	}

	/**
	 * 指派采购单
	 *
	 * @param id
	 * @return
	 */
	@Override
	public void assignOrder(Long id, Integer assignto, Long orgId, Long userId, boolean isActivate, User operator) {
		if (id == null || id.equals(0l) || operator == null || assignto == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "指派失败，请求参数不正确！");
		}
		PurchaseOrder order = purchaseOrderDao.selectByPrimaryKey(id);
		if (order == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "指派失败，询价单不存在！");
		}
		if (!PurchaseOrderStatus.PENDING_CLERK_ACCEPTE.getCode().equals(order.getStatus())
				&& !PurchaseOrderStatus.PENDING_ACCEPTE.getCode().equals(order.getStatus())
				&& !PurchaseOrderStatus.PENDING_DIRECTOR_ASSIGNED.getCode().equals(order.getStatus())
				&& !PurchaseOrderStatus.PENDING_SERVER_MANAGER_ASSIGNED.getCode().equals(order.getStatus())
				&& !PurchaseOrderStatus.CLOSED.getCode().equals(order.getStatus())
				&& !PurchaseOrderStatus.PENDING_OPEN_BILL.getCode().equals(order.getStatus())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "指派失败，询价单已被处理！");
		}
		order.setLastUpdatedBy(operator.getName());
		order.setLastUpdated(new Date());
		order.setOwnerId(userId);
		order.setOrgId(orgId);
		if (assignto.equals(1)) {
			order.setStatus(PurchaseOrderStatus.PENDING_SERVER_MANAGER_ASSIGNED.getCode());// 指派完待交易员处理
		} else if (assignto.equals(2)) {
			order.setStatus(PurchaseOrderStatus.PENDING_CLERK_ACCEPTE.getCode());// 指派完待交易员处理
		} else if (assignto.equals(3)) {
			order.setStatus(PurchaseOrderStatus.PENDING_ACCEPTE.getCode());// 指派完待交易员处理
		}else if(assignto.equals(4)){
			order.setStatus(PurchaseOrderStatus.PENDING_OPEN_BILL.getCode());
		} else {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "指派失败，未选择指派人！");
		}
		// 添加操作人
		String userIds = order.getUserIds();
		if (!StringUtils.isEmpty(userIds)) {
			if (!Arrays.asList(userIds.split(",")).contains(operator.getId().toString())) {
				userIds += "," + operator.getId().toString();
			}
		} else {
			userIds = operator.getId().toString();
		}
		order.setUserIds(userIds);
		Integer num = order.getModificationNumber();
		if (num == null) {
			num = 0;
		}
		num++;
		order.setModificationNumber(num);
		order.setId(id);

		purchaseOrderDao.updateByPrimaryKeySelective(order);
		// 发送短信通知
		try {
			String phone = getRestUserService().queryById(userId).getTel();
			String content = String.format("您好，%s指派了询价单%s给你，请及时处理!", operator.getName(), order.getCode());
			HashMap<String, String> params = new HashMap<>();
			String timestamp = DateUtil.dateToStr(new Date(), Constant.DATEFORMAT_YYYYMMDD_HHMMSS);
			params.put("timestamp", timestamp);
			content = content.replaceAll(" ", "");
			params.put("content", content);
			String from = Constant.FROM;
			params.put("from", from);
			params.put("phone", phone);
			String token;
			try {
				token = StringUtil.getSignature(params, Constant.SECRET);
			} catch (IOException e) {
				logger.error("发送短信时生成token失败");
				throw new BusinessException(e.getMessage(), "==");
			}
			getRestSendSmsServiceBean();
			if (this.restSendSmsService != null) {
				restSendSmsService.send(timestamp, token, phone, content, from);
			} else {
				logger.error("--------------------找不到发送短信的服务的bean[beanName:restSendSmsService]");
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到发送短信的服务接口！");
			}
		} catch (Exception smsEx) {
			logger.error("发送短信失败：" + smsEx.toString());
		}
		if(isActivate) {
			try {
				RequirementStatusDto requirementStatusDto = new RequirementStatusDto();
				requirementStatusDto.setStatusTo("ACTIVATED"); //激活
				requirementStatusDto.setCloseReason("");
				requirementStatusDto.setOperated(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				requirementStatusDto.setCode(order.getRequirementCode()); //超市需求单号
				requirementStatusDto.setOperateCode(order.getExt1());//分检询价单
				requirementStatusService.send(requirementStatusDto);
			} catch (Exception ex) {
				logger.error("询价单激活发送到超市失败：" + ex.toString());
			}
		}
	}

	/**
	 * 改派采购单
	 *
	 * @param id
	 * @return
	 */
	@Override
	public void reassignOrder(Long id, String remark, User operator) {
		if (id == null || id.equals(0l) || operator == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "改派失败，请求参数不正确！");
		}
		PurchaseOrder order = purchaseOrderDao.selectByPrimaryKey(id);
		if (order == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "改派失败，询价单不存在！");
		}
		if (!PurchaseOrderStatus.PENDING_QUOTE.getCode().equals(order.getStatus())
				&& !PurchaseOrderStatus.QUOTED.getCode().equals(order.getStatus())
				&& !PurchaseOrderStatus.PENDING_ACCEPTE.getCode().equals(order.getStatus())
				&& !PurchaseOrderStatus.PENDING_CLERK_ACCEPTE.getCode().equals(order.getStatus())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "改派失败，询价单已被处理！");
		}
		order.setPreOwnerId(order.getOwnerId());
		order.setLastUpdatedBy(operator.getName());
		order.setLastUpdated(new Date());
		order.setCloseReason(remark);
		if (PurchaseOrderStatus.PENDING_CLERK_ACCEPTE.getCode().equals(order.getStatus())) {
			order.setStatus(PurchaseOrderStatus.PENDING_SERVER_MANAGER_ASSIGNED.getCode());
		} else {
			order.setStatus(PurchaseOrderStatus.PENDING_DIRECTOR_ASSIGNED.getCode());// 改派回网销主管
		}
		String userIds = order.getUserIds();
		List<User> managers = userDao.queryDirectManagersByRoleId(operator.getRoleId());
		if (managers == null) {  //若无上级，指派给自己
			managers = new ArrayList<>();
			managers.add(operator);
		}
		for (User item : managers) { //有上级，将所有 上级添加到
			order.setOwnerId(item.getId());
			// 添加操作人
			if (!StringUtils.isEmpty(userIds)) {
				if (!Arrays.asList(userIds.split(",")).contains(operator.getId().toString())) {
					userIds += "," + operator.getId().toString();
				}
			} else {
				userIds = operator.getId().toString();
			}
		}

		order.setUserIds(userIds);
		Integer num = order.getModificationNumber();
		if (num == null) {
			num = 0;
		}
		num++;
		order.setModificationNumber(num);
		order.setId(id);
		// add by caosulin 160616 给增加的受理人字段赋值
		order.setAccepter(operator.getLoginId());

		purchaseOrderDao.updateByPrimaryKeySelective(order);
		// 发送短信通知
		try {
			for (User item : managers) {
				if (!item.getId().equals(operator.getId())) {
					String phone = getRestUserService().queryById(item.getId()).getTel();
					String content = String.format("您好，%s改派了询价单%s给你，请及时处理!", operator.getName(), order.getCode());
					HashMap<String, String> params = new HashMap<>();
					String timestamp = DateUtil.dateToStr(new Date(), Constant.DATEFORMAT_YYYYMMDD_HHMMSS);
					params.put("timestamp", timestamp);
					content = content.replaceAll(" ", "");
					params.put("content", content);
					String from = Constant.FROM;
					params.put("from", from);
					params.put("phone", phone);
					String token;
					try {
						token = StringUtil.getSignature(params, Constant.SECRET);
					} catch (IOException e) {
						logger.error("发送短信时生成token失败");
						throw new BusinessException(e.getMessage(), "==");
					}
					getRestSendSmsServiceBean();
					if (this.restSendSmsService != null) {
						restSendSmsService.send(timestamp, token, phone, content, from);
					} else {
						logger.error("--------------------找不到发送短信的服务的bean[beanName:restSendSmsService]");
						throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到发送短信的服务接口！");
					}
				}
			}
		} catch (Exception smsEx) {
			logger.error("发送短信失败：" + smsEx.toString());
		}
	}

	/**
	 * 报价单推送
	 *
	 * @param quotationOrderId
	 *            报价单id
	 * @return
	 * @author peanut
	 * @date 2016/6/27
	 */
	@Override
	public void pushQuotationOrder(Long quotationOrderId, Long orgId, Long userId) {
		if (quotationOrderId == null) {
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "报价单id为空!");
		}

		QuotationOrder quotationOrder = quotationOrderDao.selectByPrimaryKey(quotationOrderId);//查询报价单

		if (QuotationOrderStatus.CONFIRMED.getCode().equals(quotationOrder.getStatus())) {

			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "报价单已推送!");

		} else if (QuotationOrderStatus.UNCONFIRMED.getCode().equals(quotationOrder.getStatus())) {
			try {

				RequirementStatusDto requirementStatusDto = quotationOrderDao
						.selectPushQuotationOrder(quotationOrderId);
				// 有需求单号的单子才要推送
				if (StringUtils.isNotBlank(requirementStatusDto.getCode())) {
					// 已报价
					requirementStatusDto.setStatusTo(PurchaseOrderStatus.QUOTED.getCode());

					// 发送消息
					requirementStatusService.send(requirementStatusDto);

					// 更新状态
					quotationOrder.setStatus(QuotationOrderStatus.CONFIRMED.getCode());
					quotationOrderDao.updateByPrimaryKeySelective(quotationOrder);
				}

			} catch (BusinessException e) {
				e.setMsg("报价单推送失败!");
			}
		} else {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "报价单状态错误!");
		}
		
		if (orgId != null || userId != null) {
			//查询询价单信息
			PurchaseOrder purchaseOrder = purchaseOrderDao.selectByPrimaryKey(quotationOrder.getPurchaseOrderId());
			if (quotationOrder.getPushNumber() != 0) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该报价单已推送，请刷新页面!");
			} else {
				QuotationOrder newQuotationOrder = new QuotationOrder();
				newQuotationOrder.setPushNumber(1);
				newQuotationOrder.setId(quotationOrderId);
				quotationOrderDao.updateByPrimaryKeySelective(newQuotationOrder);
			}
			if (userId == null) {
				com.prcsteel.platform.acl.model.model.Organization organization = organizationDao.queryById(orgId);
				userId = organization.getCharger();
			}
			PurchaseOrder newPurchaseOrder = new PurchaseOrder();
			newPurchaseOrder.setOrgId(orgId);
			newPurchaseOrder.setOwnerId(userId);
			newPurchaseOrder.setStatus(PurchaseOrderStatus.PENDING_OPEN_BILL.getCode());//修改询价单状态为待开单状态
			newPurchaseOrder.setId(quotationOrder.getPurchaseOrderId());
			if (purchaseOrderDao.updateByPrimaryKeySelective(newPurchaseOrder) == 0) {
				 throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "询价单修改失败！");
			}
			User user = userDao.queryById(userId);
			if (user == null) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没有对应的服总或交易员！");
			}
			if (user.getTel() == null) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, user.getName() + "的手机号为空！");
			}
			String content = String.format("%s，你好，询价单%s已确认报价，请及时到询价单管理中查看并开单。", user.getName(), purchaseOrder.getCode());
			HashMap<String, String> params = new HashMap<>();
	        String timestamp = DateUtil.dateToStr(new Date(), Constant.DATEFORMAT_YYYYMMDD_HHMMSS);
	        params.put("timestamp", timestamp);
	        content = content.replaceAll(" ", "");
	        params.put("content", content);
	        String from = Constant.FROM;
	        params.put("from", from);
	        params.put("phone", user.getTel());
			String token;
	        try {
	            token = StringUtil.getSignature(params, Constant.SECRET);
	        } catch (IOException e) {
	            logger.error("发送短信时生成token失败",e);
	            throw new BusinessException(e.getMessage(), "==");
	        }
	        //获取发送短信的服务
	        getRestSendSmsServiceBean();
	        try {
		        if(this.restSendSmsService != null){
		        	 restSendSmsService.send(timestamp, token, user.getTel(), content, from);
		        }else{
		        	logger.error("--------------------找不到发送短信的服务的bean[beanName:restSendSmsService]");
		        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"找不到发送短信的服务接口！");
		        }
	        } catch (Exception e) {
	            logger.error("发送短信失败!",e);
	            throw new BusinessException(e.getMessage(), "发送短信失败!");
	        }	
		}
	}

	/**
	 * 根据需求单号查询询价单
	 * 
	 * @param requirementCode
	 */
	public Long searchPurchaseOrderByrequrimentCode(String requirementCode) {
		return purchaseOrderDao.searchPurchaseOrderByrequrimentCode(requirementCode);
	}

	/**
	 * 报价单点开单前的处理
	 *
	 * @param quotationOrderId
	 *            报价单id
	 * @return
	 * @author peanut
	 * @date 2016/7/1
	 */
	@Override
	public String preOpen(Long quotationOrderId) {
		if (quotationOrderId == null) {
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "报价单id为空!");
		}
		QuotationOrder quotationOrder = quotationOrderDao.selectByPrimaryKey(quotationOrderId);

		// modify by caosulin 获取询价单
		PurchaseOrder purchaseorder = purchaseOrderDao.selectByPrimaryKey(quotationOrder.getPurchaseOrderId());
		if (purchaseorder.getRequirementCode() != null) {// 如果询价单有需求单号，则需要推送到MQ
			// 如果未推送,则调用推送方法
			if (!QuotationOrderStatus.CONFIRMED.getCode().equals(quotationOrder.getStatus())) {
				pushQuotationOrder(quotationOrderId, null, null);
			}
		} else {
			if (!QuotationOrderStatus.CONFIRMED.getCode().equals(quotationOrder.getStatus())) {
				QuotationOrder record = new QuotationOrder();
				record.setId(quotationOrderId);
				record.setStatus(QuotationOrderStatus.CONFIRMED.getCode());
				quotationOrderDao.updateByPrimaryKeySelective(record);
			}
		}
		return purchaseorder.getRequirementCode();
	}

	/**
	 * 询价单点开单前的处理
	 *
	 * @param quotationOrderId
	 *            报价单id
	 * @return
	 * @author peanut
	 * @date 2016/7/1
	 */
	public void preOpenPurchaseOrder(Long purchaseOrderId, String requestCode) {
		if (purchaseOrderId == null) {
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "询价单id为空!");
		}
		QuotationOrder quotationOrder = quotationOrderDao.selectByPurchaseOderId(purchaseOrderId);

		if (quotationOrder != null) {
			if (StringUtils.isBlank(requestCode)) {
				if (!QuotationOrderStatus.CONFIRMED.getCode().equals(quotationOrder.getStatus())) {
					QuotationOrder record = new QuotationOrder();
					record.setId(quotationOrder.getId());
					record.setStatus(QuotationOrderStatus.CONFIRMED.getCode());
					quotationOrderDao.updateByPrimaryKeySelective(record);
					quotationOrder.setStatus(record.getStatus());
				}
			}
			// modify by caosulin 获取询价单
			// 如果未推送,则调用推送方法
			if (!QuotationOrderStatus.CONFIRMED.getCode().equals(quotationOrder.getStatus())) {
				pushQuotationOrder(quotationOrder.getId(), null, null);
			}
		} /*
			 * else{ throw new
			 * BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM,
			 * "开单前请先生成报价单！！"); }
			 */

	}

	@Override
	public City getCityByOrderId(Long orderId) {
		if (orderId == null || orderId.equals(0l)) {
			return null;
		}
		PurchaseOrder order = purchaseOrderDao.selectByPrimaryKey(orderId);
		if (order == null || order.getDeliveryCityId() == null) {
			return null;
		}
		City city = cityDao.selectByPrimaryKey(order.getDeliveryCityId());
		return city;
	}

	@Override
	public int insertInquiryMsg(InquiryOrderMessageQueue msg) {
		return purchaseOrderDao.insertInquiryMsg(msg);
	}

	@Override
	public List<PurchaseOrderItemsAttributeDto> getAllAttributes() {
		return attributesDao.getAllAttributes();
	}
	
    /**
     * 根据报价单详情的ID更新其对应的询价单的状态
     * @param id
     * @param string
     * @param loginId
     */
	public void updatePurchaseOrderStatusByQuotataionItemId(Long id, String string,
			String loginId){
		purchaseOrderDao.updatePurchaseOrderStatusByQuotataionItemId(id,string,loginId);
	}

	/**
	 * 关闭并推送报价
	 * @param id
	 * @param reason
	 * @param loginUser
	 */
	public void pushAndCloseOrder(Long purchaseId, String reason, User loginUser) {
		//获取当前的询价单
		PurchaseOrder order = purchaseOrderDao.selectByPrimaryKey(purchaseId);
		if(order == null){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到当前询价单！"+purchaseId);
		}
		// 查询出采购单对应的最新报价单
		QuotationOrder qorder = quotationOrderDao.selectByPurchaseOderId(purchaseId);
		if(qorder == null){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到当前询价单最新的报价单，请先报价！"+purchaseId);
		}
		String push_status = null;
		// 修改推送状态，把当前询价单最近的一条报价单状态改为已推送
		// 如果当前报价单没有推送过价格
		if(qorder.getStatus().equals(QuotationOrderStatus.UNCONFIRMED.getCode())){
			qorder.setStatus(QuotationOrderStatus.CONFIRMED.getCode());
			qorder.setLastUpdated(new Date());
			qorder.setLastUpdatedBy(loginUser.getLoginId());
			quotationOrderDao.updateByPrimaryKeySelective(qorder);
			
			push_status = PurchaseOrderStatus.QUOTED_CLOSED.getCode();//推送的状态为推送并关闭
		}else{//如果当前报价单推送过
			push_status = PurchaseOrderStatus.CLOSED.getCode();//推送的状态为推送并关闭
		}

		// 以下是已关闭的逻辑
		// 1.关闭询价单， 修改当前询价单的状态为已关闭。
		order.setOriginStatus(order.getStatus()); // 把关闭前状态设置成原状态
		order.setStatus(PurchaseOrderStatus.CLOSED.toString()); // 设置新状态
		order.setLastUpdatedBy(loginUser.getLoginId());
		order.setCloseReason(reason); // 关闭理由
		if (purchaseOrderDao.updateByPrimaryKeySelective(order) != 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "订单关闭失败！");
		}
		// 如果有需求单号的则要发送MQ，推送给超市
		// 2.发送MQ，有需求单号并且关闭操作--添加更新需求单至mq
		if (StringUtils.isNotBlank(order.getRequirementCode())
				&& PurchaseOrderStatus.CLOSED.getCode().equals(order.getStatus())) {

			RequirementStatusDto requirementStatusDto = new RequirementStatusDto();
			//推送状态为
			requirementStatusDto.setStatusTo(push_status);
			//关闭原因
			requirementStatusDto.setCloseReason(reason);
			//操作日期
			requirementStatusDto.setOperated(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			//需求单号
			requirementStatusDto.setCode(order.getRequirementCode());
			//推送报价单id
			requirementStatusDto.setOperateCode(String.valueOf(qorder.getId()));
			//发送消息
			requirementStatusService.send(requirementStatusDto);
		}
	}

	/***
	 * 获取 回退人员列表
	 * @param userId
	 * @return
     */
	@Override
	public List<User> getBackoffUsers(Long userId){
		return userDao.querySiblingById(userId);
	}
}

