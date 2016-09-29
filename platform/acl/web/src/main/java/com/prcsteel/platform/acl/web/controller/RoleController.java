package com.prcsteel.platform.acl.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.common.enums.OperateStatus;
import com.prcsteel.platform.acl.model.enums.RoleType;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.RoleService;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.common.vo.TreeView;

/**
 * Created by rolyer on 15-7-15.
 */
@Controller
@RequestMapping("/role/")
public class RoleController extends BaseController {
    @Autowired
    RoleService roleService;

    @RequestMapping("index.html")
    public void index(ModelMap out) {
    	out.put("roleTypes", RoleType.values());
    }

    @RequestMapping(value = "list.html", method = RequestMethod.POST)
    public
    @ResponseBody
    TreeView[] list() {
        List<Role> list = roleService.query();
        TreeView[] roles = new TreeView[list.size()];
        for (int i = 0; i < list.size(); i++) {
            roles[i] = mapRoleToTreeView(list.get(i));
        }
        return roles;
    }

    /**
     * Role数据映射成TreeView
     *
     * @param role
     * @return
     */
    private TreeView mapRoleToTreeView(Role role) {
        TreeView tv = new TreeView();
        tv.setId(role.getId());
        tv.setpId(role.getParentId());
        tv.setName(role.getName());

        return tv;
    }

    private List<Role> getChildren(List<Role> list, long parentId) {
        List<Role> subList = new ArrayList<Role>();
        for (Role r : list) {
            if (r.getParentId() == parentId)
                subList.add(r);
        }
        list.removeAll(subList);
        return subList;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public
    @ResponseBody
    Result add(@RequestParam("name") String name, @RequestParam("code") String code, @RequestParam("parentId") Long parentId, 
    		@RequestParam("type") Integer type,@RequestParam("roleType") String roleType) {
        Result result = new Result();
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(name)) {
            result.setData(OperateStatus.INVALID_PARAMETER.ordinal());
            return result;
        }

        String loginId = this.getLoginUser().getLoginId();
        Role role = new Role();
        role.setName(name);
        role.setParentId(parentId);
        role.setStatus(1);
        role.setRoleType(roleType);
        role.setCode(code);
        role.setType(type);
        role.setLastUpdatedBy(loginId);
        role.setCreatedBy(loginId);
        Integer effect = roleService.add(role);
        result.setSuccess(effect.intValue() == OperateStatus.SUCCESS.ordinal());
        result.setData(effect);

        return result;
    }

    @RequestMapping(value = "del", method = RequestMethod.POST)
    public
    @ResponseBody
    Result del(@RequestParam("id") long id) {
        Result result = new Result();

        String loginId = this.getLoginUser().getLoginId();
        Role role = new Role();
        role.setId(id);
        role.setLastUpdatedBy(loginId);
        if (roleService.disable(role)) {
            result.setSuccess(true);
        }

        return result;
    }

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public
    @ResponseBody
    Result query(@RequestParam("id") long id) {
        Result result = new Result();

        result.setData(roleService.queryById(id));
        result.setSuccess(true);

        return result;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public
    @ResponseBody
    Result edit(Role role) {
        Result result = new Result();

        User user = getLoginUser();
        role.setLastUpdatedBy(user.getLoginId());

        Integer effect = roleService.updateKeyFieldById(role);

        result.setSuccess(effect.intValue() == OperateStatus.SUCCESS.ordinal());
        result.setData(effect);

        return result;
    }

    /**
     * 获取所有role
     *
     * @return
     */
    @RequestMapping(value = "roleList", method = RequestMethod.POST)
    @ResponseBody
    public Result query() {
        Result result = new Result();
        result.setData(roleService.query());
        result.setSuccess(true);
        return result;
    }

}