package com.prcsteel.platform.acl.api;

import com.prcsteel.platform.account.model.model.Account;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @date :2016-5-7
 * @decrpiton: 风控代码合并，通过rest调用acc服务，查询订单详细子表信息
 * @author :zhoucai@prcsteel.com
 */
@RestApi(value="restAccountService", restServer="accRestServer")
public interface RestAccountService {
    /**
     * 根据客户
     * @param accountName
     * @return Account
     */
    @RestMapping(value = "account/selectaccountbyname.html", method = RequestMethod.POST)
     Account selectAccountByName(@UrlParam("accountName") String accountName);

    /**
     * 根据id查询客户信息
     * @param accountId
     * @return Account
     */
    @RestMapping(value = "account/queryaccountbyid.html", method = RequestMethod.POST)
     Account selectAccountById(@UrlParam("accountId") Long accountId);

}

