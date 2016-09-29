package com.prcsteel.platform.smartmatch.api;

import java.util.List;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

import com.prcsteel.platform.account.model.dto.AccountAllDto;
import com.prcsteel.platform.account.model.model.Account;

/**
 * 账户REST服务接口
 * @author caosulin
 *
 */
@RestApi(value="smart_restAccountService", restServer="accRestServer")
public interface RestAccountService {
    /**
     * 查询所有卖家
     * @return
     */
    @RestMapping(value = "account/selectallselleraccount.html", method = RequestMethod.POST)
    public List<AccountAllDto> selectAllSellerAccount();
    /**
     * 查询所有买家
     * @return
     */
    @RestMapping(value = "account/selectallbuyeraccount.html", method = RequestMethod.POST)
    public List<AccountAllDto> selectAllBuyerAccount();
    /**
     * 根据id查询客户信息
     * @param accountId
     * @return Account
     */
    @RestMapping(value = "account/queryaccountbyid.html", method = RequestMethod.POST)
    public Account selectAccountById(@UrlParam("accountId") Long accountId);

}
