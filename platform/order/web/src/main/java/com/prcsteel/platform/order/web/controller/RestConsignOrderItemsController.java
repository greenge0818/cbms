package com.prcsteel.platform.order.web.controller;

import com.prcsteel.platform.order.model.dto.AllowanceOrderItemsDto;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @date :2016-5-7
 * @decrpiton:迁移风控上线代码 将归属于acl和account进行代码迁移，并将服务调用统一成调用rest服务进行跨应用调用
 * @author :zhoucai@prcsteel.com
 */
@RestController
@RequestMapping("/api/consignrrderitems/")
public class RestConsignOrderItemsController extends BaseController {
	@Resource
	ConsignOrderItemsService consignOrderItemsService;
    

	@RequestMapping(value = "querysellerquantity", method = RequestMethod.POST)
	public AllowanceOrderItemsDto querySellerQuantity(@RequestParam("paramMap") Map<String, Object> paramMap) {
		return consignOrderItemsService.querySellerQuantity(paramMap);
	}


	@RequestMapping(value = "querysellerorgquantity", method = RequestMethod.POST)
	public AllowanceOrderItemsDto querySellerOrgQuantity(@RequestParam("paramMap") Map<String, Object> paramMap) {
		return  consignOrderItemsService.querySellerOrgQuantity(paramMap);
	}


	@RequestMapping(value = "queryallsellerquantity", method = RequestMethod.POST)
	public AllowanceOrderItemsDto queryAllSellerQuantity(@RequestParam("paramMap") Map<String, Object> paramMap) {
		return  consignOrderItemsService.querySellerOrgQuantity(paramMap);
	}
}

