package com.prcsteel.platform.smartmatch.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.smartmatch.model.dto.BillHistoryDataDto;
import com.prcsteel.platform.smartmatch.model.dto.SearchCompanyContactDto;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.smartmatch.model.dto.SearchResultDtoTwo;
import com.prcsteel.platform.smartmatch.service.SearchService;

/**
 * Created by Green.Ge on 2015/11/19.
 */
@Controller
@RequestMapping("/smartmatch/")
public class SearchController extends BaseController {
	@Resource
	SearchService searchService;

	@RequestMapping("match")
	@ResponseBody
	public Result match(Long purchaseOrderId, String purchaseOrderItemIds, String notSpecificSellerIds,
			Long specificSellerId, Integer isAppend, Long cityId) {
		// long start = System.currentTimeMillis();
		Result result = new Result();
		// List<Long> itemIdList = new ArrayList<Long>();
		// if(StringUtils.isNotBlank(itemIds)){
		// Arrays.asList(itemIds.split(",")).stream().forEach(a->itemIdList.add(Long.parseLong(a)));
		// }
		List<Long> noSpecificSellerList = new ArrayList<Long>();
		if (StringUtils.isNotBlank(notSpecificSellerIds)) {
			Arrays.asList(notSpecificSellerIds.split(",")).stream()
					.forEach(a -> noSpecificSellerList.add(Long.parseLong(a)));
		}
		List<Long> purchaseOrderItemIdList = new ArrayList<Long>();
		if (StringUtils.isNotBlank(purchaseOrderItemIds)) {
			Arrays.asList(purchaseOrderItemIds.split(",")).stream()
					.forEach(a -> purchaseOrderItemIdList.add(Long.parseLong(a)));
		}
		try {
			// SearchResultDto searchResult =
			// searchService.match(purchaseOrderId, purchaseOrderItemIdList,
			// noSpecificSellerList, specificSellerId, isAppend);
			SearchResultDtoTwo searchResult = searchService.match2(purchaseOrderId, purchaseOrderItemIdList,
					noSpecificSellerList, specificSellerId, isAppend, cityId);

			result.setData(searchResult);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		// long end = System.currentTimeMillis();
		// System.out.println("spent:"+(end-start)/1000+" sec");
		return result;
	}

	/**
	 * 资源城市列表
	 * 
	 * @param purchaseOrderId
	 * @return
	 */
	@RequestMapping("matchcities")
	@ResponseBody
	public Result matchCities(Long purchaseOrderId) {
		Result result = new Result();
		try {
			List<City> cityList = searchService.matchCities(purchaseOrderId);
			result.setData(cityList);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	// 保存询价单
	@ResponseBody
	@RequestMapping(value = "saveInquiryOrder", method = RequestMethod.POST)
	public Result saveInquiryOrder(String inquiryOrder) {
		Result result = new Result();
		try {
			Long inquiryOrderId = searchService.saveInquiryOrder(inquiryOrder, getLoginUser());
			result.setSuccess(true);
			result.setData(inquiryOrderId);

		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	// 搜索卖家交易历史记录
	@ResponseBody
	@RequestMapping(value = "getBillHistory", method = RequestMethod.POST)
	public Result selectBillHistoryByAccountId(Long accountId) {
		List<BillHistoryDataDto> dtos = searchService.selectBillHistoryByAccountId(accountId);
		if (dtos.size() > 0) {
			return new Result(dtos);
		} else {
			return new Result("该卖家一个月内没有成交", false);
		}
	}
	
	/**
	 * 查询公司联系人
	 * @param inquiryOrder
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findComContacts", method = RequestMethod.POST)
	public Result findComContacts(Long accountId) {
		Result result = new Result();
		try {
			List<SearchCompanyContactDto> contactList = searchService.queryCompanyContacts(accountId);
			result.setSuccess(true);
			result.setData(contactList);

		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

}
