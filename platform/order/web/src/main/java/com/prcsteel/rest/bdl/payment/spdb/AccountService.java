package com.prcsteel.rest.bdl.payment.spdb;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.prcsteel.rest.bdl.payment.spdb.model.request.RequestBody;
import com.prcsteel.rest.bdl.payment.spdb.model.request.RequestHead;
import com.prcsteel.rest.bdl.payment.spdb.model.response.ResponseData;

/**
 * Created by kongbinheng on 2015/7/13.
 */

@WebService
public interface AccountService {

    @WebResult
    @WebMethod
    ResponseData queryAccountDetail(@WebParam RequestHead requestHead, @WebParam RequestBody requestBody);

}