package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderDto;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderItemsDto;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderItemsDtoTwo;
import com.prcsteel.platform.smartmatch.model.dto.SearchCompanyContactDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchCompanyResultDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchResourceDtoTwo;
import com.prcsteel.platform.smartmatch.model.dto.SearchResultItemsDto;
import com.prcsteel.platform.smartmatch.model.dto.SellerMatchWeight;
import com.prcsteel.platform.smartmatch.model.model.PurchaseOrderItems;
import com.prcsteel.platform.smartmatch.model.query.PurchaseOrderItemsQuery;
import com.prcsteel.platform.smartmatch.model.query.SearchPurchaseOrderQuery;
import com.prcsteel.platform.smartmatch.model.query.SmartMatchQuery;

public interface PurchaseOrderItemsDao {
	int deleteByPrimaryKey(Long id);

	int insert(PurchaseOrderItems record);

	int insertSelective(PurchaseOrderItems record);

	PurchaseOrderItems selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(PurchaseOrderItems record);

	int updateByPrimaryKey(PurchaseOrderItems record);

	List<PurchaseOrderItems> getListByPOId(Long purchaseOrderId);

	List<PurchaseOrderItems> getListByIds(@Param("itemIds") List<Long> itemIds);

	// 某个卖家在品规覆盖率上的得分
	List<SellerMatchWeight> getSellerCategoryCoverage(SmartMatchQuery query);

	// 某个卖家在品规覆盖率上的得分
	List<SellerMatchWeight> getSellerPriceWeight(SmartMatchQuery query);

	int deleteByIds(@Param("ids") List<Long> ids);

	/**
	 * 统计某卖家近一个月内某品规交易笔数
	 * 
	 * @param query
	 * @return
	 */
	List<Map<String, Object>> countHistoryTrading(List<SearchPurchaseOrderQuery> queryList);

	/**
	 *
	 * @Title: queryResourceItemsByOrderIdAndSellerId @Description:
	 *         根据采购单ID和卖家Id返回该卖家能提供的资源列表 @param @param
	 *         purchaseOrderId @param @param sellerId @param @return 参数 @return
	 *         List<SearchResultItemsDto> 返回类型 @throws
	 */
	List<SearchResultItemsDto> queryResourceItems(SmartMatchQuery query);

	List<PurchaseOrderDto> getListByPurchaseOrderId(Long id);

	/**
	 * 查询历史采购单记录
	 */
	List<PurchaseOrderItemsDto> getItemsByTelOrAccountName(PurchaseOrderItemsQuery query);

	/**
	 * 查询采购单表体，用于继续询价展示
	 * 
	 * @param purchaseOrderId
	 * @return
	 */
	List<PurchaseOrderItemsDto> getItemsByPurchaseOrderId(Long purchaseOrderId);

	/**
	 * 根据采购单Id获取品名Uuid
	 * 
	 * @param purchaseOrderId
	 * @return
	 */
	List<String> getCategoryByPurchaseOrderId(Long purchaseOrderId);

	/**
	 * 根据采购单Id获取采购单明细
	 * 
	 * @param purchaseOrderId
	 * @return
	 */
	List<SearchResultItemsDto> queryByPurchaseOrderId(Long purchaseOrderId);

	/*********** 找货2.0 start ,added by yjx **********/
	// 订单能匹配到资源的城市列表
	List<City> queryMatchedCitys(Long purchaseOrderId);

	// 匹配公司列表
	List<SearchCompanyResultDto> queryMatchedCompanies(@Param("purchaseOrderId") Long purchaseOrderId,
			@Param("cityId") Long cityId, @Param("start") Integer start, @Param("end") Integer end);
	//根据公司ID获取联系人列表，联系人与交易员1：1，一个公司两个联系人可能是关联的两个交易员
	List<SearchCompanyContactDto> queryCompanyContacts(Long accountId);
	// 匹配的资源列表
	List<SearchResourceDtoTwo> queryMatchedResources(@Param("purchaseOrderId") Long purchaseOrderId,
			@Param("cpIds") List<Long> cpIds);

	List<City> findCenterCityIds(Long deliveryCityId);

	List<PurchaseOrderItemsDtoTwo> getListByPOIdTwo(Long purchaseOrderId);

	List<PurchaseOrderItemsDtoTwo> getListByIdsTwo(@Param("itemIds") List<Long> itemIds);

	/**
	 * 补充匹配
	 * 
	 * @return
	 */
	List<SearchCompanyResultDto> queryComplementMatchedCompanies(@Param("purchaseOrderId") Long purchaseOrderId,
			@Param("purchaseOrderItemId") List<Long> purchaseOrderItemId,
			@Param("notSpecificSellerId") List<Long> notSpecificSellerId, @Param("cityId") Long cityId);

	/*********** 找货2.0 start **********/

}