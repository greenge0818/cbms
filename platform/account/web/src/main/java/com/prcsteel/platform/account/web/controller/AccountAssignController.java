package com.prcsteel.platform.account.web.controller;

import com.prcsteel.platform.account.model.query.AccountAssignLogQuery;
import com.prcsteel.platform.account.service.AccountAssignService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.common.vo.PageResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xiyan on 2015/7/17.
 */
@Controller
@RequestMapping("/accountAssign")
public class AccountAssignController extends BaseController{

    @Resource
    AccountAssignService accountAssignService;

    @Resource
    OrganizationService organizationService;

    /**
     * 查看客户划转历史界面，返回当前客户的org modify by zhoucai@prcsteel.com
     * modify_date:2016-3-18
     */
    @RequestMapping("/viewaccountassignlog")
    public String viewAccountAssignLog(ModelMap out){
    	Organization organization = getOrganization();
    	out.put("organization",organization);   
       return "accountAssign/assignLog";
    }


    /**
     * 加载客户划转历史数据
     * @return
     */
    @RequestMapping("/loadaccountassignlogdata")
    @ResponseBody
    public PageResult loadAccountAssignLogData(AccountAssignLogQuery query){ int total = accountAssignService.queryAccountAssignLogTotalByParam(query);
        List list = accountAssignService.queryAccountAssignLogByParam(query);
        return new PageResult(list.size(),total,list);
    }





}
