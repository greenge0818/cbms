package com.prcsteel.platform.account.web.controller;

import com.prcsteel.platform.account.model.dto.AccountAllDto;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.CustGroupingInforService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @date :2016-5-7
 * @decrpiton:迁移风控上线代码 将归属于acl和account进行代码迁移，并将服务调用统一成调用rest服务进行跨应用调用
 * @author :zhoucai@prcsteel.com
 */
@Controller
@RequestMapping("/api/account/")
public class RestAccountController extends BaseController{
	@Resource
	AccountService accountService;
	@Resource
	CustGroupingInforService custGroupingInforService;


	/**
	 * 根据账户名称查询账户信息
	 * @param accountName
	 * @return Account
	 */
	@ResponseBody
	@RequestMapping(value = "selectaccountbyname", method = RequestMethod.POST)
	public Account selectAccountByName(@RequestParam("accountName") String accountName){
		return accountService.selectAccountByName(accountName);
	}

	/**
	 * 根据账户id查询账户信息
	 * @param accountId
	 * @return Account
	 */
	@ResponseBody
	@RequestMapping(value = "queryaccountbyid", method = RequestMethod.POST)
	public Account selectAccountById(@RequestParam("accountId") Long accountId){
		return accountService.queryById(accountId);
	}

	/**
	 * 按部门Id查询客户信息（若Id本身为客户Id，则返回当前客户信息）
	 * @param id
	 * @return Account
	 * @author chengui
	 */
	@ResponseBody
	@RequestMapping(value = "accounts/{id}/parent", method = RequestMethod.GET)
	public Account selectParentById(@PathVariable("id") Long id){
		return accountService.selectParentById(id);
	}
	/**
	 * 查询所有的卖家客户信息
	 * @return List<Account>
	 */
	@ResponseBody
	@RequestMapping(value = "selectallselleraccount", method = RequestMethod.POST)
	public List<AccountAllDto> selectAllSellerAccount(){
		return accountService.selectAllSellerAccount();
	}
	/**
	 * 查询所有的买家客户信息
	 * @return List<Account>
	 */
	@ResponseBody
	@RequestMapping(value = "selectallbuyeraccount", method = RequestMethod.POST)
	public List<AccountAllDto> selectAllBuyerAccount(){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("accountTag", AccountTag.buyer.getCode());
		paramMap.put("start", 0);
	    paramMap.put("length", Integer.MAX_VALUE);
	    List<Account> accounts = accountService.listAccountByName(paramMap);
	    List<AccountAllDto> accountAllDto = new ArrayList<AccountAllDto>();
	    for (Account account : accounts) {
	    	AccountAllDto accountAll = new AccountAllDto();
	    	accountAll.setId(account.getId());
	    	accountAll.setName(account.getName());
	    	accountAllDto.add(accountAll);
		}
		return accountAllDto;
	}

	/**
	 * 按客户ID查询分组记录数
	 */
	@ResponseBody
	@RequestMapping(value = "getGroupCountByAccountId", method = RequestMethod.POST)
	public int getGroupCountByAccountId(@RequestParam("accountId") Long accountId){
		return custGroupingInforService.getGroupCountByAccountId(accountId);
	}
}
