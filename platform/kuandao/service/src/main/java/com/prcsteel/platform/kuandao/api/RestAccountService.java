package com.prcsteel.platform.kuandao.api;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;


@RestApi(value="restAccountService", restServer="accRestServer")
public interface RestAccountService {
	
	@RestMapping(value = "accounts/{accountId}", method = RequestMethod.GET)
	String queryById(@UrlParam("accountId") Long acctId);

}
