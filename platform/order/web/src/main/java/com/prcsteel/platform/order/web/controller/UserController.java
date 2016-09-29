package com.prcsteel.platform.order.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.common.vo.TreeView;


@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Resource
    UserService userService;
    /**
     * 查找交易员
     *
     * @return
     */
    @RequestMapping(value = "/getuserorg.html", method = RequestMethod.POST)
    @ResponseBody
    public Result getUserOrg(@RequestParam("orgId")Long orgId,@RequestParam("roleType")String roleType) {
        Result result = new Result();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userIds", getUserIds());
        if (orgId > 0) {
        	paramMap.put("orgId", orgId);
        }
        if(null != roleType && !"".equals(roleType)) {
        	paramMap.put("roleType", roleType);
        }
        List<TreeView> resultList = new ArrayList<TreeView>();
        List<UserOrgDto> userOrgList = userService.queryUserOrg(paramMap);
        for (UserOrgDto item : userOrgList) {
            TreeView treeView = new TreeView();
            treeView.setId(item.getId());
            treeView.setpId(item.getParentId());
            treeView.setName(item.getName());
            resultList.add(treeView);
        }
        //添加置空选项
       // TreeView treeView = new TreeView();
        //treeView.setId(1000);
        //treeView.setName("无");
        //treeView.setpId(0);
        //resultList.add(treeView);

        result.setSuccess(true);
        result.setData(resultList);
        return result;
    }
    
}
