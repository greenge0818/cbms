package com.prcsteel.platform.api;

import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.common.vo.Result;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by prcsteel on 2016/3/17.
 */
@RestApi(value="accountDepartmentService", restServer="accRestServer")
public interface AccountDepartmentService {
    @RestMapping(value = "account/queryDepartmentByName.html", method = RequestMethod.POST)
    public int queryDepartmentByName(@UrlParam("accountId")Long accountId);
    @RestMapping(value = "account/queryDepartment.html", method = RequestMethod.POST)
    public Long queryDepartment(@UrlParam("id")Long id);
    @RestMapping(value = "account/registerCompany.html", method = RequestMethod.POST)
    public Result registerCompany(@UrlParam("companyName") String companyName, @UrlParam("accountCode") String accountCode,
                                  @UrlParam("bankName") String bankName, @UrlParam("bankCode") String bankCode,
                                  @UrlParam("contactName") String contactName, @UrlParam("tel") String tel,
                                  @UrlParam("userId") Long userId, @UrlParam("userName") String userName);
    @RestMapping(value = "account/queryByAccountId.html", method = RequestMethod.POST)
    public AccountDto queryByAccountId(@UrlParam("id") Long accountId);
    @RestMapping(value = "account/queryContactByTel.html", method = RequestMethod.POST)
    public Contact queryContactByTel(@UrlParam("tel") String tel);

}

