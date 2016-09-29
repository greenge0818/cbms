package com.prcsteel.platform.account.api;

import com.prcsteel.platform.common.vo.Result;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * @author Rabbit
 * @version v2.0_account
 * @Description: 提现申请api
 * @date 2016-3-14 10:33:13
 */
@RestApi(value="restPayRequestService", restServer="orderRestServer")
public interface RestPayRequestService {
    /**
     * 保存提现申请
     */
    @RestMapping(value = "payrequest/saveapply.html", method = RequestMethod.POST)
    public Result saveApply(@UrlParam("departmentId") Long departmentId, @UrlParam("bankId") Long bankId,
                     @UrlParam("money") Double money, @UrlParam("balance") Double balance, @UrlParam("userId") Long userId);
}
