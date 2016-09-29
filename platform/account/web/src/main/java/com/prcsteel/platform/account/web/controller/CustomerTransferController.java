package com.prcsteel.platform.account.web.controller;

import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.common.aspect.OpAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.dto.CustomerTransferDto;
import com.prcsteel.platform.account.model.query.CustomerTransferQuery;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.account.service.CustomerTransferService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.account.web.utils.WebAppContextUtil;
/**
 * Created by dq on 16-01-11.
 */
@Controller
@RequestMapping("/transfer")
public class CustomerTransferController extends BaseController {
	
    private static final Logger logger = LoggerFactory.getLogger(CustomerTransferController.class);
    
    @Resource
    CustomerTransferService customerTransferService;
    
    @Resource
    OrganizationService organizationService;
    
    @Resource
    ContactService contactService;
    
    /**
     * 客户划转页面
     * @author dq modyfy by zhoucai@prcsteel.com ,客户划转逻辑变更，取当前登录服务中心信息	 
     */
    @RequestMapping("/customertransfer")
	public void customerTransfer(ModelMap out) {
    	Organization organization = getOrganization();
    	out.put("organization",organization);
    }
    
    /**
     * 客户划转数据
     * @author dq
     */
    @ResponseBody
    @RequestMapping("/customertransferdata")
    public PageResult customerTransferData(CustomerTransferQuery query) {
    	PageResult result = new PageResult();    	
    	Integer total = customerTransferService.querCustomerTransferCount(query);  //总数
    	result.setRecordsFiltered(total);

    	List<CustomerTransferDto> data = customerTransferService.querCustomerTransferList(query);
    	result.setData(data);
    	result.setRecordsTotal(data.size());
    	
    	return result;
    }
    
    /**
     * descript： 查询客户是否为管理员
     * author zhoucai@prcsteel.com
     * date :2016-3-16
     */
    @ResponseBody
    @RequestMapping("/queryisadmin")
    public PageResult queryIsAdmin(CustomerTransferQuery query) {    	
    	PageResult result = new PageResult();
    	User loginUser= WebAppContextUtil.getLoginUser();
    	String login_id =loginUser.getLoginId();
    	int total = customerTransferService.queryIsAdminCount(login_id);  //总数
    	result.setRecordsTotal(total);    	  	
    	return result;
    }
    /**
     * descript： 查询当前所有的服务中心列表
     * author zhoucai@prcsteel.com
     * date :2016-3-16
     */
    @ResponseBody
    @RequestMapping("/querymanagerlists")
    public Result queryManagerLists(CustomerTransferQuery query) {    	
    	Result result = new Result();    	   	    		
    	List<Organization> list= organizationService.queryAllBusinessOrg(); 
    	String transType= query.getTransferType();
    	Long orgId =query.getOrgId();
    	if(null!=orgId){
    		for(int i=0;i<list.size();i++){
    			Organization org =(Organization)list.get(i);
    			if(org.getId()==orgId){    			
    				list.remove(i);
    				break;
    			}
    		}    		
    	}
    	result.setData(list);    	
    	return result;
    }
    @ResponseBody
    @RequestMapping("/initmanagers")
    public Result initManagers(CustomerTransferQuery query) {
    	Result result = new Result();
    	List<User> users = contactService.queryUserByOrgId(query.getOrgId());
    	result.setData(users);
    	return result;
    }
    
    /**
     * 客户划转动作
     * @author dq
     */
	@OpAction( key = "accountId")
	@ResponseBody
    @RequestMapping("/customertransferaction")
    public Result customerTransferAction(CustomerTransferQuery query,Long accountId) {
    	Result result = new Result();
    	try {
    		query.setUser(getLoginUser());
    		customerTransferService.customerTransferAction(query);
			result.setSuccess(Boolean.TRUE);
			return result;
		} catch (BusinessException e) {
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMsg());
			return result;
		}
    }
}
