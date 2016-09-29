package com.prcsteel.platform.api;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author chengui
 * @date 2016-8-5 10:33:13
 */
@RestApi(value="restAccountService", restServer="accRestServer")
public interface RestAccountService {
    /**
     * 查询客户是否为额度客户
     * @return
     */
    @RestMapping(value = "account/getGroupCountByAccountId.html", method = RequestMethod.POST)
    int getGroupCountByAccountId(@UrlParam("accountId") Long accountId);

}
