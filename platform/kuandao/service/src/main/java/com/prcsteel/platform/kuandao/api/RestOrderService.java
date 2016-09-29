package com.prcsteel.platform.kuandao.api;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestApi(value="restOrderService", restServer="orderRestServer")
public interface RestOrderService {
	
	@RestMapping(value = "orders/query", method = RequestMethod.GET)
	String queryByAccountId(@RequestParam("accountId") Long acctId,@RequestParam("status") String status);

}
