package com.prcsteel.platform.account.web.controller;

import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.account.web.vo.RestResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @date :2016-5-30
 * @author :chengui@prcsteel.com
 */
@Controller
@RequestMapping("/api/accountcontact/")
public class RestAccountContactController extends BaseController{
	@Resource
	AccountContactService accountContactService;
	/**
	 * 根据contactId获取客户Id列表
	 * @param contactId
	 */
	@ResponseBody
	@RequestMapping(value = "accountIds/{contactId}", method = RequestMethod.GET)
	public RestResult getAccountIdsByContactId(@PathVariable("contactId") Integer contactId){
		RestResult result = new RestResult();
		List<Long> accountIds = accountContactService.getAccountIdsByContactId(contactId);
		if(CollectionUtils.isEmpty(accountIds)){
			result.setSuccess(false);
			result.setMsg("联系人ID为"+contactId+"的客户不存在");
			return result;
		}
		result.setData(accountIds);
		return result;
	}

}
