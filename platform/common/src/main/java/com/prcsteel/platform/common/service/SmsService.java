package com.prcsteel.platform.common.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * webService
 * Created by lcw on 2015/8/18.
 */
@WebService(serviceName = "SMSService", targetNamespace = "http://tempuri.org/")
public interface SmsService {

    /**
     * 
     *
     * @param key     key
     * @param phone 
     * @param content
     * @param from app
     * @return
     */
    @WebMethod(operationName = "SendMongateMessage", action = "http://tempuri.org/SendMongateMessage")
    @WebResult(name = "SendMongateMessageResult", targetNamespace = "http://tempuri.org/")
    String sendMessage(@WebParam(name = "key", targetNamespace = "http://tempuri.org/") String key,
                       @WebParam(name = "phone", targetNamespace = "http://tempuri.org/") String phone,
                       @WebParam(name = "content", targetNamespace = "http://tempuri.org/") String content,
                       @WebParam(name = "from", targetNamespace = "http://tempuri.org/") String from);
}
