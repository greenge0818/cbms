package com.prcsteel.platform.order.web.controller;

import com.prcsteel.platform.order.model.model.AcceptDraft;
import com.prcsteel.platform.order.service.acceptdraft.AcceptDraftService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chengui
 * @version v2.0_account
 * @Description: 银票管理Api
 * @date 2016/4/21
 */
@RestController
@RequestMapping("/api/acceptdraft/")
public class RestAcceptDraftController extends BaseController {
	@Resource
	AcceptDraftService acceptDraftService;
    
   /**
    * 应付款申请
    * @param accountId 客户id
    * @param status 银票状态
    * @return
    */
	@RequestMapping(value = "query", method = RequestMethod.POST)
	public List<AcceptDraft> query(@RequestParam("accountId") Long accountId, @RequestParam("status") String status) {
		return  acceptDraftService.queryByAccountStatus(accountId, status);
	}
}
