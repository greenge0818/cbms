package com.prcsteel.platform.api;

import com.prcsteel.platform.common.vo.Result;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 询价单REST服务
 * @author prcsteel
 *
 */
@RestApi(value="restPurchaseOrderService", restServer="purchaseOrderRestServer")
public interface RestPurchaseOrderService {
   /**
    * 更新询价单的状态
    * @param id 询价单id
    * @param statusNum 更新的询价单状态
    * @param loginId 操作人
    * @return
    */
    @RestMapping(value = "purchaseorder/updateStatusById.html", method = RequestMethod.POST)
    public Result updateStatusById(@UrlParam("id") Long id,@UrlParam("statusStr") String statusStr,@UrlParam("loginId") String loginId);

}
