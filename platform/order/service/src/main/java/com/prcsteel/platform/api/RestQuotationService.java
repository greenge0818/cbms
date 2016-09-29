package com.prcsteel.platform.api;

import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrder;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author chengui
 * @Description: 报价单接口调用
 * @date 2016-8-4
 */
@RestApi(value="restQuotationService", restServer="smartmatchRestServer")
public interface RestQuotationService {
    /**
     * 保存银票分配记录
     * @return
     */
    @RestMapping(value = "quotationitems/updateStatusById.html", method = RequestMethod.POST)
    Result save(@UrlParam("ids") String ids, @UrlParam("statusStr") String statusStr,
                @UrlParam("loginId") String loginId);

    /**
     * 根据id查询报价单详情
     * @param idsStr
     * @param userIdsStr
     * @return List<LinkedHashMap<String, Object>>
     */
    @RestMapping(value = "quotationitems/getQuotationOrderItems.html", method = RequestMethod.POST)
    List<LinkedHashMap<String, Object>> getQuotationOrderItems(@UrlParam("idsStr") String idsStr, @UrlParam("userIdsStr") String userIdsStr);

    /**
     * 根据id查询报价单
     * @param id
     * @return Long
     */
    @RestMapping(value = "quotation/getQuotationOrderById.html", method = RequestMethod.POST)
    QuotationOrder getQuotationOrderById(@UrlParam("id") Long id);
}
