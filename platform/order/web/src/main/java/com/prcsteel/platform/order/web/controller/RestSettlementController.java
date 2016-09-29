package com.prcsteel.platform.order.web.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.service.order.SecondSettlementLogService;
/**
 * @author lixiang
 * @version v2.0_account
 * @Description: 提现申请Api
 * @date 2016/3/21
 */
@RestController
@RequestMapping("/api/settlement/")
public class RestSettlementController extends BaseController {
    @Resource
    SecondSettlementLogService secondSettlementLogService;
    
   /**
    * 应付款申请
    * @param accountId 客户id
    * @param departmentId 部门id
    * @param accountName 客户名称
    * @param deductionPay 申请提现金额
    * @param balanceSecondSettlemento 二次余额
    * @param bankId 银行id
    * @return
    */
    @ResponseBody
	@RequestMapping(value = "savepayrequst", method = RequestMethod.POST)
	public Result savePayRequst(
			@RequestParam("accountId") Long accountId,
			@RequestParam("departmentId") Long departmentId,
			@RequestParam("deductionPay") BigDecimal deductionPay,
			@RequestParam("balanceSecondSettlemento") BigDecimal balanceSecondSettlemento,
			@RequestParam("bankId") Long bankId,
			@RequestParam("userId") Long userId) {
		Result result = new Result();
		
		try {
			secondSettlementLogService.addSecondPayAmount(departmentId, deductionPay, balanceSecondSettlemento, bankId, userService.queryById(userId), accountId);
			result.setSuccess(true);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		} 
		return result;
	}
}
