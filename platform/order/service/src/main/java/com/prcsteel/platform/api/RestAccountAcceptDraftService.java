package com.prcsteel.platform.api;

import com.prcsteel.platform.common.vo.Result;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Rabbit
 * @version v2.0_account
 * @Description: 保存银票分配记录
 * @date 2016-3-14 10:33:13
 */
@RestApi(value="restAccountAcceptDraftService", restServer="accRestServer")
public interface RestAccountAcceptDraftService {
    /**
     * 保存银票分配记录
     * @return
     */
    @RestMapping(value = "accountAcceptDraft/save.html", method = RequestMethod.POST)
    Result save(@UrlParam("accountId") Long accountId, @UrlParam("acceptDraftId") Long acceptDraftId,
                               @UrlParam("amount") Double amount, @UrlParam("userId") Long userId);

}
