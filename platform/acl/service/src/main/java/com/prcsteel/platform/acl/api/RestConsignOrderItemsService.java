package com.prcsteel.platform.acl.api;

import com.prcsteel.platform.order.model.dto.AllowanceOrderItemsDto;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @date :2016-5-7
 * @decrpiton: 风控代码合并，通过rest调用order服务，查询订单详细子表信息
 * @author :zhoucai@prcsteel.com
 */
@RestApi(value="restConsignOrderItemsService", restServer="orderRestServer")
public interface RestConsignOrderItemsService {

    @RestMapping(value = "consignrrderitems/querysellerquantity.html", method = RequestMethod.POST)
     AllowanceOrderItemsDto querySellerQuantity(@UrlParam("paramMap") Map<String, Object> paramMap);


    @RestMapping(value = "consignrrderitems/querysellerorgquantity.html", method = RequestMethod.POST)
     AllowanceOrderItemsDto querySellerOrgQuantity(@UrlParam("paramMap") Map<String, Object> paramMap);

    @RestMapping(value = "consignrrderitems/queryallsellerquantity.html", method = RequestMethod.POST)
    AllowanceOrderItemsDto queryAllSellerQuantity(@UrlParam("paramMap") Map<String, Object> paramMap);


}

