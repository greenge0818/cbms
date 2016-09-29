package com.prcsteel.platform.acl.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.vo.Result;

/**
 * Created by Rabbit Mao on 2015/7/18.
 */
@Controller
@RequestMapping("/org/")
public class OrganizationController extends BaseController {
    @Resource
    private OrganizationService organizationService;
    @Resource
    private UserService userService;

    @RequestMapping("index.html")
    public void index(ModelMap out) {
        List<Organization> list = organizationService.getAllOrganization();
        if(!list.isEmpty()){
            out.put("orgId", list.get(0).getId());
        }
    }

    @RequestMapping(value = "loadByOrgId.html", method = RequestMethod.POST)
    @ResponseBody
    public Result load(Long orgId) {
        Map<String, Object> param = new HashMap<>();
        Result result = new Result();

        List<Long> orgIds = organizationService.getAllChildOrgId(orgId, 0);
        param.put("orgId", orgId);
        param.put("orgIds", orgIds);
        result.setData(organizationService.selectOrgInfoByParam(param));
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("addOrganization.html")
    @OpLog(OpType.AddOrganization)
    @OpParam("organization")
    @ResponseBody
    public Result addOrganization(Organization organization, @RequestParam("deliveryTypes[]") List<String> deliveryTypes) {
        Result result = new Result();
        result.setSuccess(true);
        try {
            organizationService.addOrganization(organization, deliveryTypes, getLoginUser().getLoginId());
        }catch (BusinessException e){
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    @RequestMapping("addDepartment.html")
    @OpLog(OpType.AddOrganization)
    @OpParam("organization")
    @ResponseBody
    public Result addDepartment(Organization organization) {
        Result result = new Result();
        result.setSuccess(true);
        try {
            organizationService.addDepartment(organization, getLoginUser().getLoginId());
        }catch (BusinessException e){
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    @RequestMapping("loadAllUser.html")
    @ResponseBody
    public Result loadAllUser() {
        Result result = new Result();
        result.setSuccess(true);
        result.setData(userService.queryByParam(null));
        return result;
    }
    
    @RequestMapping("updateDepartment.html")
    @OpLog(OpType.UpdateOrganization)
    @OpParam("organization")
    @OpParam("deliveryTypes")
    @ResponseBody
    public Result updateDepartment(Organization organization) {
        Result result = new Result();
        result.setSuccess(true);
        try {
            organizationService.updateDepartment(organization,getLoginUser().getLoginId());
        }catch (BusinessException e){
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }
    
    @RequestMapping("updateOrganization.html")
    @OpLog(OpType.UpdateOrganization)
    @OpParam("organization")
    @OpParam("deliveryTypes")
    @ResponseBody
    public Result updateOrganization(Organization organization,@RequestParam("deliveryTypes[]") List<String> deliveryTypes) {
        Result result = new Result();
        result.setSuccess(true);
        try {
            organizationService.updateOrganization(organization, deliveryTypes, getLoginUser().getLoginId());
        }catch (BusinessException e){
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }
    
    
}
