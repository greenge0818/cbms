package com.prcsteel.platform.smartmatch.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.prcsteel.platform.core.model.model.Category;
import com.prcsteel.platform.core.persist.dao.CategoryDao;
import com.prcsteel.platform.smartmatch.api.RestAccountService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.persist.dao.AccountContactDao;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.DateUtil;
import com.prcsteel.platform.core.model.model.CategoryNorms;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.persist.dao.CategoryNormsDao;
import com.prcsteel.platform.core.persist.dao.CityDao;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.enums.ConsignType;
import com.prcsteel.platform.order.model.enums.InvoiceSpeedType;
import com.prcsteel.platform.smartmatch.model.dto.BillHistoryDataDto;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderDto;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderSellersDto;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderItemsDtoTwo;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchCityCompaniesDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchCompanyContactDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchCompanyResultDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchItermResultDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchResourceDtoTwo;
import com.prcsteel.platform.smartmatch.model.dto.SearchResultDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchResultDtoTwo;
import com.prcsteel.platform.smartmatch.model.dto.SearchResultInquiryDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchResultItemsDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchResultSellerDto;
import com.prcsteel.platform.smartmatch.model.dto.SellerMatchWeight;
import com.prcsteel.platform.smartmatch.model.enums.ResourceException;
import com.prcsteel.platform.smartmatch.model.enums.ResourceOperType;
import com.prcsteel.platform.smartmatch.model.enums.ResourceSourceType;
import com.prcsteel.platform.smartmatch.model.enums.ResourceStatus;
import com.prcsteel.platform.smartmatch.model.model.InquiryHistory;
import com.prcsteel.platform.smartmatch.model.model.InquiryOrder;
import com.prcsteel.platform.smartmatch.model.model.InquiryOrderItems;
import com.prcsteel.platform.smartmatch.model.model.InquiryOrderSellers;
import com.prcsteel.platform.smartmatch.model.model.PurchaseOrder;
import com.prcsteel.platform.smartmatch.model.model.PurchaseOrderItems;
import com.prcsteel.platform.smartmatch.model.model.ResourceBase;
import com.prcsteel.platform.smartmatch.model.model.ResourceNorms;
import com.prcsteel.platform.smartmatch.model.model.ResourceNormsBase;
import com.prcsteel.platform.smartmatch.model.query.ResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.SearchPurchaseOrderQuery;
import com.prcsteel.platform.smartmatch.model.query.SmartMatchQuery;
import com.prcsteel.platform.smartmatch.persist.dao.FactoryDao;
import com.prcsteel.platform.smartmatch.persist.dao.InquiryHistoryDao;
import com.prcsteel.platform.smartmatch.persist.dao.InquiryOrderDao;
import com.prcsteel.platform.smartmatch.persist.dao.InquiryOrderItemsDao;
import com.prcsteel.platform.smartmatch.persist.dao.InquiryOrderSellersDao;
import com.prcsteel.platform.smartmatch.persist.dao.PurchaseOrderDao;
import com.prcsteel.platform.smartmatch.persist.dao.PurchaseOrderItemAttributesDao;
import com.prcsteel.platform.smartmatch.persist.dao.PurchaseOrderItemsDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceBaseDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceNormsBaseDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceNormsDao;
import com.prcsteel.platform.smartmatch.persist.dao.WarehouseDao;
import com.prcsteel.platform.smartmatch.service.ResourceService;
import com.prcsteel.platform.smartmatch.service.SearchService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by Green.Ge on 2015/11/19.
 */

@Service("searchService")
public class SearchServiceImpl implements SearchService, ApplicationContextAware {
	@Resource
	PurchaseOrderDao purchaseOrderDao;
	@Resource
	PurchaseOrderItemsDao purchaseOrderItemsDao;
	@Resource
	ResourceDao resourceDao;
	@Resource
	private FactoryDao factoryDao;
	@Resource
	private AccountService accountService;
	@Resource
	private UserService userService;
	@Resource
	private AccountContactDao accountContactDao;
	@Resource
	private InquiryOrderDao inquiryOrderDao;
	@Resource
	private InquiryOrderSellersDao inquiryOrderSellersDao;
	@Resource
	private InquiryOrderItemsDao inquiryOrderItemsDao;
	@Resource
	private InquiryHistoryDao inquiryHistoryDao;
	@Resource
	private CategoryDao categoryDao;
	@Resource
	private CategoryNormsDao categoryNormsDao;
	@Resource
	private ResourceNormsDao resourceNormsDao;
	@Resource
	private ResourceService resourceService;
	@Resource
	private WarehouseDao warehouseDao;
	@Resource
	private PurchaseOrderItemAttributesDao attributesDao;
	@Resource
	private CityDao cityDao;
	@Resource
	private ResourceBaseDao resourceBaseDao;
	@Resource
	private ResourceNormsBaseDao resourceNormsBaseDao;
	// 日志
	private final Logger logger = LoggerFactory.getLogger(RequirementStatusServiceImpl.class);

	/**
	 * 规格属性最长为3个
	 */
	private static final Integer MAX_SPEC_LENGTH = 3;

	// spring上下文
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
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

	@SuppressWarnings("serial")
	@Override
	/***
	 * 
	 * @Title: match @Description: 智能匹配 @param @param purchaseOrderId
	 *         采购单号 @param @param notSpecificSellerId 指定不搜索的卖家列表 @param @param
	 *         specificSellerId 指定搜索某个卖家 @param isAppend 是否是点开后查询的 @return
	 *         参数 @return SearchResultDto 返回类型 @throws
	 */
	public SearchResultDto match(Long purchaseOrderId, List<Long> purchaseOrderItemIds, List<Long> noSpecificSellerList,
			Long specificSellerId, Integer isAppend) {

		/**
		 * 如果已经询价，获取询价过的资源集合
		 */
		// 循环获取每个卖家资源集
		List<SearchResultInquiryDto> inquiryList = new ArrayList<>();
		if (isAppend == null || isAppend != 1) {
			// 询价单列表
			List<InquiryOrderDto> inquiryOrders = inquiryOrderDao.getInquiryOrders(purchaseOrderId);

			for (InquiryOrderDto inquiryOrderDto : inquiryOrders) {

				Long inquiryOrderId = inquiryOrderDto.getInquiryOrderId();
				List<InquiryOrderSellers> inquiryOrderSellers = inquiryOrderSellersDao
						.getInquiryOrderSellers(inquiryOrderId);

				List<SearchResultSellerDto> sellerList = new ArrayList<SearchResultSellerDto>();
				for (InquiryOrderSellers inquiryOrderseller : inquiryOrderSellers) {
					Long inquiryOrderSellerId = inquiryOrderseller.getId();
					Long sellerId = inquiryOrderseller.getAccountId();
					// noSpecificSellerList.add(sellerId);
					List<SearchResultItemsDto> itemList = inquiryOrderItemsDao
							.getInquiryOrderItems(inquiryOrderSellerId);

					Account account = accountService.queryById(sellerId);
					User trader = userService.queryById(account.getManagerId());
					List<AccountContact> contacts = accountContactDao.queryContactListByAccountId(sellerId);

					String traderName = "";
					if (trader != null) {
						traderName = trader.getName();
					}
					if (itemList.size() > 0) {
						// 根据卖家id获取最近一次询价记录
						InquiryOrderSellersDto lastInquiryOrderSeller = inquiryOrderSellersDao
								.selectByAccountId(sellerId);
						if (lastInquiryOrderSeller != null) {
							SearchResultSellerDto sellerDto = new SearchResultSellerDto(account.getConsignType(),
									account.getId(), account.getName(), contacts, account.getManagerId(), traderName,
									itemList,
									DateUtil.showTime(lastInquiryOrderSeller.getLastUpdated(), "yyyy-MM-dd HH:mm:ss"),
									lastInquiryOrderSeller.getLastUserName());
							sellerList.add(sellerDto);
						} else {
							SearchResultSellerDto sellerDto = new SearchResultSellerDto(account.getConsignType(),
									account.getId(), account.getName(), contacts, account.getManagerId(), traderName,
									itemList, null, null);
							sellerList.add(sellerDto);
						}
					}
				}
				if (sellerList.size() > 0) {
					SearchResultInquiryDto inquiryDto = new SearchResultInquiryDto(sellerList,
							Constant.INQUIRYORDER_SAVE_STATUS_INQUIRY, Constant.INQUIRYORDER_SAVE_STATUS_SAVED,
							inquiryOrderId);
					inquiryList.add(inquiryDto);
				}

			}
		}

		/**
		 * 获取能匹配到的询价资源集合
		 */
		// 检查订单ID参数合法性
		PurchaseOrder po = checkPurchaseOrderId(purchaseOrderId);
		List<SellerMatchWeight> sellerWeightList = new ArrayList<SellerMatchWeight>();
		if (specificSellerId != null) {
			/**
			 * 有指定的卖家，匹配度优先
			 */
			Account seller = accountService.queryById(specificSellerId);
			if (seller == null) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "指定卖家并不存在");
			}
			// 指定卖家，此卖家满分入选，别无竞争人选
			sellerWeightList.add(new SellerMatchWeight(specificSellerId, seller.getName(), 1d));
		} else {
			/**
			 * 无指定的卖家
			 */
			/**
			 * 获取采购单中的采购资源列表
			 */
			List<PurchaseOrderItems> itemList;
			if (purchaseOrderItemIds == null || purchaseOrderItemIds.size() == 0) {
				itemList = purchaseOrderItemsDao.getListByPOId(purchaseOrderId);
			} else {
				itemList = purchaseOrderItemsDao.getListByIds(purchaseOrderItemIds);
			}

			// （1） 先筛选出资源仓库及客户所在地在采购地的卖家；

			// 资源仓库所在地
			String purchaseCitys = "";
			// 采购地是否为空
			if (StringUtils.isNotBlank(po.getPurchaseCityIds())) {
				purchaseCitys += po.getPurchaseCityIds();
				if (po.getPurchaseCityOtherId() != null) {
					// 加上其他采购地
					purchaseCitys += "," + po.getPurchaseCityOtherId();
				}
			} else {
				// 取其他采购地
				purchaseCitys = po.getPurchaseCityOtherId() == null ? "" : po.getPurchaseCityOtherId().toString();
			}
			/**
			 * 根据采购城市或者其他采购城市获取商家列表？
			 */
			List<Account> sellerList = resourceDao.getAvailableAccountListByCityId(purchaseCitys, purchaseOrderId);
			sellerList = sellerList.stream().filter(a -> !noSpecificSellerList.contains(a.getId()))
					.collect(Collectors.toList());
			if (sellerList.isEmpty()) {
				return null;
			}
			String sellers = StringUtils.join(sellerList.stream().map(Account::getId).collect(Collectors.toList()),
					',');

			// （2） 计算出这些卖家的匹配权重系数，由高到低排列；
			// （3） 匹配权重系数=品规覆盖权重（W）+价格权重（X）+卖家资质权重Y+历史成交权重（Z）；

			int topN = 5;
			SmartMatchQuery query = new SmartMatchQuery(purchaseOrderId, sellers, purchaseOrderItemIds,
					noSpecificSellerList, specificSellerId);
			// SellerMatchWeight smw;
			// （4） 品规覆盖权重（W）：库存中卖家资源100%满足=0.3；60%-100%=0.2；30%-60%=0.1；0-30%=0；
			List<SellerMatchWeight> listCoverage = purchaseOrderItemsDao.getSellerCategoryCoverage(query);
			// （5） 价格权重（X）：每条明细单价由低到高排序，排在前3位的加0.1，最多0.3；
			List<SellerMatchWeight> listPrice = purchaseOrderItemsDao.getSellerPriceWeight(query);

			// （7）
			// 历史成交权重Z：最近一个月内成交15笔以上的Z=0.15；成交6-15的Z=0.1；成交1-5笔的Z=0.05；没有成交的Z=0；（注：一个订单中成交2条明细的记成交2笔）；
			List<SellerMatchWeight> listHistory = getWeightedForSeller(sellerList, itemList);

			// 注：历史成交到已关联状态；
			int size = 10;
			// ExecutorService executorService =
			// Executors.newCachedThreadPool();
			ExecutorService executorService = Executors.newFixedThreadPool(size);
			// 协调线程之间
			CountDownLatch countDownLatch = new CountDownLatch(size);
			for (Account seller : sellerList) {
				FutureTask<SellerMatchWeight> futureTask = new FutureTask<SellerMatchWeight>(
						new RunnerCallable(countDownLatch, seller, listCoverage, listPrice, listHistory, itemList));
				executorService.execute(futureTask);
				try {
					sellerWeightList.add(futureTask.get());
				} catch (Exception e) {
					throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "调用多线程匹配出现异常");
				}
			}

			// 如果不关闭，需要等待超时.
			executorService.shutdown();
			sellerWeightList.sort(
					(SellerMatchWeight smw1, SellerMatchWeight smw2) -> smw2.getWeight().compareTo(smw1.getWeight()));
			if (sellerWeightList.size() > topN) {
				sellerWeightList = new ArrayList<SellerMatchWeight>(sellerWeightList.subList(0, topN));
			}
		}

		// （8）
		// 匹配出卖家后从权重由高到低显示5组卖家，默认先展开权重最高的卖家详情，如果卖家资源不全的，展开时按同样规则匹配出所缺资源对应权重最高的卖家，如果补充匹配出的2个卖家还不能覆盖所有资源，则只显示3个卖家；

		return buildSearchResultDto(purchaseOrderId, purchaseOrderItemIds, sellerWeightList, inquiryList);

	}

	class RunnerCallable implements Callable {

		private CountDownLatch countDownLatch;
		private Account seller;
		private List<SellerMatchWeight> listCoverage;
		private List<SellerMatchWeight> listPrice;
		private List<SellerMatchWeight> listHistory;
		List<PurchaseOrderItems> itemList;

		public RunnerCallable(CountDownLatch countDownLatch, Account seller, List<SellerMatchWeight> listCoverage,
				List<SellerMatchWeight> listPrice, List<SellerMatchWeight> listHistory,
				List<PurchaseOrderItems> itemList) {
			super();
			this.countDownLatch = countDownLatch;
			this.seller = seller;
			this.listCoverage = listCoverage;
			this.listPrice = listPrice;
			this.listHistory = listHistory;
			this.itemList = itemList;
		}

		@Override
		public Object call() throws Exception {
			double totalWeight = 0;
			Optional<SellerMatchWeight> option;
			String weightDetail = "";
			// totalWeight=0;
			Long sellerId = seller.getId();
			// 品规覆盖率
			option = listCoverage.stream().filter(a -> a.getSellerId().equals(sellerId)).findFirst();
			if (option.isPresent()) {
				totalWeight += option.get().getWeight();
				weightDetail += "coverage:" + option.get().getWeight() + ";";
			}
			// 价格权重
			option = listPrice.stream().filter(a -> a.getSellerId().equals(sellerId)).findFirst();
			if (option.isPresent()) {
				totalWeight += option.get().getWeight();
				weightDetail += "price:" + option.get().getWeight() + ";";
			}
			// 历史成交
			option = listHistory.stream().filter(a -> a.getSellerId().equals(sellerId)).findFirst();
			if (option.isPresent()) {
				totalWeight += option.get().getWeight();
				weightDetail += "listHistory:" + option.get().getWeight() + ";";
			}
			//
			if (totalWeight > 0) {// 没资源或没卖过也就不用加分了 issue5139#
				// （6） 卖家资质权重Y：卖家性质a+开票速度b+经营资质c；
				//  代运营卖家a=0.1，临采卖家a=0.05，未审核资料卖家a=0；
				//  开票快b=0.05；开票正常b=0.03；开票慢b=0；
				// 
				// 经营了该品名，代理资质一、二级的c=0.1，经营了该品名，代理资质三级及以下的c=0.05，未经营该品名的c=0,有多个品名时，只要满足其中一个品名即可；
				double sellerQualificationWeight = getWeightedBaseOnSellerQualification(seller);
				totalWeight += sellerQualificationWeight;
				weightDetail += "Qualification:" + sellerQualificationWeight + ";";
			}

			SellerMatchWeight totalMatchWeight = new SellerMatchWeight(sellerId, seller.getName(), totalWeight);

			// 减一
			countDownLatch.countDown();
			return totalMatchWeight;
		}
	}

	private PurchaseOrder checkPurchaseOrderId(Long purchaseOrderId) {
		if (purchaseOrderId == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "采购单ID不能为空");
		}
		PurchaseOrder po = purchaseOrderDao.selectByPrimaryKey(purchaseOrderId);
		if (po == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "指定采购单不存在");
		}
		Long cityId = po.getDeliveryCityId();
		if (cityId == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "采购单未指定交货地城市");
		}
		return po;
	}

	// 可用于查询的订单状态：('RELATED', 'SECONDSETTLE', 'INVOICEREQUEST','INVOICE',
	// 'FINISH')
	private List<String> orderStatus = new ArrayList<String>() {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		{
			add(ConsignOrderStatus.RELATED.getCode());
			add(ConsignOrderStatus.SECONDSETTLE.getCode());
			add(ConsignOrderStatus.INVOICEREQUEST.getCode());
			add(ConsignOrderStatus.INVOICE.getCode());
			add(ConsignOrderStatus.FINISH.getCode());
		}
	};

	/**
	 * 获取卖家历史交易权值
	 * 
	 * @param sellerList
	 * @param items
	 * @return
	 */
	private List<SellerMatchWeight> getWeightedForSeller(List<Account> sellerList, List<PurchaseOrderItems> items) {
		List<SellerMatchWeight> weightList = new LinkedList<SellerMatchWeight>();
		List<SearchPurchaseOrderQuery> queryList = new LinkedList<SearchPurchaseOrderQuery>();
		String sellers = StringUtils.join(sellerList.stream().map(Account::getId).collect(Collectors.toList()), ',');
		for (PurchaseOrderItems i : items) {
			// 设置查询
			SearchPurchaseOrderQuery q = new SearchPurchaseOrderQuery();
			q.setSellers(sellers);
			q.setFactory(factoryDao.queryFactoryNamesByIds(i.getFactoryIds()));
			q.setNsortNameUuid(i.getCategoryUuid());
			q.setMaterialUuid(i.getMaterialUuid());
			q.setOrderStatus(orderStatus);
			q.setSpec1(i.getSpec1());
			q.setSpec2(i.getSpec2());
			q.setSpec3(i.getSpec3());
			q.setSpec4(i.getSpec4());
			q.setSpec5(i.getSpec5());
			q.setSqlOfspec(buildSqlForSpec(q));
			queryList.add(q);

		}
		// 采购项成绩记录叠加
		List<Map<String, Object>> list = purchaseOrderItemsDao.countHistoryTrading(queryList);
		for (Account seller : sellerList) {
			Long sellerId = seller.getId();
			Integer totalDeal = 0;
			if (list != null && list.size() > 0) {
				totalDeal = getSellerDealTotal(list, sellerId);
			} else {
				return weightList;
			}
			if (totalDeal > 0) {
				SellerMatchWeight weight = new SellerMatchWeight(sellerId, seller.getName(),
						getWeightedOfHistoryTrading(totalDeal));
				weightList.add(weight);
			}
		}
		return weightList;
	}

	private Integer getSellerDealTotal(List<Map<String, Object>> list, Long sellerId) {
		for (Iterator<Map<String, Object>> it = list.iterator(); it.hasNext();) {
			Map<String, Object> map = it.next();
			if (sellerId.equals((Long) map.get("seller_id"))) {
				it.remove();
				return ((BigDecimal) map.get("count")).intValue();
			}
		}

		return 0;
	}

	/**
	 * 获取历史交易权值，依据为该卖家近一个月内某品规交易笔数。
	 * <p/>
	 * 
	 * <pre>
	 * 历史成交权重（Z）业务规则:  最近一个月内成交15笔以上的Z=0.15；  成交6-15的Z=0.1；  成交1-5笔的Z=0.05； 
	 * 没有成交的Z=0；（注：一个订单中成交2条明细的记成交2笔）
	 * 
	 * <pre/>
	 *
	 * @param total
	 *            交易笔数
	 * @return
	 */
	private double getWeightedOfHistoryTrading(Integer total) {
		if (total > 15) {
			return 0.15;
		} else if (total >= 6 && total <= 15) {
			return 0.1;
		} else if (total >= 1 && total <= 5) {
			return 0.05;
		} else {
			return 0;
		}
	}

	/**
	 * 构建品规查询语句
	 * 
	 * @param query
	 * @return
	 */
	public String buildSqlForSpec(SearchPurchaseOrderQuery query) {
		StringBuffer sb = new StringBuffer();
		// spec1
		sb.append(buildSingleSqlBySpec(query.getSpec1(), 1));
		// spec2
		sb.append(buildSingleSqlBySpec(query.getSpec2(), 2));
		// spec3
		sb.append(buildSingleSqlBySpec(query.getSpec3(), 3));
		// spec4
		sb.append(buildSingleSqlBySpec(query.getSpec4(), 4));
		// spec5
		sb.append(buildSingleSqlBySpec(query.getSpec5(), 5));

		return sb.toString();
	}

	public final static String DELIMITER = "*"; // （规格）分隔字符定义。

	/**
	 * 构造单一规格查询SQL语句<br/>
	 *
	 * 注：数据库中需要创建func_split函数
	 * 
	 * <pre>
		 -- ----------------------------
		 -- Function structure for func_split
		 -- ----------------------------
		 DROP FUNCTION IF EXISTS `func_split`;
		 DELIMITER ;;
		 CREATE FUNCTION `func_split`(f_string varchar(1000),f_delimiter varchar(5),f_order int) RETURNS varchar(255) CHARSET utf8
		 BEGIN
		 # 拆分传入的字符串，返回拆分后的新字符串
		 declare result varchar(255) default '';
		 declare count int(20) default 0;
	
		 set	count = ((LENGTH(f_string) - LENGTH(REPLACE(f_string, f_delimiter, '')))/LENGTH(f_delimiter));
		 IF f_order <= count THEN  SET result = reverse(substring_index(reverse(substring_index(f_string,f_delimiter,f_order)),f_delimiter,1));
		 ELSE SET result = NULL;
		 END IF;
		 return result;
		 END
		 ;;
		 DELIMITER ;
	 * </pre>
	 * 
	 * @param spec
	 *            规格参数<br/>
	 *            格式: 数值 1; 多选 1,2,3; 区间 10-90;
	 * @param index
	 *            规格序号<br/>
	 *            格式: spec1 -> 1; spec2 -> 2; spec1 -> 2 ect.
	 * @return int
	 */
	private String buildSingleSqlBySpec(String spec, int index) {
		if (StringUtils.isBlank(spec)) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		if (spec.indexOf("-") >= 0) {
			// 区间
			String[] arrSpec = spec.split("-");
			if (arrSpec.length == 1) {
				sb.append(" AND CAST(func_split(spec, '" + DELIMITER + "', " + index + ") AS DECIMAL(10,6)) >= CAST("
						+ arrSpec[0] + " AS DECIMAL(18,6))");
			} else {
				sb.append(" AND CAST(func_split(spec, '" + DELIMITER + "', " + index + ") AS DECIMAL(10,6)) <= CAST("
						+ arrSpec[1] + " AS DECIMAL(18,6))");
			}
		} else if (spec.indexOf(",") >= 0) {
			// 多选
			sb.append(" AND func_split(spec, '" + DELIMITER + "', " + index + ") IN (" + spec + ")");
		} else {
			// 数值
			sb.append(" AND CAST(func_split(spec, '" + DELIMITER + "', " + index + ") AS DECIMAL(10,6)) = CAST(" + spec
					+ " AS DECIMAL(18,6))");
		}
		return sb.toString();
	}

	/**
	 * 获取卖家资质权值
	 *
	 * <p/>
	 * 
	 * <pre>
	 * 卖家资质权重业务规则：卖家性质a+开票速度b+经营资质c。  代运营卖家a=0.1，临采卖家a=0.05，未审核资料卖家a=0； 
	 * 开票快b=0.05；开票正常b=0.03；开票慢b=0； 
	 * 经营了该品名，代理资质一、二级的c=0.1，经营了该品名，代理资质三级及以下的c=0.05，
	 * 未经营该品名的c=0,有多个品名时，只要满足其中一个品名即可；
	 * 
	 * <pre/>
	 *
	 * @param account
	 * @return
	 */
	private double getWeightedBaseOnSellerQualification(Account account) {
		// 卖家性质a+开票速度b+经营资质c
		double weighted = 0;

		// 卖家性质
		if (!AccountType.buyer.toString().equals(account.getType())) {
			// 买家为0，非买家按下面逻辑计算。
			if (ConsignType.consign.toString().equals(account.getConsignType())) {
				weighted += 0.1;
			} else if (ConsignType.temp.toString().equals(account.getConsignType())) {
				weighted += 0.05;
			}
		}

		// 开票速度
		if (InvoiceSpeedType.FAST.getValue().equals(account.getInvoiceSpeed())) {
			weighted += 0.05;
		} else if (InvoiceSpeedType.NORMAL.getValue().equals(account.getInvoiceSpeed())) {
			weighted += 0.03;
		}
		// TODO:经营资质，需求确认。

		return weighted;
	}

	/**
	 * 构建询价信息集
	 *
	 * @param purchaseOrderId
	 *            采购单ID
	 * @param sellerIds
	 *            卖家ID集
	 * @param purchaseOrderItemId
	 *            采购单详情ID列表
	 * 
	 * @param inquiryList
	 *            已经询价的公司列表
	 * @return
	 */
	private SearchResultDto buildSearchResultDto(Long purchaseOrderId, List<Long> purchaseOrderItemId,
			List<SellerMatchWeight> sellerIds, List<SearchResultInquiryDto> inquiryList) {
		// 循环读取每个卖家资源集
		// List<SearchResultInquiryDto> inquiryList = new ArrayList<>();
		for (SellerMatchWeight sellerInfo : sellerIds) {
			// 得分为0的不返回了
			if (sellerInfo.getWeight().equals(0d)) {
				continue;
			}
			Long id = sellerInfo.getSellerId(); // 获取ID
			List<SearchResultSellerDto> sellerList = new ArrayList<SearchResultSellerDto>();
			SmartMatchQuery query = new SmartMatchQuery(purchaseOrderId, null, purchaseOrderItemId, null, id);
			// 资源详情集
			List<SearchResultItemsDto> itemList = purchaseOrderItemsDao.queryResourceItems(query);

			// 卖家相关信息
			Account account = accountService.queryById(id);
			User trader = userService.queryById(account.getManagerId());
			List<AccountContact> contacts = accountContactDao.queryContactListByAccountId(id);

			String traderName = "";
			if (trader != null) {
				traderName = trader.getName();
			}
			String consignType = "";
			// 买家不给寄售类型
			if (!AccountType.buyer.toString().equals(account.getType())) {
				consignType = account.getConsignType();
			}
			// 根据卖家id获取最近一次询价记录
			InquiryOrderSellersDto lastInquiryOrderSeller = inquiryOrderSellersDao.selectByAccountId(id);
			if (lastInquiryOrderSeller != null) {
				SearchResultSellerDto sellerDto = new SearchResultSellerDto(consignType, account.getId(),
						sellerInfo.getSellerName(), contacts, account.getManagerId(), traderName, itemList,
						DateUtil.showTime(lastInquiryOrderSeller.getLastUpdated(), "yyyy-MM-dd HH:mm:ss"),
						lastInquiryOrderSeller.getLastUserName());
				sellerList.add(sellerDto);
			} else {
				SearchResultSellerDto sellerDto = new SearchResultSellerDto(consignType, account.getId(),
						sellerInfo.getSellerName(), contacts, account.getManagerId(), traderName, itemList, "", "");
				sellerList.add(sellerDto);
			}

			SearchResultInquiryDto inquiryDto = new SearchResultInquiryDto(sellerList,
					Constant.INQUIRYORDER_SAVE_STATUS_UNINQUIRY, Constant.INQUIRYORDER_SAVE_STATUS_UNSAVE, 0L);
			inquiryList.add(inquiryDto);
		}
		SearchResultDto dto = new SearchResultDto(inquiryList);

		return dto;
	}

	/**
	 * 保存询价单
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Long saveInquiryOrder(String inquiryOrder, User user) {
		JSONObject inquiry = JSONObject.fromObject(inquiryOrder);
		String loginId = user.getLoginId();

		Long inquiryOrderId = null;
		Long purchaseOrderId = "null".equals(inquiry.getString("purchaseOrderId"))
				&& StringUtils.isNotBlank(inquiry.getString("purchaseOrderId")) ? null
						: Long.parseLong(inquiry.getString("purchaseOrderId"));

		InquiryOrder order = new InquiryOrder();
		order.setLastUpdatedBy(loginId);

		// if("true".equals(inquiry.getString("isSaveOrder"))){
		if ("".equals(inquiry.getString("inquiryOrderId")) || "0".equals(inquiry.getString("inquiryOrderId"))
				|| "null".equals(inquiry.getString("inquiryOrderId"))) {
			order.setCreatedBy(loginId);
			order.setPurchaseOrderId(purchaseOrderId);
			inquiryOrderDao.insertSelective(order);
			inquiryOrderId = order.getId();

		} else {
			inquiryOrderId = Long.parseLong(inquiry.getString("inquiryOrderId"));
			order.setId(inquiryOrderId);
			inquiryOrderDao.updateByPrimaryKeySelective(order);
			inquiryOrderItemsDao.deleteByInquiryOrderId(inquiryOrderId);
			inquiryOrderSellersDao.deleteByInquiryOrderId(inquiryOrderId);
		}
		// }

		JSONArray sellerList = inquiry.getJSONArray("sellerList");

		for (int i = 0; i < sellerList.size(); i++) {
			JSONObject seller = (JSONObject) sellerList.get(i);

			InquiryOrderSellers inquiryOrderSellers = new InquiryOrderSellers(inquiryOrderId,
					"null".equals(seller.getString("sellerId")) && StringUtils.isNotBlank(seller.getString("sellerId"))
							? null : Long.parseLong(seller.getString("sellerId")),
					seller.getString("sellerName"), loginId, loginId);

			inquiryOrderSellers.setContactId("null".equals(seller.getString("contactId"))
					&& StringUtils.isNotBlank(seller.getString("contactId")) ? null
							: Long.parseLong(seller.getString("contactId")));
			/**
			 * 屏蔽报价时，保存询价单，但是在填写报价单时，把屏蔽报价的
			 */
			// if ("true".equals(seller.getString("isSaveSeller"))) {

			inquiryOrderSellersDao.insertSelective(inquiryOrderSellers);
			// }

			JSONArray items = JSONArray.fromObject(seller.get("itemList"));
			saveItems(items, inquiryOrderSellers, user, purchaseOrderId);
		}
		return inquiryOrderId;
	}

	/**
	 * 保存询价单明细
	 * 
	 * @param items
	 * @param inquiryOrderSellers
	 * @param user
	 * @param purchaseOrderId
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void saveItems(JSONArray items, InquiryOrderSellers inquiryOrderSellers, User user, Long purchaseOrderId) {
		String loginId = user.getLoginId();
		List<String> condition = new ArrayList<String>();
		for (int j = 0; j < items.size(); j++) {
			JSONObject item = (JSONObject) items.get(j);

			// 品名材质规格钢厂仓库记重方式一样的数据，过滤掉，不用重复保存。
			String conditionStr = item.getString("categoryUuid") + "," + item.getString("materialUuid") + ","
					+ item.getString("normsName") + "," + item.getString("factoryId") + ","
					+ item.getString("warehouseId") + "," + item.getString("weightConcept");
			if (condition.contains(conditionStr)) {// 如果品名材质规格钢厂仓库记重方式相同则跳过本条数据，不用保存。
				continue;
			} else {
				condition.add(conditionStr);
			}

			BigDecimal price = new BigDecimal(item.getString("price"));
			BigDecimal weight = new BigDecimal(item.getString("resultWeight"));
			int quantity;
			try {
				quantity = Integer.parseInt(item.getString("resultQuantity"));
			} catch (NumberFormatException e) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "输入的数量必须为整数");
			}

			// 保存资源表
			com.prcsteel.platform.smartmatch.model.model.Resource resource = saveResource(item,
					inquiryOrderSellers.getAccountId(), user);

			// if("save".equals(item.getString("option")) &&
			// !StringUtils.isEmpty(item.getString("warehouseId"))){

			InquiryOrderItems inquiryOrderItems = new InquiryOrderItems(
					"null".equals(item.getString("purchaseOrderItemId"))
							&& StringUtils.isNotBlank(item.getString("purchaseOrderItemId")) ? null
									: Long.parseLong(item.getString("purchaseOrderItemId")),
					inquiryOrderSellers.getId(), item.getString("categoryUuid"), item.getString("materialUuid"),
					item.getString("normsName"),
					StringUtils.isEmpty(item.getString("factoryId"))
							&& StringUtils.isNotBlank(item.getString("factoryId")) ? null
									: Long.parseLong(item.getString("factoryId")),
					resource == null ? null : resource.getWarehouseId(),
					resource != null && StringUtils.isEmpty(item.getString("warehouseId"))
							? item.getString("warehouseName") : null,
					quantity, price, weight, loginId, loginId,
					"null".equals(item.getString("weightConcept")) ? null : item.getString("weightConcept"),
					resource == null ? null : resource.getId());

			InquiryHistory inquiryHistory = new InquiryHistory(purchaseOrderId, inquiryOrderSellers.getInquiryOrderId(),
					"null".equals(item.getString("purchaseOrderItemId"))
							&& StringUtils.isNotBlank(item.getString("purchaseOrderItemId")) ? null
									: Long.parseLong(item.getString("purchaseOrderItemId")),
					item.getString("categoryUuid"), item.getString("categoryName"), item.getString("materialUuid"),
					item.getString("materialName"), item.getString("normsName"),
					StringUtils.isEmpty(item.getString("factoryId"))
							&& StringUtils.isNotBlank(item.getString("factoryId")) ? null
									: Long.parseLong(item.getString("factoryId")),
					item.getString("factoryName"), resource == null ? null : resource.getWarehouseId(),
					item.getString("warehouseName"), quantity, weight, price, loginId, loginId);
			//add by caosulin 20160902 增加品名，材质，钢厂，仓库冗余字段
			inquiryOrderItems.setCategoryName(item.getString("categoryName"));
			inquiryOrderItems.setMaterialName(item.getString("materialName"));
			inquiryOrderItems.setFactoryName(item.getString("factoryName"));
			inquiryOrderItems.setWarehouseName(item.getString("warehouseName"));
			
			inquiryOrderItemsDao.insertSelective(inquiryOrderItems);
			inquiryHistoryDao.insertSelective(inquiryHistory);
			// }

		}

	}

	/**
	 * 保存资源表1.保存询价单时保存资源 2.填写报价单时，可以修改报价
	 * 
	 * @param item
	 * @param accountId
	 * @param user
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public com.prcsteel.platform.smartmatch.model.model.Resource saveResource(JSONObject item, Long accountId,
			User user) {
		String loginId = user.getLoginId();
		List<CategoryNorms> list = categoryNormsDao.getNormCombineByCategoryUuid(item.getString("categoryUuid"));
		String[] spec = item.getString("normsName").split("\\" + Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR);
		String specStr = item.getString("normsName");
		String userId = user.getId().toString();
		com.prcsteel.platform.smartmatch.model.model.Resource returnResource = null;
		String categoryUuid = item.getString("categoryUuid");
		String materialUuid = item.getString("materialUuid");
		Long warehouseId = StringUtils.isEmpty(item.getString("warehouseId")) ? null
				: Long.parseLong(item.getString("warehouseId"));
		Long factoryId = StringUtils.isEmpty(item.getString("factoryId")) ? null
				: Long.parseLong(item.getString("factoryId"));
		Date now = new Date();
		BigDecimal price = new BigDecimal(item.getString("price"));
		if ("save".equals(item.getString("option"))) {
			/**
			 * 规格属性大于三时，只需要填三个就可以；规格属性小于等于3时，必须匹配
			 */
			if (list.size() > MAX_SPEC_LENGTH) {
				if (spec.length < MAX_SPEC_LENGTH) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "规格填写不正确(规格长度应为3)");
				} else if (spec.length > list.size()) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "规格填写不正确(规格长度应为" + list.size() + ")");
				}
			} else {
				if (list.size() != spec.length) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "规格填写不正确(规格长度应为" + list.size() + ")");
				}
			}

			List<String> uuidList = list.stream().map(a -> a.getNormsUuid()).collect(Collectors.toList());

			ResourceQuery resourceQuery = new ResourceQuery(accountId, item.getString("categoryUuid"),
					item.getString("materialUuid"), null,
					"null".equals(item.getString("factoryId")) && StringUtils.isNotBlank(item.getString("factoryId"))
							? null : Long.parseLong(item.getString("factoryId")),
					null, warehouseId, item.getString("warehouseName"), null, item.getString("normsName"));
			resourceQuery.setSourceType(ResourceSourceType.INQUIRY.getCode());
			resourceQuery.setWeightConcept(item.getString("weightConcept"));
			resourceQuery.setCityName(item.getString("cityName"));

			List<ResourceDto> resourceDtoList = resourceService.selectByQueryForUpdate(resourceQuery);
			// 资源存在，更新资源
			if (resourceDtoList.size() >= 1) {
				ResourceDto resourceDto = resourceDtoList.get(0);
				// 数据库里的用户id集
				String u_ids = resourceDto.getUserIds();
				// 不包含当前用户则添加
				long count = Arrays.asList(u_ids.split(",")).stream().filter(e -> e.equals(userId)).count();
				if (count <= 0) {
					u_ids += "," + userId;
				}

				com.prcsteel.platform.smartmatch.model.model.Resource resource = new com.prcsteel.platform.smartmatch.model.model.Resource(
						accountId, resourceDto.getAccountName(), categoryUuid, resourceDto.getCategoryName(),materialUuid,  factoryId, warehouseId, item.getString("weightConcept"),
						new BigDecimal(item.getString("price")), null, loginId,
						Integer.parseInt(item.getString("resultQuantity")),
						new BigDecimal(item.getString("resultWeight")), null, ResourceSourceType.INQUIRY.getCode(), "1",
						u_ids, item.getString("factoryName"), item.getString("warehouseName"), null, null);
				resource.setCityName(item.getString("cityName"));
				resource.setCityId(StringUtils.isNotBlank(item.getString("cityId"))
						? Long.parseLong(item.getString("cityId")) : null);
				resource.setId(resourceDto.getId());
				resource.setCreated(now);
				resource.setCreatedBy(user.getName());
				resource.setStatus(ResourceStatus.APPROVED.getCode());
				
				resource.setMgtLastUpdated(now);
				resource.setMgtLastUpdatedBy(user.getLoginId());
				if (checkResourceException(resource)) {
					resource.setIsException(ResourceException.EXCEPTION.getCode());
				} else {
					resource.setIsException(ResourceException.NORMAL.getCode());
				}
				boolean validResource = true;
				if (price != null && StringUtils.isNotEmpty(categoryUuid)) {
					Category category = categoryDao.queryByCategoryUuid(categoryUuid);
					if (category != null && (price.compareTo(category.getPriceMin()) < 0
							|| price.compareTo(category.getPriceMax()) > 0)) { // 如果价格不在参考价范围，不更新
						validResource = false;
					}
				}
				if (validResource) {
					// add by caosulin 如果城市ID或者name为空，根据仓库ID重新查一遍
					resourceService.getResourceCity(resource);

					resourceDao.updateByPrimaryKeySelective(resource);
					insertResourceBase(resource, ResourceOperType.UPDATE, spec, uuidList);
					try {
						resourceService.saveBaseProductByResource(resource, specStr);// 保存到资源产品表
					} catch (Exception e1) {
						logger.error("--------保存到产品表失败:accountName=" + e1);
					}
				} else {
					return resource;
				}
				returnResource = resource;

			} else if (resourceDtoList.size() == 0) { // 资源不存在，新增资源
				com.prcsteel.platform.smartmatch.model.model.Resource resource = new com.prcsteel.platform.smartmatch.model.model.Resource(
						accountId, null, item.getString("categoryUuid"),"", item.getString("materialUuid"),
						"null".equals(item.getString("factoryId"))
								&& StringUtils.isNotBlank(item.getString("factoryId")) ? null
										: Long.parseLong(item.getString("factoryId")),
						warehouseId, item.getString("weightConcept"), new BigDecimal(item.getString("price")), loginId,
						loginId, Integer.parseInt(item.getString("resultQuantity")),
						new BigDecimal(item.getString("resultWeight")), null, ResourceSourceType.INQUIRY.getCode(), "1",
						user.getId().toString(), item.getString("factoryName"), item.getString("warehouseName"), null,
						null);
				if (checkResourceException(resource)) {
					resource.setIsException(ResourceException.EXCEPTION.getCode());
				} else {
					resource.setIsException(ResourceException.NORMAL.getCode());
				}
				try {
					resource.setAccountName(((RestAccountService) this.getRestService("smart_restAccountService")).selectAccountById(resource.getAccountId()).getName());
				} catch (Exception ex) {
					logger.error("保存资源客户不存在");
				}
				resource.setCategoryName(categoryDao.queryByCategoryUuid(resource.getCategoryUuid()).getName());
				resource.setMaterialName(item.getString("materialName"));
				resource.setCityName(item.getString("cityName"));
				resource.setCityId(StringUtils.isNotEmpty(item.getString("cityId"))
						? Long.parseLong(item.getString("cityId")) : null);
				resource.setStatus(ResourceStatus.APPROVED.getCode());
				resource.setMgtLastUpdated(now);
				resource.setMgtLastUpdatedBy(user.getLoginId());
				resource.setCreated(now);
				resource.setCreatedBy(user.getName());
				boolean validResource = true;
				Category category=null;
				if(StringUtils.isNotEmpty(categoryUuid)) {
					category= categoryDao.queryByCategoryUuid(categoryUuid);
					if(category!=null) {
						resource.setCategoryName(category.getName());
					}
				}
				if (price != null){
					if (category != null && (price.compareTo(category.getPriceMin()) < 0
							|| price.compareTo(category.getPriceMax()) > 0)) { // 如果价格不在参考价范围，不更新
						validResource = false;
					}
				}
				if (validResource) {
					resourceService.insertSelective(resource);
					insertResourceBase(resource, ResourceOperType.INSERT, spec, uuidList);
					try {
						resourceService.saveBaseProductByResource(resource, specStr);// 保存到资源产品表
					} catch (Exception e) {
						logger.error("--------保存到产品表失败:accountName=" + e);
					}
				} else {
					return resource;
				}
				returnResource = resource;

				ResourceNorms resourceNorms = new ResourceNorms();
				resourceNorms.setCreatedBy(loginId);
				resourceNorms.setResourceId(resource.getId());
				for (int k = 0; k < spec.length; k++) {
					resourceNorms.setNormUuid(uuidList.get(k));
					resourceNorms.setValue(spec[k]);
					resourceNorms.setPriority(k + 1);
					resourceNormsDao.insertSelective(resourceNorms);
				}
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "传入的参数不全，保存资源失败，请重新输入再保存");
			}
		}
		return returnResource;
	}

	/**
	 * 插入基础资源表，新增一条资源记录
	 * 
	 * @param spec
	 * @param uuidList
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void insertResourceBase(com.prcsteel.platform.smartmatch.model.model.Resource res,
			ResourceOperType operType, String[] spec, List<String> uuidList) {
		if (ResourceStatus.DECLINED.getCode().equals(res.getStatus())) {
			// 只有已审核的才保存操作记录
			return;
		}
		ResourceBase base = null;
		try {
			base = new ResourceBase();
			BeanUtils.copyProperties(base, res);
			base.setId(null);
			base.setCustResourceId(res.getId());
			base.setOperType(operType.getCode());
			resourceBaseDao.insertSelective(base);
			Date dt = new Date();
			ResourceNormsBase resourceNorms = new ResourceNormsBase();
			resourceNorms.setCreatedBy(res.getCreatedBy());
			resourceNorms.setCreated(dt);
			resourceNorms.setLastUpdatedBy(res.getCreatedBy());
			resourceNorms.setLastUpdated(dt);
			resourceNorms.setResourceId(base.getId());
			for (int k = 0; k < spec.length; k++) {
				resourceNorms.setNormUuid(uuidList.get(k));
				resourceNorms.setValue(spec[k]);
				resourceNorms.setPriority(k + 1);
				resourceNormsBaseDao.insertSelective(resourceNorms);
			}
		} catch (Exception e) {
			logger.error("===========================" + e);
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存资源新增记录异常");
		}
	}

	private boolean checkResourceException(com.prcsteel.platform.smartmatch.model.model.Resource resource) {
		if (resource.getFactoryId() == null || resource.getWarehouseId() == null) {
			return true;// 异常
		}
		return false;// 正藏
	}

	@Override
	public List<BillHistoryDataDto> selectBillHistoryByAccountId(Long accountId) {
		return purchaseOrderDao.selectBillHistoryByAccountId(accountId);
	}

	/**
	 * 找货2.0智能匹配， added by yjx
	 * 
	 * 
	 * @Description: 智能匹配
	 * @param purchaseOrderId
	 *            采购单号
	 * @param notSpecificSellerId
	 *            指定不搜索的卖家列表
	 * @param specificSellerId
	 *            指定搜索某个卖家
	 * 
	 * @param isAppend
	 *            是否是点开后补充查询的
	 */
	@Override
	public SearchResultDtoTwo match2(Long purchaseOrderId, List<Long> purchaseOrderItemId,
			List<Long> notSpecificSellerId, Long specificSellerId, Integer isAppend, Long cityId) {

		/**
		 * 1、匹配模式： 指定卖家，补充匹配，匹配 。a. 指定卖家：匹配结果中，当前城市的指定卖家提到最前面 。b.
		 * 补充匹配：点击一个卖家以后，如果当前卖家的资源不能满足所有的询价详情，那么会将包含当前卖家所没有的询价详情资源的卖家按照匹配的规则补充进来
		 * （已经询价过的卖家不允许补充匹配 ,最多显示三级，补充匹配最多显示两家）。
		 * c.匹配：（1）、匹配的地区显示顺序：交货地——交货地映射的中心城市——其它 （2）卖家排列顺序：
		 * 已询价>包销返佣>银票预付>白名单>满足率>历史成交时间近的优先；
		 */

		// 匹配结果结构： -------------------------
		// -- List<SearchCityCompaniesDto>:
		// |
		// v
		// --------------------cpCount 当前城市的公司数量
		// --------------------City 城市信息
		// --------------------List<SearchCompanyResultDto> 匹配的公司结果集合
		// |
		// v
		// -----------------------------公司的基础信息 +List<SearchItermResultDto>
		// |
		// v
		// -----------------------------PurchaseOrderItemsDtoTwo +
		// List<SearchResourceDtoTwo>
		// | |
		// v v
		// 询价详情的基本信息 询价详情所匹配的资源列表
		// 结果集
		SearchResultDtoTwo resultDto = new SearchResultDtoTwo();

		if (specificSellerId != null) {
			/**
			 * 有指定的卖家
			 */
			List<SearchCityCompaniesDto> cplist = new ArrayList<SearchCityCompaniesDto>();
			resultDto.setInquiryList(cplist);
			// 根据指定卖家的ID获取指定的卖家
			Account seller = accountService.queryById(specificSellerId);
			// 指定卖家查询不到，或者不在当前城市中，返回
			if (seller == null) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "指定卖家并不存在");
			}
			// 公司列表
			List<SearchCompanyResultDto> cpResultList = new ArrayList<SearchCompanyResultDto>();
			cpResultList.add(iniCpResult(seller));
			City city = cityDao.selectByPrimaryKey(seller.getCityId());
			cplist.add(new SearchCityCompaniesDto(city, cpResultList));
			List<PurchaseOrderItemsDtoTwo> itemList;
			if (purchaseOrderItemId == null || purchaseOrderItemId.size() == 0) {
				itemList = purchaseOrderItemsDao.getListByPOIdTwo(purchaseOrderId);
			} else {
				itemList = purchaseOrderItemsDao.getListByIdsTwo(purchaseOrderItemId);
			}
			List<Long> specificSellerIds = new ArrayList<Long>();
			specificSellerIds.add(seller.getId());
			// 匹配到的资源列表
			List<SearchResourceDtoTwo> resultDto2 = purchaseOrderItemsDao.queryMatchedResources(purchaseOrderId,
					specificSellerIds);
			itemList.stream().forEach(a -> a.setAttributeList(
					attributesDao.getAttributesByPurchaseOrderItemIdAndCategoryUuid(a.getCategoryUuid(), a.getId())));
			setResource(cpResultList, resultDto2, itemList, true);

			// 获取原来的老代码中生成的公司的数据对象
			SearchResultDtoTwo dto = null;
			if (!cpResultList.isEmpty()) {
				SearchCompanyResultDto specificSeller = cpResultList.get(0);
				specificSeller.setCityId(cityId);

				// 清空原来的查询结果
				resultDto.getInquiryList().clear();

				// 查询所有的卖家，并且指定卖家
				dto = getResourceMatchData(purchaseOrderId, purchaseOrderItemId, specificSeller, resultDto);
			}
			return dto;

		} else if (isAppend != null && isAppend == 1) {
			/**
			 * 补充匹配资源
			 */
			if (cityId == null || cityId < 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "城市信息缺失，无法补充匹配资源！");
			}
			List<City> cityList = new ArrayList<City>();
			City ct = new City();
			ct.setId(cityId);
			cityList.add(ct);
			List<SearchCompanyResultDto> companyList = purchaseOrderItemsDao
					.queryComplementMatchedCompanies(purchaseOrderId, purchaseOrderItemId, notSpecificSellerId, cityId);
			if (companyList == null || companyList.size() == 0) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没有匹配到缺失资源！");
			}
			/**
			 * 已询价的公司ID列表,补充匹配时，已询价的需要排除在外，而不是提前
			 */
			List<Long> inquiredCpIds = inquiryOrderSellersDao.selectInqueriredAccountIds(purchaseOrderId);

			/**
			 * 补充匹配时，移除已询价的公司
			 */
			companyList = removeInquired(companyList, inquiredCpIds);
			/**
			 * 移除已经询价过的公司以后，可能补充匹配的公司列表为空
			 */
			if (companyList == null || companyList.size() == 0) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没有匹配到缺失资源！");
			}
			List<Long> cpIds = new ArrayList<Long>();

			for (SearchCompanyResultDto cp : companyList) {
				cpIds.add(cp.getSellerId());
				cp.setContactList(purchaseOrderItemsDao.queryCompanyContacts(cp.getSellerId()));
			}
			// 查询到的资源列表
			List<SearchResourceDtoTwo> resultDto2 = purchaseOrderItemsDao.queryMatchedResources(purchaseOrderId, cpIds);
			List<PurchaseOrderItemsDtoTwo> itemList = purchaseOrderItemsDao.getListByPOIdTwo(purchaseOrderId);

			itemList.stream().forEach(a -> a.setAttributeList(
					attributesDao.getAttributesByPurchaseOrderItemIdAndCategoryUuid(a.getCategoryUuid(), a.getId())));
			setResource(companyList, resultDto2, itemList, false);
			setCompanies(resultDto, cityList, supplement(companyList, purchaseOrderItemId), inquiredCpIds);

			return resultDto;

		} else {
			/**
			 * 第一次匹配获取所有匹配结果
			 */
			SearchResultDtoTwo dto = getResourceMatchData(purchaseOrderId, purchaseOrderItemId, null, resultDto);
			return dto;
		}

	}

	/**
	 * 补充匹配，如果第一条满足率为1，则只取一条； 取到能满足需要补充匹配的公司所没有的所有item对应的资源时，不再补充后面匹配到的公司
	 * 
	 * @param cpList
	 * @return
	 */
	private List<SearchCompanyResultDto> supplement(List<SearchCompanyResultDto> cpList,
			List<Long> purchaseOrderItemId) {
		Map<Long, Long> map = new HashMap<Long, Long>();
		purchaseOrderItemId.forEach(a -> map.put(a, a));
		List<SearchCompanyResultDto> supplemtCpList = new ArrayList<SearchCompanyResultDto>();
		Map<Long, Long> unContainMap = new HashMap<Long, Long>();
		Integer contaiCount = 0;
		SearchCompanyResultDto en = null;
		if (cpList.size() > 0) {
			en = cpList.get(0);
			supplemtCpList.add(en);
			if (en.getRate() == 1) {// 第一条满足率为1
				return supplemtCpList;
			} else {// 或者第一条满足缺失的那几条资源时
				for (SearchItermResultDto item : en.getItermResourceList()) {
					if (map.containsKey(item.getItem().getId())) {
						if (item.getResourceList().size() > 0) {
							contaiCount++;// 包含的数量
						} else {
							unContainMap.put(item.getItem().getId(), item.getItem().getId());// 第一条除外的itemid
						}
					}
				}
				if (contaiCount == purchaseOrderItemId.size()) {
					return supplemtCpList;
				}
			}
		}
		contaiCount = 0;
		if (cpList.size() > 1) {// 从第二条开始匹配第一家公司不能满足的资源，匹配出其余公司中能满足第一家所不能满足的第一家时，返回
			for (int i = 1; i < cpList.size(); i++) {
				en = cpList.get(i);
				for (SearchItermResultDto item : en.getItermResourceList()) {
					if (unContainMap.containsKey(item.getItem().getId()) && item.getResourceList().size() > 0) {
						contaiCount++;// 包含的数量
					}
				}
				if (contaiCount == unContainMap.size()) {
					supplemtCpList.add(en);
					break;
				}
			}
		}

		return supplemtCpList;
	}

	/**
	 * 获取匹配的资源公用方法
	 * 
	 * @param purchaseOrderId
	 * @param purchaseOrderItemId
	 * @param specificSellerId
	 * @param resultDto
	 * @return
	 */
	private SearchResultDtoTwo getResourceMatchData(Long purchaseOrderId, List<Long> purchaseOrderItemId,
			SearchCompanyResultDto specificSeller, SearchResultDtoTwo resultDto) {
		// 有资源的城市列表
		List<City> cityList = purchaseOrderItemsDao.queryMatchedCitys(purchaseOrderId);
		if (cityList == null || cityList.isEmpty()) {
			return resultDto;
		} // 查询订单
		PurchaseOrder po = checkPurchaseOrderId(purchaseOrderId);
		cityList = sortCity(cityList, po);
		// 已询价>包销返佣>(银票预付)>(白名单)>满足率>(历史成交时间近的优先): 包销返佣字段暂时不考虑，后续可能需要加上
		// 匹配到的公司列表，已按银票预付，白名单、历史成交时间最近排序；后续需要根据是否已询价，以满足率来排序
		List<SearchCompanyResultDto> companyList = purchaseOrderItemsDao.queryMatchedCompanies(purchaseOrderId, null,
				null, null);

		List<Long> cpIds = new ArrayList<Long>();
		for (SearchCompanyResultDto cp : companyList) {
			cpIds.add(cp.getSellerId());
			// cp.setContactList(purchaseOrderItemsDao.queryCompanyContacts(cp.getSellerId()));
		}

		// 匹配查询到的资源结果列表
		List<SearchResourceDtoTwo> resultDto2 = purchaseOrderItemsDao.queryMatchedResources(purchaseOrderId, cpIds);

		/**
		 * 获取询价单详情信息
		 */
		List<PurchaseOrderItemsDtoTwo> itemList;
		if (purchaseOrderItemId == null || purchaseOrderItemId.size() == 0) {
			itemList = purchaseOrderItemsDao.getListByPOIdTwo(purchaseOrderId);
		} else {
			itemList = purchaseOrderItemsDao.getListByIdsTwo(purchaseOrderItemId);
		}
		// 设置询价详情的扩展属性
		itemList.stream().forEach(a -> a.setAttributeList(
				attributesDao.getAttributesByPurchaseOrderItemIdAndCategoryUuid(a.getCategoryUuid(), a.getId())));

		/**
		 * 设置公司的数据结构，同时计算公司的资源满足率；
		 */
		setResource(companyList, resultDto2, itemList, false);
		/**
		 * 已询价的公司ID列表
		 */
		List<Long> inquiredCpIds = inquiryOrderSellersDao.selectInqueriredAccountIds(purchaseOrderId);
		/**
		 * 设置公司与城市的对应关系，并将已询价的公司放在当前城市的最前面，并且根据资源满足率 将当前城市的公司按满足率进行排序
		 */
		setCompanies(resultDto, cityList, companyList, inquiredCpIds);

		// 添加指定的卖家
		if (specificSeller != null) {
			// 判断当前卖家是否已存在，且把指定卖家放到城市的最前面
			for (SearchCityCompaniesDto currcityCompanies : resultDto.getInquiryList()) {
				City currcity = currcityCompanies.getCity();
				if (currcity != null) {
					// 当前的城市
					if (currcity.getId().equals(specificSeller.getCityId())) {
						// 城市下面的卖家
						if (currcityCompanies.getCompamies() != null) {
							boolean exists = false;
							for (int i = 0; i < currcityCompanies.getCompamies().size(); i++) {
								SearchCompanyResultDto rDto = currcityCompanies.getCompamies().get(i);
								// 判断如果已经存在，则把位置放到第一
								if (rDto.getSellerId().equals(specificSeller.getSellerId())) {
									SearchCompanyResultDto first = currcityCompanies.getCompamies().get(0);
									currcityCompanies.getCompamies().set(0, rDto);
									currcityCompanies.getCompamies().set(i, first);
									exists = true;
								}
							}
							if (!exists) {
								currcityCompanies.getCompamies().add(0, specificSeller);
							}

						}
					}
				}
			}
		}

		return resultDto;
	}

	/**
	 * 指定卖家时，生成SearchCompanyResultDto对象
	 * 
	 * @param seller
	 * @param specificSellerId
	 * @param purchaseOrderId
	 * @return
	 */
	private SearchCompanyResultDto iniCpResult(Account seller) {
		SearchCompanyResultDto dto = new SearchCompanyResultDto();
		dto.setCityId(seller.getCityId());
		dto.setSellerId(seller.getId());
		dto.setSellerName(seller.getName());
		dto.setPayMentLaybel(seller.getPaymentLabel());
		dto.setSupplierLabel(seller.getSupplierLabel());
		return dto;
	}

	/**
	 * 设置公司-》资源对应
	 * 
	 * 1 、更新价格：如果询价资源的价格时在当天时间内的，则显示；如果不是在当天的时间内的，则不显示 。2、询价价格：
	 * 如果询价资源的价格时在当天时间内的，则显示；如果不是在当天的时间内的，则不显示。3 、 历史成交价格： 历史成交资源的价格
	 * 
	 * @param resultDto
	 * @param cityList
	 * @param compamies
	 */
	private void setResource(List<SearchCompanyResultDto> compamies, List<SearchResourceDtoTwo> resourceList,
			List<PurchaseOrderItemsDtoTwo> itemList, boolean isSpecific) {
		// 获取当天的起始时间与终止时间，用于判断询价资源日常资源是否为当天的价格，如果不是当天的价格不做显示
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date start = cal.getTime();// 获取当天0:0:0的时间，用于过滤更新（日常资源）价格，询价价格只显示当天的 价格

		// 当前公司下的资源列表集
		List<SearchResourceDtoTwo> cpResList = null;
		double totalItems = itemList.size();
		BigDecimal currPrice = null;
		int currCount = 0;// 资源满足的条数，用于计算资源满足率
		boolean itemCounted = false;
		List<SearchItermResultDto> itemResList = null;
		/**
		 * 三层数据结构`````````````````````````````````````````````````````
		 * |-公司层数据SearchCompanyResultDto ``````````````````````````````
		 * |---对应的询价单详情item SearchItermResultDto``````````````````````
		 * |-----item对应的resourceList SearchResourceDtoTwo````````````````
		 */
		// 循环遍历所有有询价单匹配资源的公司列表
		for (SearchCompanyResultDto currComp : compamies) {
			itemResList = new ArrayList<SearchItermResultDto>();
			// 遍历所有询价单详情，
			for (PurchaseOrderItemsDtoTwo item : itemList) {
				cpResList = new ArrayList<SearchResourceDtoTwo>();
				itemCounted = false;
				// 遍历所有资源,如果该条资源数据当前的公司，添加到询价单详情对应的资源列表中
				for (SearchResourceDtoTwo res : resourceList) {

					if (isSpecific) {
						// 指定卖家 不匹配资源所在城市
						if (res.getItemId().equals(item.getId()) && res.getAccountId().equals(currComp.getSellerId())) {
							// 根据类型设置价格
							currPrice = res.getPrice();
							res.setPrice(null);
							if (ResourceSourceType.DAILY_COMMON.getCode().equals(res.getSourceType())) {
								if (res.getLastUpdateTime() != null && res.getLastUpdateTime().after(start)) {
									// 如果是当天的日常资源，则设置日常价格，否则设置默认为空
									res.setUpdatePrice(currPrice);
								}

							} else if (ResourceSourceType.HISTORY_TRANSACTION.getCode().equals(res.getSourceType())) {
								res.setHistoryPrice(currPrice);
							} else if (ResourceSourceType.INQUIRY.getCode().equals(res.getSourceType())) {
								if (res.getLastUpdateTime() != null && res.getLastUpdateTime().after(start)) {
									// 如果是当天的日常资源，则设置日常价格，否则设置默认为空
									res.setPrice(currPrice);
								}
							}
							cpResList.add(res);
							if (!itemCounted) {
								// 用于计算资源满足率
								currCount++;
								itemCounted = true;
							}
						}
					} else {
						// 一家公司在多个仓库有货时，作区分
						if (res.getItemId().equals(item.getId()) && res.getAccountId().equals(currComp.getSellerId())) {
							if (res.getCityId() != null && res.getCityId().equals(currComp.getCityId())) {// 资源的city可能为空
								// 根据类型设置价格
								currPrice = res.getPrice();
								res.setPrice(null);
								if (ResourceSourceType.DAILY_COMMON.getCode().equals(res.getSourceType())) {
									if (res.getLastUpdateTime() != null && res.getLastUpdateTime().after(start)) {
										// 如果是当天的日常资源，则设置日常价格，否则设置默认为空
										res.setUpdatePrice(currPrice);
									}

								} else if (ResourceSourceType.HISTORY_TRANSACTION.getCode()
										.equals(res.getSourceType())) {
									res.setHistoryPrice(currPrice);
								} else if (ResourceSourceType.INQUIRY.getCode().equals(res.getSourceType())) {
									if (res.getLastUpdateTime() != null && res.getLastUpdateTime().after(start)) {
										// 如果是当天的日常资源，则设置日常价格，否则设置默认为空
										res.setPrice(currPrice);
									}
								}
								cpResList.add(res);
								if (!itemCounted) {
									// 用于计算资源满足率
									currCount++;
									itemCounted = true;
								}
							}

						}
					}

				}
				// 去除重复资源，品名规格材质等相同的日常资源询价资源历史成交资源合并成一条显示，并将价格设置到相应的日常价格、询价价格、历史成交价格字段中
				cpResList = removeRepeatRes(cpResList);
				// item，resourceList关联 ，询价单详情与对应的资源集合数据
				itemResList.add(new SearchItermResultDto(item, cpResList));

			}
			// 设置公司下的询价单详情
			currComp.setItermResourceList(itemResList);
			// 计算满足率
			currComp.setRate(currCount / totalItems);
			currCount = 0;
		}

		/**
		 * 列好以后，再根据（1）已询价；（2）包销返佣；（3）银票预付；（4）白名单；（5）满足率；（6）历史成交时间近的优先的顺序排序
		 * 查询时按（3）银票预付；（4）白名单；（6）历史成交时间近的优先的顺序排序排序，
		 * 比较银票预付与白名单，相同时满足率高的冒泡，不同的不做排序。排序后在白名单银票预付的基础上根据满足率对公司进行了排序。
		 */
		SearchCompanyResultDto c1, c2;
		for (int i = 0; i < compamies.size(); i++) {
			for (int j = compamies.size() - 1; j > i; j--) {
				c1 = compamies.get(i);
				c2 = compamies.get(j);
				if (c1.getSupplierLabel().equals(c2.getSupplierLabel())
						&& c1.getPayMentLaybel().equals(c2.getPayMentLaybel()) && c1.getRate() < c2.getRate()) {
					// 比较银票预付与白名单，相同时满足率高的冒泡，相同时，将资源进行冒泡
					compamies.set(i, c2);
					compamies.set(j, c1);
				}
			}
		}
	}

	/**
	 * 设置重复资源的价格,公司包含的资源做循环去重，并根据数据类型设置价格
	 * 
	 * 这一段在设置公司与资源列表对应时执行，然后重新判断一下资源品名 材质相同时，去除一条资源并且将价格设置到相应的字段中
	 * 
	 */
	private List<SearchResourceDtoTwo> removeRepeatRes(List<SearchResourceDtoTwo> resources) {
		if (resources == null || resources.size() < 2) {
			return resources;
		}

		SearchResourceDtoTwo resI, resJ;
		List<Integer> remove = new ArrayList<Integer>();// 重复资源下标
		for (int i = 0; i < resources.size() - 1; i++) {
			resI = resources.get(i);
			for (int j = resources.size() - 1; j > i; j--) {
				resJ = resources.get(j);
				/**
				 * 公司+钢厂+品名+材质+规格+仓库+记重+资源类型相同时，视为一条资源， 去重时：
				 * 钢厂+品名+材质+规格+仓库+记重（不同资源类型显示在同一条，但是价格按照资源类型显示）
				 */
				if ((resI.getCategoryUuid() != null && resI.getCategoryUuid().equals(resJ.getCategoryUuid()))
						&& (resI.getMaterialUuid() != null && resI.getMaterialUuid().equals(resJ.getMaterialUuid())
								&& (resI.getSpec() != null && resI.getSpec().equals(resJ.getSpec()))
								&& (resI.getFactoryId() != null && resI.getFactoryId().equals(resJ.getFactoryId()))
								&& (resI.getWarehouseId() != null
										&& resI.getWarehouseId().equals(resJ.getWarehouseId()))
								&& (resI.getWeightConcept() != null
										&& resI.getWeightConcept().equalsIgnoreCase(resJ.getWeightConcept())))) {
					remove.add(j);// 讲重复的放入列表
					if ((resI.getPrice() == null && resJ.getPrice() != null)
							|| (resI.getPrice() != null && resJ.getPrice() != null
									&& resI.getPrice().doubleValue() > resJ.getPrice().doubleValue())) {
						// 询价价格
						resI.setPrice(resJ.getPrice());
					}

					if ((resI.getUpdatePrice() == null && resJ.getUpdatePrice() != null)
							|| (resI.getUpdatePrice() != null && resJ.getUpdatePrice() != null
									&& resI.getUpdatePrice().doubleValue() > resJ.getUpdatePrice().doubleValue())) {
						// 设置日常资源价格
						resI.setUpdatePrice(resJ.getUpdatePrice());
					}
					if ((resI.getHistoryPrice() == null && resJ.getHistoryPrice() != null)
							|| (resI.getHistoryPrice() != null && resJ.getHistoryPrice() != null
									&& resI.getHistoryPrice().doubleValue() > resJ.getHistoryPrice().doubleValue())) {
						// 历史成交价格及成交时间，前端使用lastupdate作为历史成交价格的时间
						resI.setHistoryPrice(resJ.getHistoryPrice());
						resI.setLastUpdateTime(resJ.getLastUpdateTime());
					}

				}
			}
		}

		HashSet<Integer> h = new HashSet<Integer>(remove);

		List<SearchResourceDtoTwo> fList = new ArrayList<SearchResourceDtoTwo>();
		if (h.size() > 0) {
			for (int i = 0; i < resources.size(); i++) {
				if (!h.contains(i)) {
					fList.add(resources.get(i));
				}
			}
		} else {
			fList = resources;
		}

		return fList;
	}

	/**
	 * 设置城市-》公司对应； 已经询价的工资放在当前城市的最前面
	 * 
	 * @param resultDto
	 * @param cityList
	 *            城市列表
	 * @param compamies
	 *            排序后的公司列表
	 * @param inquiredCpIds
	 *            已经询价的公司
	 */
	private void setCompanies(SearchResultDtoTwo resultDto, List<City> cityList, List<SearchCompanyResultDto> compamies,
			List<Long> inquiredCpIds) {

		List<SearchCompanyResultDto> currcityCompanies = new ArrayList<SearchCompanyResultDto>();

		// 遍历城市列表
		for (City currcity : cityList) {
			currcityCompanies = new ArrayList<SearchCompanyResultDto>();
			if (currcity != null) {
				// 遍历公司列表，当前公司属于该城市时，设置到当前城市的公司列表中
				for (SearchCompanyResultDto comp : compamies) {
					if (currcity.getId().equals(comp.getCityId())) {
						currcityCompanies.add(comp);
					}
				}
			}
			// 当前城市的公司列表+已询价的公司列表，如果当前城市有已询价的公司，将已询价的公司放到最前面
			currcityCompanies = setInquiredFirst(currcityCompanies, inquiredCpIds);
			// 设置城市与公司对应
			resultDto.getInquiryList().add(new SearchCityCompaniesDto(currcity, currcityCompanies));
		}
	}

	/**
	 * 把询价过的公司提到最前
	 * 
	 * @param currcityCompanies
	 * @param inquiredCpIds
	 * @return
	 */
	private List<SearchCompanyResultDto> setInquiredFirst(List<SearchCompanyResultDto> currcityCompanies,
			List<Long> inquiredCpIds) {

		if (inquiredCpIds == null || inquiredCpIds.size() == 0) {
			return currcityCompanies;
		}
		List<SearchCompanyResultDto> inquiredList = new ArrayList<SearchCompanyResultDto>();
		List<SearchCompanyResultDto> otherList = new ArrayList<SearchCompanyResultDto>();

		Map<Long, Long> map = new HashMap<Long, Long>();
		inquiredCpIds.forEach(a -> map.put(a, a));

		for (SearchCompanyResultDto en : currcityCompanies) {
			if (map.containsKey(en.getSellerId())) {
				en.setIsInquired(1);// 设置公司是否已询价字段，前台显示时展示出来
				inquiredList.add(en);
			} else {
				otherList.add(en);
			}
		}
		if (inquiredList.size() > 0) {
			inquiredList.addAll(otherList);
			currcityCompanies = inquiredList;
		}
		return currcityCompanies;
	}

	/**
	 * 移除已询价的公司，补充匹配时候过滤已经询价的公司
	 * 
	 * @param currcityCompanies
	 * @param inquiredCpIds
	 * @return
	 */
	private List<SearchCompanyResultDto> removeInquired(List<SearchCompanyResultDto> currcityCompanies,
			List<Long> inquiredCpIds) {

		if (inquiredCpIds == null || inquiredCpIds.size() == 0) {
			return currcityCompanies;
		}
		/**
		 * 将已询价的公司从补充匹配的公司列表中移除
		 */
		List<SearchCompanyResultDto> unInquiredList = new ArrayList<SearchCompanyResultDto>();

		Map<Long, Long> map = new HashMap<Long, Long>();
		inquiredCpIds.forEach(a -> map.put(a, a));

		for (SearchCompanyResultDto en : currcityCompanies) {
			if (!map.containsKey(en.getSellerId())) {
				unInquiredList.add(en);
			}
		}

		return unInquiredList;
	}

	/**
	 * 获取匹配的城市列表
	 */
	@Override
	public List<City> matchCities(Long purchaseOrderId) {
		List<City> cityList = purchaseOrderItemsDao.queryMatchedCitys(purchaseOrderId);
		if (cityList == null || cityList.isEmpty()) {
			return null;
		} // 查询订单
		PurchaseOrder po = checkPurchaseOrderId(purchaseOrderId);
		cityList = sortCity(cityList, po);
		return cityList;
	}

	/**
	 * 按采购城市排序，有当前城市先按当前城市查询，无当前城市，查询当前城市的中心城市列表，讲中心城市列表放在最前，多个中心城市的顺序随意
	 * 
	 * @param cityList
	 * @param po
	 */
	private List<City> sortCity(List<City> cityList, PurchaseOrder po) {
		Map<Long, City> map = new HashMap<Long, City>();

		for (City en : cityList) {
			if (en != null) {
				map.put(en.getId(), en);
			}
		}

		List<City> currLi = null;
		// 交货地城市不管存在不存在都需要把中心城市放最前面，多个中心城市位置随意，但是要在其他城市前面
		List<City> centerCities = purchaseOrderItemsDao.findCenterCityIds(po.getDeliveryCityId());
		if (centerCities != null && centerCities.size() > 0) {
			currLi = new ArrayList<City>();
			for (City curr : centerCities) {
				if (map.containsKey(curr.getId()) && !po.getDeliveryCityId().equals(curr.getId())) {
					// 中心城市在资源匹配的城市列表中，且中心城市不是交货地城市
					currLi.add(curr);
					map.remove(curr.getId());
				}
			}

			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				currLi.add((City) map.get(it.next()));
			}

			cityList = currLi;
		}
		if (map.containsKey(po.getDeliveryCityId())) {
			// 有交货地城市的，将交货地城市放最先
			City curr = map.get(po.getDeliveryCityId());
			currLi = new ArrayList<City>();
			currLi.add(curr);
			cityList.remove(curr);
			currLi.addAll(cityList);
			cityList = currLi;
		}
		return cityList;
	}

	/**
	 * 获取公司联系人列表 added by yjx 2016.7.15
	 */
	@Override
	public List<SearchCompanyContactDto> queryCompanyContacts(Long accountId) {
		return purchaseOrderItemsDao.queryCompanyContacts(accountId);
	}

}
