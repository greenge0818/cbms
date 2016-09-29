package com.prcsteel.platform.account.api;

import java.math.BigDecimal;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

import com.prcsteel.platform.common.vo.Result;
/**
 * @author lixiang
 * @version v2.0_account
 * @Description: 二次结算应付api
 * @date 2016-3-14 
 */
@RestApi(value="restSettlementService", restServer="orderRestServer")
public interface RestSettlementService {
    
	/**
	 * 应付款申请
	 * 
	 * @param accountId
	 *            客户id
	 * @param id
	 *            部门id
	 * @param accountName
	 *            客户名称
	 * @param deductionPay
	 *            申请提现金额
	 * @param balanceSecondSettlemento
	 *            二次余额
	 * @param bankId
	 *            银行id
	 * @return
	 */
    @RestMapping(value = "settlement/savepayrequst.html", method = RequestMethod.POST)
    public Result savePayRequst(
    		@UrlParam("accountId") Long accountId,
    		@UrlParam("departmentId") Long departmentId,
    		@UrlParam("deductionPay") BigDecimal deductionPay,
    		@UrlParam("balanceSecondSettlemento") BigDecimal balanceSecondSettlemento,
    		@UrlParam("bankId") Long bankId,
    		@UrlParam("userId") Long userId);
}
