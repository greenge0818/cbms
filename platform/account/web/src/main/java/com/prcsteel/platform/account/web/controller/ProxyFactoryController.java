package com.prcsteel.platform.account.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.enums.AccountTabId;
import com.prcsteel.platform.account.model.model.ProxyFactory;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.account.service.ProxyFactoryService;
import com.prcsteel.platform.account.web.vo.Result;
import com.prcsteel.platform.acl.model.model.User;

/**
 * 代理钢厂控制器 
 * @author tangwei
 *
 */
@Controller
@RequestMapping("/proxyfactory")
public class ProxyFactoryController extends BaseController{

	@Autowired
	private ContactService contactService;
	
	@Autowired
	private ProxyFactoryService proxyFactoryService;
	
    @RequestMapping("{accountId}/list")
    public String accountInfo(ModelMap out,@PathVariable("accountId") Long accountId) {
        AccountDto dto = contactService.getCompanyById(accountId);
    	List<ProxyFactory> proxyFactoryList= proxyFactoryService.findProxyFactoryByAccountId(accountId);
        out.put("accountdto", dto);
        out.put("tabId", AccountTabId.proxyFactory);
        out.put("proxyFactoryList", proxyFactoryList);
		return "company/proxyFactory/list";
    }
    
    @RequestMapping(value="saveproxyfactory",method=RequestMethod.POST)
    @ResponseBody
    public Result saveProxyFactory(ModelMap out,@ModelAttribute ProxyFactory record) {
    	Result result = new Result();
    	User user = getLoginUser();
    	if(record.getId() == null || record.getId()<=0){
    		proxyFactoryService.addSaveProxyFactory(record,user);
    	}else{
    		proxyFactoryService.editSaveProxyFactory(record,user);
    	}
    	if(record.getId() == null || record.getId()<=0){
    		result.setSuccess(false);
    	}
    	result.setData(record);
    	return result;
    }
    
    @RequestMapping(value="deleteproxyfactory",method=RequestMethod.POST)
    @ResponseBody
    public Result deleteProxyFactory(long id){
    	Result result = new Result();
    	User user = getLoginUser();
    	proxyFactoryService.deleteProxyFactory(id, user);
    	result.setSuccess(true);
    	return result;
    }

}
