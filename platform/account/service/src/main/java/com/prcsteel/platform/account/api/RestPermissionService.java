package com.prcsteel.platform.account.api;

import com.prcsteel.platform.acl.model.model.Permission;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author kongbinheng
 * @version v2.0_account
 * @Description: 菜单权限接口
 * @date 2016/2/17
 */
@RestApi(value="restPermissionService", restServer="aclRestServer")
public interface RestPermissionService {

    /**
     * 查询所有权限列表
     */
    @RestMapping(value = "permission/query/all.html", method = RequestMethod.GET)
    public List<Permission> queryAllPermissionOnlyWithCodeAndUrl();


    /**
     * 根据用户id和角色id查询用户权限列表
     */
    @RestMapping(value = "permission/query/userid/roleid/{userId}/{roleId}.html", method = RequestMethod.GET)
    public List<Permission> findAllPermissionsForUser(@UrlParam("userId") Long userId, @UrlParam("roleId") Long roleId);

    /**
     * 根据权限code和url查询权限
     */
    @RestMapping(value = "permission/query/code/url/{code}/{url}.html", method = RequestMethod.GET)
    public Permission queryByCodeAndUrl(@UrlParam("code") String code, @UrlParam("url") String url);

    /**
     * 根据用户id查询菜单权限url
     */
    @RestMapping(value = "permission/query/userid/{userId}.html", method = RequestMethod.GET)
    public List<Permission> queryPermissionByUserId(@UrlParam("userId") Long userId);

    /**
     * 用户是否包含某个权限
     * @param userId
     * @param roleId
     * @param permissionCode
     * @return
     */
    @RequestMapping(value = "permission/hasPermission.html", method = RequestMethod.POST)
    public boolean hasPermission(@UrlParam("userId") Long userId, @UrlParam("roleId") Long roleId, @UrlParam("permissionCode") String permissionCode);
}
