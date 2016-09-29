package com.prcsteel.platform.account.web.controller;

import com.prcsteel.platform.account.model.model.AccountAcceptDraft;
import com.prcsteel.platform.account.service.AccountAcceptDraftService;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Controller
@RequestMapping("/api/accountAcceptDraft")
public class RestAccountAcceptDraftController extends BaseController{
	@Resource
	AccountAcceptDraftService accountAcceptDraftService;
	@Resource
	UserService userService;

	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Result save(Long accountId, Long acceptDraftId, Double amount, Long userId){
		try {
			accountAcceptDraftService.save(new AccountAcceptDraft(accountId, acceptDraftId, BigDecimal.valueOf(amount)),
					userService.queryById(userId));
		}catch (BusinessException e){
			return new Result(e.getMsg(), false);
		}catch (Exception e){
			return  new Result(e.getMessage(), false);
		}
		return new Result();
	}
}
