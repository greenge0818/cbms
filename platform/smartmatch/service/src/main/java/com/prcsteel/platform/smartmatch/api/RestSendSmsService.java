package com.prcsteel.platform.smartmatch.api;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 发送短信
 * @author lixiang
 *
 */
@Service
@RestApi(value="restSendSmsService", restServer="esbRestServer")
public interface RestSendSmsService {

    /**
     * @ClassName: send
     * @Description: 发短信
     * @Author Tiny
     * @Date 2016年06月12日
     */
    @RestMapping(value = "/sms/send/V2", method = RequestMethod.POST)
    String send(@UrlParam("timestamp") String timestamp, @UrlParam("token") String token,@UrlParam("phone") String phone, @UrlParam("content") String content, @UrlParam("from") String from);
  
}
