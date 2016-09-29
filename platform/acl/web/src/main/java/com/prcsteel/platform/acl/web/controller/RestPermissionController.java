package com.prcsteel.platform.acl.web.controller;

import com.prcsteel.platform.acl.model.model.Permission;
import com.prcsteel.platform.acl.service.PermissionService;
import com.prcsteel.platform.acl.service.UserPermissionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author kongbinheng
 * @version v2.0_account
 * @Description: 菜单权限接口api
 * @date 2016/2/17
 */
@RestController
@RequestMapping("/api/permission/")
public class RestPermissionController {

    @Resource
    private PermissionService permissionService;
    @Resource
    private UserPermissionService userPermissionService;

    /**
     * 查询所有权限列表
     */
    @RequestMapping(value = "query/all.html", method = RequestMethod.GET)
    public List<Permission> queryAll() {
        return permissionService.queryAllPermissionOnlyWithCodeAndUrl();
    }

    /**
     * 根据用户id和角色id查询用户权限列表
     */
    @RequestMapping(value = "query/userid/roleid/{userId}/{roleId}.html", method = RequestMethod.GET)
    public List<Permission> queryByUserIdAndRoleId(@PathVariable Long userId, @PathVariable Long roleId) {
        return permissionService.findAllPermissionsForUser(userId, roleId);
    }

    /**
     * 根据权限code和url查询权限
     */
    @RequestMapping(value = "query/code/url/{code}/{url}.html", method = RequestMethod.GET)
    public Permission queryCodeAndUrl(@PathVariable String code, @PathVariable String url) {
        return permissionService.queryByCodeAndUrl(code, url);
    }

    /**
     * 根据用户id查询菜单权限url
     */
    @RequestMapping(value = "query/userid/{userId}.html", method = RequestMethod.GET)
    public List<Permission> queryByUserId(@PathVariable Long userId) {
        return permissionService.queryPermissionByUserId(userId);
    }

    /**
     * 用户是否包含某个权限
     * @param userId
     * @param roleId
     * @param permissionCode
     * @return
     */
    @RequestMapping(value = "hasPermission.html", method = RequestMethod.POST)
    public boolean hasPermission(@PathVariable Long userId, @PathVariable Long roleId, @PathVariable String permissionCode){
        return userPermissionService.hasPermission(userId, roleId, permissionCode);
    }
}
