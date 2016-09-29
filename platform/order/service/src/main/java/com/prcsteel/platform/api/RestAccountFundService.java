package com.prcsteel.platform.api;

import com.prcsteel.platform.common.vo.Result;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Rabbit
 * @version v2.0_account
 * @Description: 统一支付接口
 * @date 2016-3-14 10:33:13
 */
@RestApi(value="restAccountFundService", restServer="accRestServer")
public interface RestAccountFundService {
    /**
     * 支付接口
     * @return
     */
    @RestMapping(value = "accountfund/updateAccountFund.html", method = RequestMethod.POST)
    public Result updateAccountFund(@UrlParam("accountId") Long accountId, @UrlParam("associationType") String associationType,
                                    @UrlParam("code") String code, @UrlParam("accountTransApplyType") String accountTransApplyType,
                                    @UrlParam("balance") Double balance, @UrlParam("balanceFreeze") Double balanceFreeze,
                                    @UrlParam("secondSettlement") Double secondSettlement, @UrlParam("secondSettlementFreeze") Double secondSettlementFreeze,
                                    @UrlParam("creditUsed") Double creditUsed, @UrlParam("creditLimit") Double creditLimit,
                                    @UrlParam("payType") String payType, @UrlParam("operatorId") Long operatorId,
                                    @UrlParam("operatorName") String operatorName, @UrlParam("operateDate") Long operateDate);

    /**
     *  信用额度还款抵扣接口
     * @return
     */
    @RestMapping(value = "accountfund/payForCredit.html", method = RequestMethod.POST)
    public Result payForCredit(@UrlParam("accountId") Long accountId, @UrlParam("associationType") String associationType,
                                    @UrlParam("code") String code, @UrlParam("operatorId") Long operatorId,
                                    @UrlParam("operatorName") String operatorName, @UrlParam("operateDate") Long operateDate);
}
