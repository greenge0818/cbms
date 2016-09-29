package com.prcsteel.platform.acl.web.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.acl.model.model.Permission;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.PermissionService;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.common.vo.TreeView;

/**
 * Created by rolyer on 15-8-9.
 */
@Controller
@RequestMapping("/perms/")
public class PermController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("index.html")
    public void index(ModelMap out) {
    }

    @RequestMapping(value = "list.html", method = RequestMethod.POST)
    public @ResponseBody
    TreeView[] list() {
        List<Permission> list = permissionService.query();
        TreeView[] roles = new TreeView[list.size()];
        for (int i = 0; i < list.size(); i++) {
            roles[i] = mapPermissionToTreeView(list.get(i));
        }
        return roles;
    }

    /**
     * 数据映射成
     * @param permission
     * @return
     */
    private TreeView mapPermissionToTreeView(Permission permission){
        TreeView tv = new TreeView();
        tv.setId(permission.getId());
        tv.setpId(permission.getParentId());
        tv.setName(permission.getName());

        return tv;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public
    @ResponseBody
    Result add(Long parentId, String name, String code, String url) {
        Result result = new Result();
        if (parentId == null || StringUtils.isEmpty(name) || StringUtils.isEmpty(code) || StringUtils.isEmpty(url)) {
            result.setData("参数不完整");
            return result;
        }
        if (permissionService.hasExsistCode(code, null)) {
            result.setData("权限值（code）重复");
            return result;
        }

        if (permissionService.hasExsistUrl(url, null)){
            result.setData("URL地址重复");
            return result;
        }

        User user = getLoginUser();

        result.setSuccess(permissionService.insert(parentId, name, code, url, user.getLoginId()) > 0);

        return result;
    }

    @RequestMapping(value = "del", method = RequestMethod.POST)
    public
    @ResponseBody
    Result del(@RequestParam("id") long id) {
        Result result = new Result();

        result.setSuccess(permissionService.delete(id));

        return result;
    }

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public
    @ResponseBody
    Result query(@RequestParam("id") long id) {
        Result result = new Result();

        result.setData(permissionService.queryById(id));
        result.setSuccess(true);

        return result;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public @ResponseBody Result edit(Long id, String name, String code, String url) {
        Result result = new Result();
        if (permissionService.hasExsistCode(code, id)) {
            result.setData("权限值（code）重复");
            return result;
        }

        if (permissionService.hasExsistUrl(url, id)){
            result.setData("URL地址重复");
            return result;
        }
        result.setSuccess(permissionService.update(id, name, code, url)>0);

        return result;
    }
}
