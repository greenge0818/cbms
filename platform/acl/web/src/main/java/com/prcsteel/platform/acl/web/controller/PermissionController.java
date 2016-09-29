package com.prcsteel.platform.acl.web.controller;

import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.common.enums.OpType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.prcsteel.platform.acl.model.dto.PermissionDto;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.RolePermission;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.model.model.UserPermission;
import com.prcsteel.platform.acl.service.PermissionService;
import com.prcsteel.platform.acl.service.RolePermissionService;
import com.prcsteel.platform.acl.service.RoleService;
import com.prcsteel.platform.acl.service.UserPermissionService;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.vo.Result;

/**
 * Created by rolyer on 15-7-15.
 */
@Controller
@RequestMapping("/perm")
public class PermissionController extends BaseController {
    private static final String DEFAULT_PARENT_ID = "0";
    @Resource
    private PermissionService permissionService;
    @Resource
    private RolePermissionService rolePermissionService;
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private UserPermissionService userPermissionService;

    /**
     *     初始化页面
     */
    @RequestMapping("/index/{id}.html")
    public String index(@PathVariable(value = "id") Long id, ModelMap out,@RequestParam(value = "parentId", required = false) String parentId) {
        if (StringUtils.isBlank(parentId)) {
            parentId = DEFAULT_PARENT_ID;
        }
        List<PermissionDto> permissions = permissionService.queryPermissionForShow(Long.parseLong(parentId));
        User user = userService.queryById(id);
        if (user != null) {
            Role role = roleService.queryById(user.getRoleId());
            String roleS = (role != null ? role.getName():"用户角色已被移除");
            out.put("user", user);    //用户信息
            out.put("role", roleS);    //角色信息
        }
        out.put("permissions", permissions);    //所有权限初始化
        return "perm/index";
    }

    /**
     *     初始化页面
     */
    @RequestMapping("/index.html")
    public String index(ModelMap out,@RequestParam(value = "parentId", required = false) String parentId) {
        if (StringUtils.isBlank(parentId)) {
            parentId = DEFAULT_PARENT_ID;
        }
        List<PermissionDto> permissions = permissionService.queryPermissionForShow(Long.parseLong(parentId));
        out.put("permissions", permissions);    //所有权限初始化
        return "perm/index";
    }

    /**
     *     初始化角色下拉框
     * @return
     */
    @RequestMapping(value = "/role/init.html", method = RequestMethod.POST)
    public @ResponseBody Result initRole() {
        Result result = new Result();

        result.setData(roleService.query());
        result.setSuccess(true);
        return result;
    }

    /**
     * 角色下拉框选项改变时，获取角色权限
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/role.html", method = RequestMethod.POST)
    public @ResponseBody Result getRolePermission(Long roleId) {
        Result result = new Result();

        List<RolePermission> rolePermissionList = rolePermissionService.queryByRoleId(roleId);
        if (rolePermissionList.size() > 0) {
            result.setData(rolePermissionList);
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 获取用户特殊权限
     * @param userId
     * @return
     */
    @RequestMapping(value = "/user.html", method = RequestMethod.POST)
    public @ResponseBody Result getUserPermission(Long userId) {
        Result result = new Result();

        List<UserPermission> userPermissionList = userPermissionService.queryByUserId(userId);
        if (userPermissionList.size() > 0) {
            result.setData(userPermissionList);
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 保存角色权限
     * @param prams
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/role/save.html", method = RequestMethod.POST)
    @OpLog(OpType.SavePermissions)
    @OpParam("prams")
	@OpParam("roleId")
    public @ResponseBody Result save(@RequestParam("p[]") List<Long> prams, Long roleId) {
        Result result = new Result();
        if(permissionService.save(prams, roleId, getLoginUser().getLoginId())) {
            result.setSuccess(true);
        }else {
            result.setSuccess(false);
            result.setData("保存权限失败");
        }
        return result;
    }

    /**
     * 保存用户权限
     * @param prams
     * @param userId
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/user/save.html", method = RequestMethod.POST)
    @OpLog(OpType.SaveToUser)
    @OpParam("prams")
	@OpParam("userId")
    @OpParam("roleId")
    public @ResponseBody Result saveToUser(@RequestParam(value = "p[]", required = false) List<Long> prams, Long userId, Long roleId) {
        Result result = new Result();
        if(permissionService.saveToUser(prams, userId, roleId, getLoginUser().getLoginId())){
            result.setSuccess(true);
        }else{
            result.setSuccess(false);
            result.setData("保存权限失败");
        }
        return result;
    }
}
