package com.prcsteel.platform.account.api;

import com.prcsteel.platform.order.model.model.AcceptDraft;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author chengui
 * @version v2.0_account
 * @Description: 银票管理接口api
 * @date 2016/4/22.
 */
@RestApi(value="restAcceptDraftService", restServer="orderRestServer")
public interface RestAcceptDraftService {

    @RestMapping(value = "acceptdraft/query.html", method = RequestMethod.POST)
    List<AcceptDraft> query(@UrlParam("accountId") Long accountId, @UrlParam("status") String status);


}
