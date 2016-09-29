package com.prcsteel.platform.api;

import java.util.List;
import java.util.Map;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.model.model.City;

/**
 * @author afeng
 * @version v2.0_account
 * @Description: 订单成交数据
 * @date 2016-6-12 09:42:13
 */
@RestApi(value="restSmartmatchService", restServer="smartmatchRestServer")
public interface RestSmartmatchService {
	/**
	 * 保存交易单记录到资源表，资源类型为历史资源
	 * @param orderItems 订单资源详情
	 * @author afeng
	 * @return
	 */
	@RestMapping(value = "resource/saveOrderHistoryResource.html", method = RequestMethod.POST)
    public Result saveOrderHistoryResource(List<Map<Object,Object>>  orderItems);



	@RestMapping(value = "purchaseorder/getCityByOrderId.html", method = RequestMethod.POST)
	public City getCityByOrderId(@UrlParam("orderId") Long orderId);
}

