package com.prcsteel.platform.order.web.controller;

import com.prcsteel.platform.order.model.dto.ConsignOrderInfoDto;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.web.vo.RestResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chengui
 * @version v2.0_accout_sprint18
 * @Description: 订单管理Api
 * @date 2016/5/30
 */
@RestController
@RequestMapping("/api/order/")
public class RestOrderController extends BaseController {
	@Resource
	ConsignOrderService consignOrderService;
    
   /**
    * 根据订单号查询订单详情信息
    * @param code 订单号
    * @return
    */
	@RequestMapping(value = "orders/{code}", method = RequestMethod.POST)
	public RestResult query(@PathVariable("code") String code) {
		RestResult result = new RestResult();
		ConsignOrder order = consignOrderService.selectByCode(code);
		if (order == null) {
			result.setSuccess(false);
			result.setMsg("交易单号为" + code + "的交易单不存在");
			return result;
		}
		result.setData(new ConsignOrderInfoDto(order, consignOrderService.queryOrderItemsById(order.getId())));
		return result;
	}


	/**
	 * 根据订单号查询订单详情信息
	 * @param accountId 买家accountId
	 * @param status 订单状态
	 * @return
	 */
	@RequestMapping(value = "orders/query.html", method = RequestMethod.GET)
	public List<ConsignOrder> queryByAccountIdAndStatus(@RequestParam("accountId") Long accountId, @RequestParam("status") String status) {
		String statusCode = null;
		for(ConsignOrderStatus consignOrderStatus : ConsignOrderStatus.values()){
			if(consignOrderStatus.name().equals(StringUtils.upperCase(status))){
				statusCode  = consignOrderStatus.getCode();
				break;
			}
		}

		if(null == accountId || null == statusCode){
			return null;
		}
		return consignOrderService.selectByAccountIdAndStatus(accountId, statusCode);
	}


}
