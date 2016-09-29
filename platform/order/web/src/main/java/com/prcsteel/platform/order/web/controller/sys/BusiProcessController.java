package com.prcsteel.platform.order.web.controller.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.common.exception.BusinessException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.acl.model.dto.UserDto;
import com.prcsteel.platform.order.model.model.ConsignOrderProcess;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.order.service.order.ConsignOrderProcessService;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;

/**
 * Created by Green.Ge on 2015/10/16.
 */
@Controller
@RequestMapping("/sys/busiprocess/")
public class BusiProcessController extends BaseController {
	@Resource
	ConsignOrderProcessService consignOrderProcessService;
	
	@Resource
    OrganizationService organizationService;
	
	@Value("${acl.domain}")
	private String aclDomain;
	
    @RequestMapping("index")
    public void index(ModelMap out) {
        
    }

    @RequestMapping("search")
    @ResponseBody
    public PageResult search(String orgName,String userName,Integer start,Integer length){
    	// 请求参数封装
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orgName", orgName);
        paramMap.put("userName", userName);
        paramMap.put("start", start);
        paramMap.put("length", length);
        
    	List<UserDto> list = consignOrderProcessService.getAllBusinessMen(paramMap);
        Integer total = consignOrderProcessService.getAllBusinessMenTotal(paramMap);
        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());

    	return result;
    }
    
    @RequestMapping("create")
    public void create(ModelMap out){
    	List<UserDto> userList = consignOrderProcessService.getUnsetBusinessMen();
    	out.put("userList", userList);
    }
    
    @RequestMapping("/{userId}")
    public String edit(ModelMap out,  @PathVariable Long userId){
    	User user = userService.queryById(userId);
    	out.put("user", user);
    	Organization org = organizationService.queryById(user.getOrgId());
    	out.put("org", org);
    	List<ConsignOrderProcess> list = consignOrderProcessService.getProcessByUserId(userId);
    	out.put("list", list);
    	out.put("aclDomain", aclDomain);
    	return "sys/busiprocess/edit";
    }
    
    @RequestMapping("save")
    @ResponseBody
    public Result save(String configJSON,Long userId){
    	List<ConsignOrderProcess> list = new Gson().fromJson(configJSON, new TypeToken<List<ConsignOrderProcess>>(){}.getType());
        Result result = new Result();
        try{
        	ResultDto dto = consignOrderProcessService.saveProcess(list);
        	if(dto.isSuccess()){
        		result.setSuccess(true);
        		result.setData("保存成功");
        	}else{
        		result.setSuccess(false);
        		result.setData(dto.getMessage());
        	}
        }catch(BusinessException e){
        	result.setSuccess(false);
    		result.setData(e.getMsg());
        }
    	return result;
    }
    
    @RequestMapping("query.html")
    @ResponseBody
    public Result query(Long orgId,boolean forUserOrgConfig) {
        Result result = new Result();
        result.setSuccess(true);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("orgId", orgId);
        params.put("forUserOrgConfig", forUserOrgConfig);
        result.setData(userService.queryByParam(params));
        return result;
    }

    @RequestMapping("queryById.html")
    @ResponseBody
    public Result queryById(Long id) {
        Result result = new Result();
        result.setSuccess(true);
        result.setData(userService.queryById(id));
        return result;
    }
    
    @RequestMapping("getProcessForUnsetUser")
    @ResponseBody
    public Result getProcessForUnsetUser(Long userId){
        Result result = new Result();
    	List<ConsignOrderProcess> list= consignOrderProcessService.getUnsetProcessByUserId(userId);
    	result.setData(list);
        
    	return result;
    }
    
    @RequestMapping("{userId}/delete")
    @ResponseBody
    public Result delete(@PathVariable Long userId){
        Result result = new Result();
        try{
        	ResultDto dto = consignOrderProcessService.deleteByUserId(userId);
        	if(dto.isSuccess()){
        		result.setSuccess(true);
        		result.setData("删除成功");
        	}else{
        		result.setSuccess(false);
        		result.setData(dto.getMessage());
        	}
        }catch(BusinessException e){
        	result.setSuccess(false);
    		result.setData(e.getMsg());
        }
    	return result;
    }
}
