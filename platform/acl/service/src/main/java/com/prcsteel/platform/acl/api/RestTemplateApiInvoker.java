package com.prcsteel.platform.acl.api;

import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.common.vo.Result;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Rolyer on 2016/1/26.
 */
@RestApi(value="restTemplateApiInvoker", restServer="accRestServer")
public interface RestTemplateApiInvoker {

    @RestMapping(value="template/sys/list.html?id=0", method= RequestMethod.GET)
    List<AccountContractTemplate> list(@UrlParam("id") String id);

    @RestMapping(value="template/save.html", method = RequestMethod.POST)
    Result save(AccountContractTemplate act);

    @RestMapping(value = "template/query/{id}.html", method = RequestMethod.GET)
    AccountContractTemplate query(@UrlParam("id") Long id);

    @RestMapping(value = "template/enabled/{enabled}/{id}.html", method = RequestMethod.POST)
    Result enabled(@UrlParam("id") Long id, @UrlParam("enabled") Boolean enabled);

    @RestMapping(value = "template/resolve/{accountId}/{type}/{id}.html", method = RequestMethod.GET)
    Result resolve(@UrlParam("accountId") Long accountId, @UrlParam("id") Long id, @UrlParam("type") String type);
    
    @RestMapping(value = "template/release/{type}/{id}.html", method = RequestMethod.POST)
    Result release(@UrlParam("id") Long id,  @UrlParam("type")String type);

    @RestMapping(value = "template/doNotRelease/{id}.html", method = RequestMethod.POST)
    Result doNotRelease(@UrlParam("id") Long id);

}
