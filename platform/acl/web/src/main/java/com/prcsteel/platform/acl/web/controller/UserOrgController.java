package com.prcsteel.platform.acl.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.acl.model.dto.UserDto;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.model.model.UserOrg;
import com.prcsteel.platform.acl.service.UserOrgService;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.service.CommonService;

/**
 * Created by Green.Ge on 2015/10/21.
 */
@Controller
@RequestMapping("/sys/userorg/")
public class UserOrgController extends BaseController {
	@Resource
	UserOrgService userOrgService;
	@Resource
	CommonService commonService;
	
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
        
    	List<UserDto> list = userOrgService.getSetUser(paramMap);
        Integer total = userOrgService.getSetUserTotal(paramMap);
        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());

    	return result;
    }
    
    @RequestMapping("create")
    public void create(ModelMap out){
    	List<UserDto> userList = userOrgService.getUnsetUser();
    	out.put("userList", userList);
    	List<Organization> orgList =commonService.getAllOrganization();
    	out.put("orgList", orgList);
    }
    
    @RequestMapping("/{userId}")
    public String edit(ModelMap out,  @PathVariable Long userId){
    	User user = userService.queryById(userId);
    	out.put("user", user);
    	List<UserOrg> list = userOrgService.getConfigByUserId(userId);
    	out.put("list", list);
    	List<Organization> orgList =commonService.getAllOrganization();
    	out.put("orgList", orgList);
    	return "sys/userorg/edit";
    }
    
    @RequestMapping("save")
    @ResponseBody
    public Result save(long orgIds[],Long userId){
    	Result result = new Result();
    	List<UserOrg> list = new ArrayList<UserOrg>();
    	if(userId==null||userId<=0){
    		result.setSuccess(false);
    		result.setData("用户ID非法");
    		return result;
    	}
    	if(orgIds==null||orgIds.length==0){
    		result.setSuccess(false);
    		result.setData("没有需要保存的数据");
    		return result;
    	}
        
    	for(Long orgId:orgIds){
    		UserOrg uo = new UserOrg();
    		uo.setUserId(userId);
    		uo.setOrgId(orgId);
    		list.add(uo);
    	}
        try{
        	ResultDto dto = userOrgService.saveProcess(list);
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
    
    @RequestMapping("{userId}/delete")
    @ResponseBody
    public Result delete(@PathVariable Long userId){
        Result result = new Result();
        try{
        	ResultDto dto = userOrgService.deleteByUserId(userId);
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
