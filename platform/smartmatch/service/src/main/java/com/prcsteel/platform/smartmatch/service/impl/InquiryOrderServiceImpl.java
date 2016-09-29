package com.prcsteel.platform.smartmatch.service.impl;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.DateUtil;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderDto;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderItemsDto;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderSatisfactionDto;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderSellersDto;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderItemsDtoTwo;
import com.prcsteel.platform.smartmatch.model.enums.InquirySelectOptions;
import com.prcsteel.platform.smartmatch.model.enums.PurchaseOrderStatus;
import com.prcsteel.platform.smartmatch.model.enums.QuotationOrderStatus;
import com.prcsteel.platform.smartmatch.model.model.InquiryOrderItems;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrder;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrderItems;
import com.prcsteel.platform.smartmatch.model.query.InquiryOrderQuery;
import com.prcsteel.platform.smartmatch.persist.dao.*;
import com.prcsteel.platform.smartmatch.service.InquiryOrderService;
import com.prcsteel.platform.smartmatch.service.SearchService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("inquiryOrderService")
public class InquiryOrderServiceImpl implements InquiryOrderService {
	@Resource
	private InquiryOrderDao inquiryOrderDao;
	@Resource
	private InquiryOrderItemsDao inquiryOrderItemsDao;
	@Resource
	private QuotationOrderDao quotationOrderDao;
	@Resource
	private QuotationOrderItemsDao quotationOrderItemsDao;
	@Resource
	private PurchaseOrderDao purchaseOrderDao;
	@Resource
	private SearchService searchService;
	@Resource
	private InquiryOrderItemsDao itemsDao;
	@Resource
	private UserDao userDao;
	@Resource
	private InquiryOrderSellersDao inquiryOrderSellersDao;
	@Resource
	private WarehouseDao warehouseDao;
	@Resource
	private PurchaseOrderItemsDao purchaseItemsDao;

	/**
	 * 通过三种选择条件异步获取系统推荐报价方式
	 * 
	 * @param id
	 * @param option
	 * @return
	 */
	@Override
	public List<InquiryOrderDto> selectByPurchaseOrderId(Long id, String option, String blockInquiryOrderSellerIds,
			String quotationOrderId) {
		List<InquiryOrderDto> orderDtos = new LinkedList<>();
		List<InquiryOrderItemsDto> dtos = inquiryOrderDao.selectByPurchaseOrderId(id, blockInquiryOrderSellerIds,
				quotationOrderId);
		if (dtos.size() == 0) {
			return null;
		}
		Map<Long, List<InquiryOrderItemsDto>> map = dtos.stream()
				.collect(Collectors.groupingBy(InquiryOrderItemsDto::getPurchaseOrderItemsId, Collectors.toList())); // 按需求的资源id分类;
		for (Long key : map.keySet()) {
			List<InquiryOrderItemsDto> list = map.get(key);
			BigDecimal allStock = BigDecimal.valueOf(list.stream().mapToDouble(a -> a.getStock().doubleValue()).sum());
			if (allStock.compareTo(list.get(0).getRequestWeight()) == -1) {
				list.stream().forEach(a -> a.setRequestWeightForSum(allStock)); // 如果所有库存都不足，则把用户需求降低到库存最大值进行计算
			} else {
				list.stream().forEach(a -> a.setRequestWeightForSum(a.getRequestWeight()));
			}
		}
		if (StringUtils.isBlank(quotationOrderId)) {
			List<Long> sellerIds = Arrays
					.asList(dtos.stream().map(InquiryOrderItemsDto::getSellerId).distinct().toArray(Long[]::new)); // 全部卖家id的数组
			List<String> warehouseNames = Arrays
					.asList(dtos.stream().map(InquiryOrderItemsDto::getWarehouse).distinct().toArray(String[]::new)); // 全部卖家id的数组
			if (InquirySelectOptions.LEASTAMOUNT.getCode().equals(option)) { // 总价最低
				orderDtos = selectLeastAmountInquiryInEligibleSellerIds(map, null, null); // 在所有卖家这一种组合中挑选价格最低的一组
			} else if (InquirySelectOptions.LEASTSELLER.getCode().equals(option)) { // 卖家数最少
				List<Long> resultSellerIdList = findLeastSeller(map, sellerIds); // 找到卖家数最少时的卖家id组合
				orderDtos = selectLeastAmountInquiryInEligibleSellerIds(map, resultSellerIdList, null); // 在卖家数最少组合中挑选价格最低的一组
			} else if (InquirySelectOptions.LEASTWAREHOUSE.getCode().equals(option)) { // 仓库数最少
				List<String> resultWarehouseNameList = findLeastWarehouse(map, warehouseNames); // 找到仓库数最少时的仓库名称组合
				orderDtos = selectLeastAmountInquiryInEligibleSellerIds(map, null, resultWarehouseNameList); // 在仓库数最少组合中挑选价格最低的一组
			}
		} else {
			for (Long i : map.keySet()) {
				orderDtos.add(buildInquiryOrderDto(map.get(i)));
			}
		}
		return orderDtos;
	}

	private InquiryOrderDto buildInquiryOrderDto(List<InquiryOrderItemsDto> groupDto) {
		InquiryOrderDto orderDto = new InquiryOrderDto();
		groupDto.sort((a, b) -> b.getWeight().compareTo(a.getWeight())); // 再按照销售重量对list做排序
		InquiryOrderItemsDto firstItems = groupDto.get(0);
		orderDto.setPurchaseOrderItemsId(firstItems.getPurchaseOrderItemsId());
		orderDto.setCategoryName(firstItems.getCategoryName());
		orderDto.setMaterialName(firstItems.getMaterialName());
		orderDto.setSpec(firstItems.getSpec());
		orderDto.setFactory(firstItems.getFactory());
		orderDto.setRequestWeight(firstItems.getRequestWeight());
		orderDto.setItems(groupDto);
		orderDto.setCityId(firstItems.getCityId());
		orderDto.setCityName(firstItems.getCityName());
		orderDto.setRemark(firstItems.getRemark());
		return orderDto;
	}

	/**
	 * 返回list中取num个元素的所有组合
	 *
	 * @param data
	 * @param num
	 * @return
	 */
	private <T> List<List<T>> parade(List<T> data, int num) {
		List<List<T>> result = new ArrayList<List<T>>();
		if (num == 1) { // 只排一个元素的时候（递归结束条件）
			for (T s : data) {
				List<T> l = new ArrayList<T>();
				l.add(s);
				result.add(l); // 每个元素分别保存到结果集
			}
			return result; // 并返回结果集
		}
		for (int i = 0; i < data.size(); i++) { // num>1，即排多个元素的时候，循环
			List<T> list = new ArrayList<T>(data);
			list.remove(i); // 去掉当前循环的元素作为下次递归的参数，即剩余的元素做递归
			List<List<T>> sub = parade(list, num - 1); // 递归调用
			for (List<T> l : sub) { // 然后循环递归得到的结果
				l.add(0, data.get(i)); // 把当前元素排在每个结果的前面
				result.add(l); // 并保存到结果集
			}
		}
		return result; // 最后返回结果集
	}

	/**
	 * 在卖家数最少组合中挑选价格最低的一组
	 *
	 * @param map
	 * @param sellerLists
	 * @return
	 */
	private List<InquiryOrderDto> selectLeastAmountInquiryInEligibleSellerIds(Map<Long, List<InquiryOrderItemsDto>> map,
			List<Long> sellerLists, List<String> warehouseLists) {
		List<InquiryOrderDto> orderDtos = new LinkedList<>();
		for (Long key : map.keySet()) {
			List<InquiryOrderItemsDto> groupDto = map.get(key);
			groupDto.sort((a, b) -> a.getCostPrice().compareTo(b.getCostPrice())); // 单价从低到高排序
			BigDecimal requestWeight = groupDto.get(0).getRequestWeightForSum(); // 需求重量
			BigDecimal lastWeight = requestWeight; // 剩余需要重量
			for (InquiryOrderItemsDto dto : groupDto) {
				if (sellerLists != null && !sellerLists.contains(dto.getSellerId())) { // 如果卖家限制不为null且限定卖家里面没有该卖家，则该资源不参与本次计算
					continue;
				}
				if (warehouseLists != null && !warehouseLists.contains(dto.getWarehouse())) { // 如果仓库限制不为null且限定仓库里面没有该仓库，则该资源不参与本次计算
					continue;
				}
				if (lastWeight.compareTo(dto.getStock()) > 0) { // 如果可供重量不足，把供应重量设为最大
					dto.setWeight(dto.getStock());
					lastWeight = lastWeight.subtract(dto.getStock());
				} else {
					dto.setWeight(lastWeight); // 如果已足够（等于或大于），供应重量为剩余需要供应重量
					break;
				}
			}
			// 封装外层
			orderDtos.add(buildInquiryOrderDto(groupDto));
		}
		return orderDtos;
	}

	/**
	 * 找到卖家数最少时的卖家id组合
	 *
	 * @param map
	 * @param sellerId
	 * @return
	 */
	private List<Long> findLeastSeller(Map<Long, List<InquiryOrderItemsDto>> map, List<Long> sellerId) {
		Map<Long, Map<Long, List<InquiryOrderItemsDto>>> filterMap = new HashMap<>();
		List<Long> resultSellerIdList = new LinkedList<>();
		for (Long key : map.keySet()) {
			List<InquiryOrderItemsDto> groupDto = map.get(key);
			Map<Long, List<InquiryOrderItemsDto>> reourceSellerMap = groupDto.stream()
					.collect(Collectors.groupingBy(InquiryOrderItemsDto::getSellerId, Collectors.toList()));
			filterMap.put(key, reourceSellerMap); // 构建一个<资源id, <卖家id, {可供数量,
													// 单价}>>的map，便于取数
		}
		int minSellerCount = 1;
		double totalAmount = Double.MAX_VALUE;
		while (minSellerCount <= sellerId.size() && resultSellerIdList.size() == 0) {
			List<List<Long>> sellerIdList = parade(sellerId, minSellerCount++); // 取出有minSellerCount个卖家时的所有卖家组合
			for (List<Long> sellerIds : sellerIdList) { // 对每一种组合进行分析，看是否满足客户采购需求
				double maxWeight = 0d;
				boolean result = true; // 默认为true
				double amount = 0d;
				for (Long key : filterMap.keySet()) {
					List<InquiryOrderItemsDto> list = new LinkedList<>();
					for (Long sid : sellerIds) {
						List<InquiryOrderItemsDto> temp = filterMap.get(key).get(sid);
						if (temp != null) {
							list.addAll(temp);
						}
					}
					maxWeight = list.stream().mapToDouble(a -> a.getStock().doubleValue()).sum();
					if (maxWeight < map.get(key).get(0).getRequestWeightForSum().doubleValue()) {
						result = false; // 如果不符合，result设置为false
						break; // 如果第一种资源不符合，这种卖家组合直接pass
					} else { // 计算总价
						list.sort((a, b) -> a.getCostPrice().compareTo(b.getCostPrice())); // 单价从低到高排序
						BigDecimal requestWeight = list.get(0).getRequestWeightForSum(); // 需求重量
						BigDecimal lastWeight = requestWeight; // 剩余需要重量
						for (InquiryOrderItemsDto dto : list) {
							if (sellerIds != null && !sellerIds.contains(dto.getSellerId())) { // 如果卖家限制不为null且限定卖家里面没有该卖家，则该资源不参与本次计算
								continue;
							}
							if (lastWeight.compareTo(dto.getStock()) > 0) { // 如果可供重量不足，把供应重量设为最大
								lastWeight = lastWeight.subtract(dto.getStock());
								amount += dto.getStock().multiply(dto.getCostPrice()).doubleValue();
							} else {
								amount += lastWeight.multiply(dto.getCostPrice()).doubleValue();
								break;
							}
						}
					}
				}
				if (result && amount < totalAmount) {
					totalAmount = amount;
					resultSellerIdList = sellerIds;
				}
			}
		}
		return resultSellerIdList;
	}

	/**
	 * 找到仓库数最少的仓库组合
	 *
	 * @param map
	 * @param warehouseNames
	 * @return
	 */
	private List<String> findLeastWarehouse(Map<Long, List<InquiryOrderItemsDto>> map, List<String> warehouseNames) {
		Map<Long, Map<String, List<InquiryOrderItemsDto>>> filterMap = new HashMap<>();
		List<String> resultWareHouseNameList = new LinkedList<>();
		for (Long key : map.keySet()) {
			List<InquiryOrderItemsDto> groupDto = map.get(key);
			Map<String, List<InquiryOrderItemsDto>> reourceWarehouseMap = groupDto.stream()
					.collect(Collectors.groupingBy(InquiryOrderItemsDto::getWarehouse, Collectors.toList()));
			filterMap.put(key, reourceWarehouseMap); // 构建一个<资源id, <仓库名称,
														// 可供数量>>的map，便于取数
		}
		int minWarehouseCount = 1;
		double totalAmount = Double.MAX_VALUE;
		while (minWarehouseCount <= warehouseNames.size() && resultWareHouseNameList.size() == 0) {
			List<List<String>> warehouseNameList = parade(warehouseNames, minWarehouseCount++); // 取出有minSellerCount个卖家时的所有卖家组合
			for (List<String> tempWarehouseNames : warehouseNameList) { // 对每一种组合进行分析，看是否满足客户采购需求
				double maxWeight = 0d;
				boolean result = true; // 默认为true
				double amount = 0d;
				for (Long key : filterMap.keySet()) {
					List<InquiryOrderItemsDto> list = new LinkedList<>();
					for (String whId : tempWarehouseNames) {
						List<InquiryOrderItemsDto> temp = filterMap.get(key).get(whId);
						if (temp != null) {
							list.addAll(temp);
						}
					}
					maxWeight = list.stream().mapToDouble(a -> a.getStock().doubleValue()).sum();
					if (maxWeight < map.get(key).get(0).getRequestWeightForSum().doubleValue()) {
						result = false; // 如果不符合，result设置为false
						break; // 如果第一种资源不符合，这种卖家组合直接pass
					} else { // 计算总价
						list.sort((a, b) -> a.getCostPrice().compareTo(b.getCostPrice())); // 单价从低到高排序
						BigDecimal requestWeight = list.get(0).getRequestWeightForSum(); // 需求重量
						BigDecimal lastWeight = requestWeight; // 剩余需要重量
						for (InquiryOrderItemsDto dto : list) {
							if (!tempWarehouseNames.contains(dto.getWarehouse())) { // 如果卖家限制不为null且限定卖家里面没有该卖家，则该资源不参与本次计算
								continue;
							}
							if (lastWeight.compareTo(dto.getStock()) > 0) { // 如果可供重量不足，把供应重量设为最大
								lastWeight = lastWeight.subtract(dto.getStock());
								amount += dto.getStock().multiply(dto.getCostPrice()).doubleValue();
							} else {
								amount += lastWeight.multiply(dto.getCostPrice()).doubleValue();
								break;
							}
						}
					}
				}
				if (result && amount < totalAmount) {
					totalAmount = amount;
					resultWareHouseNameList = tempWarehouseNames;
				}
			}
		}
		return resultWareHouseNameList;
	}

	/**
	 * 保存报价单
	 * 
	 * @param order
	 *            表头
	 * @param items
	 *            表体
	 * @param user
	 *            当前登陆用户
	 * @return
	 */
	@Transactional
	public Long saveQuotationOrder(QuotationOrder order, List<QuotationOrderItems> items, User user) {
		Date now = new Date();
		items = items.stream().filter(a -> (a.getWeight().compareTo(BigDecimal.ZERO) > 0)).collect(Collectors.toList());
		if (items.size() == 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "请至少选中一条记录生成报价单");
		}
		order.setLastUpdatedBy(user.getLoginId());
		order.setLastUpdated(now);
		if (order.getId().equals(0l)) {
			order.setId(null);
			order.setStatus(QuotationOrderStatus.UNCONFIRMED.getCode());
			order.setCreatedBy(user.getLoginId());
			order.setCreated(now);
			if (quotationOrderDao.insertSelective(order) != 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "新增表头失败");
			}
		} else {
			if (quotationOrderDao.updateByPrimaryKeySelective(order) != 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新表头失败");
			}
			quotationOrderItemsDao.deleteByQuotationOrderId(order.getId()); // 删除子表，全删全插
		}
		items.stream().forEach(a -> {
			a.setQuotationOrderId(order.getId());
			a.setCreatedBy(user.getLoginId());
			a.setLastUpdatedBy(user.getLoginId());
		});

		if (quotationOrderItemsDao.batchInsert(items) != items.size()) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存详情失败");
		}

		// 生成报价单时，采购单状态即为已报价
		purchaseOrderDao.updateStatusById(order.getPurchaseOrderId(), PurchaseOrderStatus.QUOTED.getCode(),
				user.getLoginId());
		return order.getId();
	}

	@Override
	public Map<String, Object> getSellerNum(Long purchaseOrderId, User user) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("sellerNum", inquiryOrderDao.getSellerNum(purchaseOrderId));
		paramMap.put("itemsNum", inquiryOrderItemsDao.getPurchaseOrderItemsNum(purchaseOrderId));
		paramMap.put("itemsSatisfaction", initItemSatisfaction(purchaseOrderId));
		paramMap.put("userName", user.getName());
		return paramMap;
	}

	/**
	 * 设置满足率
	 * 
	 * @param purchaseOrderId
	 * @return
	 */
	private List<InquiryOrderSatisfactionDto> initItemSatisfaction(Long purchaseOrderId) {
		List<InquiryOrderSatisfactionDto> itemSaList = inquiryOrderItemsDao
				.getPurchaseOrderItemsSatisfaction(purchaseOrderId);
		List<PurchaseOrderItemsDtoTwo> detailList = purchaseItemsDao.getListByPOIdTwo(purchaseOrderId);
		for (InquiryOrderSatisfactionDto sa : itemSaList) {
			for (PurchaseOrderItemsDtoTwo detail : detailList) {
				if (detail.getId().equals(sa.getPurchaseOrderItemId())) {
					sa.setCategoryName(detail.getCategoryName());
					sa.setMaterialName(detail.getMaterialName());
					sa.setSpec(detail.getSpec());
					if (detail.getWeight() != null && detail.getWeight().doubleValue() > 0) {
						if (sa.getTotalWeight().doubleValue() >= detail.getWeight().doubleValue()) {
							sa.setRate(100.00);
						} else {
							sa.setRate(sa.getTotalWeight().doubleValue() / detail.getWeight().doubleValue() * 100);
						}
					}
				}

			}
		}

		return itemSaList;
	}

	@Override
	public List<InquiryOrderDto> getInquiryOrderList(InquiryOrderQuery inquiryOrderQuery) {
		return inquiryOrderDao.getInquiryOrderList(inquiryOrderQuery);
	}

	@Override
	public void updateInquiryItems(JSONArray inquiryOrderItemsList, User user) {
		Iterator<JSONObject> items = inquiryOrderItemsList.iterator();
		while (items.hasNext()) {
			JSONObject item = items.next();
			searchService.saveResource(item, Long.valueOf(item.getString("sellerId")), user); // 更新资源表

			InquiryOrderItems old = itemsDao.selectByPrimaryKey(Long.valueOf(item.getString("id"))); // 更新明细
			old.setWeight(new BigDecimal(item.getString("weight")));
			old.setWarehouseId(StringUtils.isBlank(item.getString("warehouseId")) ? -1
					: Long.valueOf(item.getString("warehouseId")));
			old.setAbnormalWarehouse(
					StringUtils.isNotBlank(item.getString("warehouseId")) ? null : item.getString("warehouseName"));
			old.setFactoryId(Long.valueOf(item.getString("factoryId")));
			old.setPrice(new BigDecimal(item.getString("price")));
			old.setQuantity(Integer.valueOf(item.getString("quantity")));
			old.setLastUpdatedBy(user.getLoginId());
			
			//add by caosulin 20160902  增加钢厂和仓库名称的修改
			String factoryName = item.getString("factoryName");
			old.setFactoryName( StringUtils.isBlank(factoryName) ? null :factoryName.toString());
			
			String warehouseName = item.getString("warehouseName");
			if(!StringUtils.isBlank(warehouseName)){
				String [] wnames = warehouseName.split("-");
				if(wnames.length > 0){
					old.setWarehouseName(wnames[0]);
				}
			}
			
			if (itemsDao.updateByPrimaryKeySelective(old) != 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新询价明细失败");
			}
		}
	}

	/**
	 * 通过卖家ID获取当天的询价记录
	 */
	@Override
	public List<InquiryOrderSellersDto> getSellersInquiryRecord(String sellerIds) {
		JSONArray sellers = JSONArray.fromObject(sellerIds);
		List list = (List) JSONArray.toList(sellers);
		List<InquiryOrderSellersDto> sellersList = new ArrayList<InquiryOrderSellersDto>();
		if(list!=null && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				InquiryOrderSellersDto lastInquiryOrderSeller = inquiryOrderSellersDao
						.selectByAccountId(Long.valueOf(list.get(i).toString()));
				if (lastInquiryOrderSeller != null) {
					String lastInquiryTime = DateUtil.showTime(lastInquiryOrderSeller.getLastUpdated(),
							"yyyy-MM-dd HH:mm:ss");
					lastInquiryOrderSeller.setLastInquirytime(lastInquiryTime);
					sellersList.add(lastInquiryOrderSeller);
				}
			}
		}
		return sellersList;
	}

	/**
	 * 根据仓库Id获取仓库所在城市
	 */
	@Override
	public String getCityByWarehouseId(Long warehouseId) {
		String cityName = warehouseDao.getCityByWarehouseId(warehouseId);
		return cityName == null ? "未设置" : cityName;
	}

}
