package com.prcsteel.platform.api;

import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.common.vo.Result;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Rolyer on 2016/1/26.
 */
@RestApi(value="restTemplateApiInvoker", restServer="accRestServer")
public interface RestTemplateApiInvoker {
    @RestMapping(value="template/save.html", method= RequestMethod.POST)
    Result save(AccountContractTemplate act);

    @RestMapping(value = "template/query/{id}.html", method = RequestMethod.GET)
    Result query(@UrlParam("id") Long id);

    @RestMapping(value = "template/enabled.html", method = RequestMethod.POST)
    Result enabled(Long id, Boolean enabled);

    @RestMapping(value = "template/resolve/{accountId}/{type}/{id}.html", method = RequestMethod.GET)
    Result resolve(@UrlParam("accountId") Long accountId, @UrlParam("id") Long id, @UrlParam("type") String type);
}
