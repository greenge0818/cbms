package com.prcsteel.platform.smartmatch.service;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.dto.SearchResultDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchResultDtoTwo;
import com.prcsteel.platform.smartmatch.model.model.Resource;
import com.prcsteel.platform.smartmatch.model.dto.BillHistoryDataDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchCompanyContactDto;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.core.model.model.City;

import net.sf.json.JSONObject;

/**
 * Created by Green.Ge on 2015/11/19.
 */

public interface SearchService {
	/***
	 * 
	 * @Title: match @Description: 智能匹配 @param @param purchaseOrderId
	 * 采购单号 @param @param notSpecificSellerId 指定不搜索的卖家列表 @param @param
	 * specificSellerId 指定搜索某个卖家 @param @return 参数 @return SearchResultDto
	 * 返回类型 @throws
	 */
	SearchResultDto match(Long purchaseOrderId, List<Long> purchaseOrderItemId, List<Long> notSpecificSellerId,
			Long specificSellerId, Integer isAppend);

	Long saveInquiryOrder(String inquiryOrder, User user);

	Resource saveResource(JSONObject item, Long accountId, User user);

	List<BillHistoryDataDto> selectBillHistoryByAccountId(Long accountId);

	/***
	 * 
	 * @param cityId 
	 * @Title: match @Description: 智能匹配 @param @param purchaseOrderId
	 * 采购单号 @param @param notSpecificSellerId 指定不搜索的卖家列表 @param @param
	 * specificSellerId 指定搜索某个卖家 @param @return 参数 @return SearchResultDto
	 * 返回类型 @throws
	 */
	SearchResultDtoTwo match2(Long purchaseOrderId, List<Long> purchaseOrderItemId, List<Long> notSpecificSellerId,
			Long specificSellerId, Integer isAppend, Long cityId);

	/**
	 * 匹配到的城市列表
	 * 
	 * @param purchaseOrderId
	 * @return
	 */
	List<City> matchCities(Long purchaseOrderId);

	/**
	 * 联系人列表
	 * @param accountId
	 * @return
	 */
	List<SearchCompanyContactDto> queryCompanyContacts(Long accountId);
}
