package com.prcsteel.platform.order.web.controller.cust;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.dto.AccountContactDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.web.controller.BaseController;

/**
 * Created by kongbinheng on 2015/7/14.
 */
@Controller
@RequestMapping("/account/")
public class AccountContactController extends BaseController {

	@Resource
	AccountContactService accountContactService;
	@Resource
	AccountService accountService;
	@ResponseBody
	@RequestMapping(value = "ajaxaccountcontact", method = {RequestMethod.POST})
	public PageResult accountContactInfo(@RequestParam("accountId") Long accountId,
										 @RequestParam("type") String type,
										 @RequestParam("start") Integer start,
										 @RequestParam("length") Integer length) {
		//请求参数封装
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("accountId", accountId);
		paramMap.put("start", start);
		paramMap.put("length", length);
		paramMap.put("type", type);
		/*
		//如果是卖家只显示主联系人
		if(Constant.ACCOUNT_TYPE.SELLER.toString().equals(type)){
			paramMap.put("isMain", 1);
		}
		*/
		List<AccountContactDto> list = accountContactService.queryByAccountId(paramMap);
		Integer total = accountContactService.totalContact(paramMap);

		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());

		return result;
	}

	@RequestMapping("buyer/{accountId}/contactlist")
	public String buyerContactList(ModelMap out,@PathVariable Long accountId) {
		User user = getLoginUser();
		Long userId = user.getId();
		out.put("account", accountService.selectByPrimaryKey(accountId).getAccount());
		out.put("userId", userId);
		out.put("type", Constant.ACCOUNT_TYPE.BUYER.toString());  //买家
		if(getUserIds()!=null){
			out.put("canSeeIds", StringUtils.join(getUserIds().toArray(),","));
		}
		
		//请求参数封装
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("accountId", accountId);
		paramMap.put("type", Constant.ACCOUNT_TYPE.BUYER.toString());  //卖家
		paramMap.put("isMain", Constant.YES);  //查主联系人

		out.put("totalIsMain", accountContactService.totalIsMain(paramMap));
		return "account/contact";
	}

	@RequestMapping("seller/{accountId}/contactlist")
	public String sellerContactList(ModelMap out,@PathVariable Long accountId) {
		User user = getLoginUser();
		Long userId = user.getId();
		out.put("account", accountService.selectByPrimaryKey(accountId).getAccount());
		out.put("userId", userId);
		out.put("type", Constant.ACCOUNT_TYPE.SELLER.toString());  //卖家
		if(getUserIds()!=null){
			out.put("canSeeIds", StringUtils.join(getUserIds().toArray(),","));
		}
		//请求参数封装
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("accountId", accountId);
		paramMap.put("type", Constant.ACCOUNT_TYPE.SELLER.toString());  //卖家

		out.put("totalIsMain",accountContactService.totalIsMain(paramMap));
		return "account/contact";
	}

	@ResponseBody
	@RequestMapping(value = { "/contact/save" }, method = { RequestMethod.POST })
	@OpLog(OpType.AddContact)
    @OpParam("accountId")
	@OpParam("tel")
	@OpParam("deptName")
	@OpParam("qq")
	@OpParam("email")
	@OpParam("isMain")
	@OpParam("status")
	@OpParam("note")
	@OpParam("type")
	public Result contactSave(@RequestParam("accountId") String accountId,
							  @RequestParam("name") String name,
							  @RequestParam("tel") String tel,
							  @RequestParam("deptName") String deptName,
							  @RequestParam("qq") String qq,
							  @RequestParam("email") String email,
							  @RequestParam("isMain") String isMain,
							  @RequestParam("status") String status,
							  @RequestParam("note") String note,
							  @RequestParam("type") String type){
		Result result = new Result();

		//请求参数封装
		result = checkMobile(tel) ;
		if(!result.isSuccess()){
			return result;
		}

		Date nowDate = new Date();
		//获取当前登录用户
		User user = getLoginUser();
		Long userId = user.getId();
		String loginId = user.getLoginId();

		AccountContact accountContact = new AccountContact();
		accountContact.setAccountId(Long.parseLong(accountId));
		accountContact.setName(name.trim());
		accountContact.setTel(tel.trim());
		accountContact.setDeptName(deptName.trim());
		accountContact.setQq(qq.trim());
		accountContact.setEmail(email.trim());
		accountContact.setIsMain(Integer.parseInt(isMain));
		accountContact.setStatus(Integer.parseInt(status));
		accountContact.setManager(userId);
		accountContact.setNote(note.trim());
		accountContact.setType(type);
		accountContact.setCreated(nowDate);
		accountContact.setCreatedBy(loginId);
		accountContact.setLastUpdated(nowDate);
		accountContact.setLastUpdatedBy(loginId);
		accountContact.setModificationNumber(0);

		boolean flag = accountContactService.add(accountContact);
		if(flag) {
			result.setSuccess(true);
			result.setData("保存成功！");
			//CBMS新增客户联系人时，通知微信端 add by wangxianjun 20150526 
			accountContactService.noticeWechat(tel.trim(),getLoginUser());
		} else {
			result.setSuccess(false);
			result.setData("保存失败！");
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/contact/edit" }, method = { RequestMethod.POST })
	@OpLog(OpType.ContactEdit)
    @OpParam("id")
	public Result contactEdit(@RequestParam("id") String id){
		Result result = new Result();
		AccountContact accountContact = accountContactService.queryById(id);
		if(accountContact != null) {
			result.setSuccess(true);
			result.setData(accountContact);
		} else{
			result.setSuccess(false);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/contact/update" }, method = { RequestMethod.POST })
	public Result contactUpdate(@RequestParam("id") Long id,
								@RequestParam("accountId") Long accountId,
								@RequestParam("name") String name,
								@RequestParam("tel") String tel,
								@RequestParam("deptName") String deptName,
								@RequestParam("qq") String qq,
								@RequestParam("email") String email,
								@RequestParam("isMain") String isMain,
								@RequestParam("status") String status,
								@RequestParam("note") String note,
								@RequestParam("type") String type){
		Result result = new Result();

		//修改不做手机号判断
		if(!accountContactService.queryById(String.valueOf(id)).getTel().equals(tel)){
			result = checkMobile(tel) ;
			if(!result.isSuccess()){
				return result;
			}
		}

		Date nowDate = new Date();
		//获取当前登录用户
		User user = getLoginUser();
		Long userId = user.getId();
		String loginId = user.getLoginId();

		AccountContact accountContact = new AccountContact();
		accountContact.setId(id);
		accountContact.setAccountId(accountId);
		accountContact.setName(name.trim());
		accountContact.setTel(tel.trim());
		accountContact.setDeptName(deptName.trim());
		accountContact.setQq(qq.trim());
		accountContact.setEmail(email.trim());
		accountContact.setIsMain(Integer.parseInt(isMain));
		accountContact.setStatus(Integer.parseInt(status));
		accountContact.setManager(userId);
		accountContact.setNote(note.trim());
		accountContact.setLastUpdated(nowDate);
		accountContact.setLastUpdatedBy(loginId);

		boolean flag = accountContactService.edit(accountContact, type);
		if(flag) {
			result.setSuccess(true);
			result.setData("编辑成功！");
		} else {
			result.setSuccess(false);
			result.setData("编辑失败！");
		}
		return result;
	}
	//检查手机号码是否重复
	private Result checkMobile(String tel){
		Result result = new Result();
		AccountContact usedContact = accountContactService.queryByTel(tel.trim());
		if(usedContact!=null){
			result.setSuccess(false);
			Account usedAccount = accountService.queryById(usedContact.getAccountId());
			if(usedAccount==null){
				result.setData("手机号已被联系人【"+usedContact.getName()+"】占用，但是该联系人所对应的客户已查不到,客户ID:"+usedContact.getId());
			}else{
				result.setData("手机号已被客户【"+usedAccount.getName()+"】下的联系人【"+usedContact.getName()+"】占用，请更换再试！");
			}
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = { "/contact/lockAndUnLock" }, method = { RequestMethod.POST })
	@OpLog(OpType.LockAndUnLock)
	@OpParam("id")
    @OpParam("status")
	public Result contactUpdate(@RequestParam("id") Long id,
								@RequestParam("status") String status){
		Result result = new Result();
		Date nowDate = new Date();
		//获取当前登录用户
		User user = getLoginUser();
		String loginId = user.getLoginId();

		AccountContact accountContact = new AccountContact();
		accountContact.setId(id);
		accountContact.setStatus(Integer.parseInt(status));
		accountContact.setLastUpdated(nowDate);
		accountContact.setLastUpdatedBy(loginId);

		boolean flag = accountContactService.disabledById(accountContact);
		if(flag)
			result.setSuccess(true);
		else
			result.setSuccess(false);

		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/contact/querycount" }, method = { RequestMethod.POST })
	public Result queryContactCount(@RequestParam("accountId") Long accountId) {
		Result result = new Result();
		//请求参数封装
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("accountId", accountId);
		result.setSuccess(true);
		result.setData(accountContactService.totalIsMain(paramMap));
		return result;
	}
}
 