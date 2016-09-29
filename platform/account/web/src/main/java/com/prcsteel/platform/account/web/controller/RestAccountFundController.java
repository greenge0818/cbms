package com.prcsteel.platform.account.web.controller;

import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.PayType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Controller
@RequestMapping("/api/accountfund")
public class RestAccountFundController extends BaseController{
	@Resource
	private AccountFundService accountFundService;

	@Resource
	private UserService userService;

	@ResponseBody
	@RequestMapping(value = "/updateAccountFund", method = RequestMethod.POST)
	public Result updateAccountFund(Long accountId, String associationType, String code, String accountTransApplyType,
						   Double balance, Double balanceFreeze, Double secondSettlement, Double secondSettlementFreeze,
						   Double creditUsed, Double creditLimit, String payType, Long operatorId, String operatorName, Long operateDate){
		try {
			String username = "系统";
			if (!operatorId.equals(0l)) {
				User user = userService.queryById(operatorId);
				if (user != null) {
					username = user.getName();
				} else {
					username = "";
				}
			}
			accountFundService.updateAccountFund(accountId, AssociationType.getItemByCode(associationType), code, AccountTransApplyType.getItemByCode(accountTransApplyType),
					BigDecimal.valueOf(balance), BigDecimal.valueOf(balanceFreeze), BigDecimal.valueOf(secondSettlement), BigDecimal.valueOf(secondSettlementFreeze),
					BigDecimal.valueOf(creditUsed), BigDecimal.valueOf(creditLimit), PayType.getItemByCode(payType), operatorId,
					username, new Date(operateDate));
		}catch (BusinessException e){
			return new Result(e.getMsg(), false);
		}catch (Exception e){
			return  new Result(e.getMessage(), false);
		}
		return new Result();
	}

	@ResponseBody
	@RequestMapping(value = "/payForCredit", method = RequestMethod.POST)
	public Result payForCredit(Long accountId, String associationType, String code, Long operatorId, String operatorName, Long operateDate){
		try {
			String username = "系统";
			if (!operatorId.equals(0l)) {
				User user = userService.queryById(operatorId);
				if (user != null) {
					username = user.getName();
				} else {
					username = "";
				}
			}
			accountFundService.payForCredit(accountId, AssociationType.getItemByCode(associationType), code,operatorId,
					username, new Date(operateDate));
		}catch (BusinessException e){
			return new Result(e.getMsg(), false);
		}catch (Exception e){
			return  new Result(e.getMessage(), false);
		}
		return new Result();
	}
}
