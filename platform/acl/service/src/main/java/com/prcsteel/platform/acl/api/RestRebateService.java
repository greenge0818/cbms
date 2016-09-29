package com.prcsteel.platform.acl.api;

import com.prcsteel.platform.acl.model.model.Rebate;
import com.prcsteel.platform.order.model.dto.RebateDto;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @date :2016-5-18
 * @decrpiton: 查询客户的返利信息，通过rest调用order，
 * @author :zhoucai@prcsteel.com
 */
@RestApi(value="restRebateService", restServer="orderRestServer")
public interface RestRebateService {

    /**
     * 获取所有正在生效的提成方案
     * @return
     */
    @RestMapping(value = "sys/getallrebatedto.html", method = RequestMethod.GET)
    List<RebateDto> getAllRebateDto();

    /**
     * 重置新的返利方案
     * @param rebateList
     * @return
     */
    @RestMapping(value = "sys/refleshrebate.html", method = RequestMethod.POST)
    int refleshRebate(@UrlParam("rebateList") List<Rebate> rebateList);

}
