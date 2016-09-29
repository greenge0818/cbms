package com.prcsteel.platform.smartmatch.web.controller;

import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.model.enums.RoleType;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.RoleService;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.common.vo.TreeView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Resource
    UserService userService;
    @Resource
    RoleService roleService;
    
    private final String NET_SALES_STAFF = "网销人员";
    /**
     *  查找交易员
     * @param orgId
     * @param roleType 
     * @param type 因产品特殊要求网销人员在报价单中推送推送业务开单功能需要查到所有交易员，加上此参数加以区分，不影响找货其他功能调用此方法。 add afeng 2016/8/9
     * @return
     */
    @RequestMapping(value = "/getuserorg.html", method = RequestMethod.POST)
    @ResponseBody
    public Result getUserOrg(@RequestParam("orgId")Long orgId,@RequestParam("roleType")String roleType,
    		@RequestParam(value = "type", required = false) String type) {
        Result result = new Result();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if(!RoleType.NetSalesManager.toString().equals(getUserRole().getRoleType())){
            paramMap.put("userIds", getUserIds());
        }
        if (null != type) {//因产品特殊要求，请勿修改此代码！
        	User user = userService.queryByLoginId(getLoginUser().getLoginId());
        	Role role = roleService.queryById(user.getRoleId());
        	if (NET_SALES_STAFF.equals(role.getName())) {
        		paramMap.put("userIds", null);
        	}
        }
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
