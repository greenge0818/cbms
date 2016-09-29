package com.prcsteel.platform.kuandao.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.kuandao.service.KuandaoPaymentOrderService;
import com.prcsteel.platform.kuandao.web.vo.Result;

@RequestMapping("/kuandao/job")
@Controller
public class KuandaoJobController extends BaseController {

	@Resource
	private KuandaoPaymentOrderService kuandaoPaymentOrderService;
	
	@RequestMapping("/queryMatchMcls.html")
	@ResponseBody
	public Result queryMatchMcls(){
		Result result = new Result();
		String userName = "system";
		User user = getLoginUser();
        if (user!=null) {
        	userName = user.getName();
        }
		kuandaoPaymentOrderService.processMclsMatch(userName);
		return result;
	}
	
	@RequestMapping("/queryUnMatchMcls.html")
	@ResponseBody
	public Result queryUnMatchMcls(){
		Result result = new Result();
		String userName = "system";
		User user = getLoginUser();
        if (user!=null) {
        	userName = user.getName();
        }
		kuandaoPaymentOrderService.processMclsUnmatch(userName);
		return result;
	}
	
	@RequestMapping("/queryNonPaymentOrderMcls.html")
	@ResponseBody
	public Result queryNonPaymentOrderMcls(){
		Result result = new Result();
		kuandaoPaymentOrderService.processNonPaymentOrderDeposit();
		return result;
	}
}
